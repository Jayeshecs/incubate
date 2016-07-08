/**
 * 
 */
package data.integration.eip.poi.iterator;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * XLSX streaming content iterator.
 * <pre>
 * try {
 * 	XlsxIterator iterator = new XlsxIterator("Sheet0", new InputStream(new File("/some/location/to/file.xslx")));
 * 	iterator.init();
 * 	while(iterator.hasNext()) {
 * 		List<String> record = iterator.next();
 * 		...process record..
 * 	}
 * } finally {
 * 	iterator.close();
 * }
 * </pre>
 * 
 * @author Prajapati
 */
public class XlsxIterator  implements Iterator<String>, Closeable {
	
	private static Logger LOG = Logger.getLogger(XlsxIterator.class);
	
	private static final char DELIMITER = ',';

	private static final char ESCAPE_CHARACTER = '"';
	
	private String worksheetName;
	private InputStream in;
	private Workbook workbook;
	private Iterator<Row> iterator;

	private int worksheetIndex = -1;
	private int noOfCells = 0;
	private Locale locale;

	private DataFormatter formatter;

	/**
	 * Construct XlsxIterator instance
	 * @param worksheetName name of the worksheet from where data needs to be read
	 * @param in InputStream pointing to XLSX content
	 */
	public XlsxIterator(String worksheetName, InputStream in) {
		this.worksheetName = worksheetName;
		this.in = in;
	}

	/**
	 * Construct XlsxIterator instance
	 * @param worksheetName name of the worksheet from where data needs to be read
	 * @param in InputStream pointing to XLSX content
	 */
	public XlsxIterator(int worksheetIndex, InputStream in) {
		this.worksheetIndex = worksheetIndex;
		this.in = in;
	}

	@Override
	public void close() throws IOException {
		if(workbook != null) {
			LOG.info("Closing workbook");
			workbook.close();
			workbook = null;
		}
		if(in != null) {
			LOG.info("Closing input stream");
			in.close();
			in = null;
		}
	}
	
	/**
	 * Set desired Locale to locale based formatting else default locale will be used.
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * Initialize XlsxIterator
	 * 
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	public void init() throws IOException, InvalidFormatException {
		LOG.info("Initializing XlsxIterator...");
		// initialize iterator to start reading given inputstream as XLSX file using POI XSSF workbook APIs
		//workbook = new XSSFWorkbook(in);
		workbook = WorkbookFactory.create(in);
		workbook.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);
		Sheet sheet = null;
		if(worksheetIndex == -1) {
			LOG.info("Accessing sheet with name " + worksheetName);
			worksheetIndex = workbook.getSheetIndex(worksheetName);
		}
		workbook.setActiveSheet(worksheetIndex);
		LOG.info("Accessing sheet at index " + worksheetIndex);
		sheet = workbook.getSheetAt(worksheetIndex);
		if(sheet == null) {
			LOG.error("Sheet with name:" + worksheetName + " or index: " + worksheetIndex + " not found and hence initialization has failed.");
			close();
			throw new IllegalArgumentException("Sheet with name:" + worksheetName + " or index: " + worksheetIndex + " not found and hence initialization has failed.");
		}
		LOG.info("Worksheet name " + sheet.getSheetName());
		iterator = sheet.iterator();
		noOfCells = sheet.getRow(0).getLastCellNum();
        if(locale == null) {
            formatter = new DataFormatter();
    		LOG.info("DataFormatter initialized with default locale - " + Locale.getDefault());
        } else  {
            formatter = new DataFormatter(locale);
    		LOG.info("DataFormatter initialized with locale - " + locale);
        }
		LOG.info("XlsxIterator initialized");
	}

	@Override
	public boolean hasNext() {
		boolean hasNext = iterator.hasNext();
		LOG.debug("hasNext: " + hasNext);
		return hasNext;
	}

	@Override
	public String next() {
		Row row = iterator.next();
		StringBuilder record = new StringBuilder();
		for (int i = 0; i < noOfCells; ++i) {
			Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
			if (i > 0) {
				record.append(DELIMITER);
			}

			// Is it a formula one?
			if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
					handleStringCell(record, cell);
				} else {
					handleNonStringCell(record, cell, formatter);
				}
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				handleStringCell(record, cell);
			} else {
				handleNonStringCell(record, cell, formatter);
			}
		}
		String result = record.toString();
		LOG.debug("next: " + result);
		return result;
	}
    
    private String escapeDelimiter(String value) {

		if (value == null) {
			value = "";
		}

		if (value.indexOf(DELIMITER) != -1) {
			return new StringBuilder().append(ESCAPE_CHARACTER).append(value).append(ESCAPE_CHARACTER).toString();
		}
		return value;
	}
	
    /**
     * NOTE: COPY FROM XSSFExcelExtractor
     * @param text
     * @param cell
     */
    private void handleStringCell(StringBuilder text, Cell cell) {
        text.append(escapeDelimiter(cell.getRichStringCellValue().getString()));
    }

	/**
     * NOTE: COPY FROM XSSFExcelExtractor
     * @param text
     * @param cell
     * @param formatter
     */
    private void handleNonStringCell(StringBuilder text, Cell cell, DataFormatter formatter) {
        int type = cell.getCellType();
        if (type == Cell.CELL_TYPE_FORMULA) {
            type = cell.getCachedFormulaResultType();
        }

        if (type == Cell.CELL_TYPE_NUMERIC) {
            CellStyle cs = cell.getCellStyle();

            if (cs.getDataFormatString() != null) {
            	String dataFormatString = cs.getDataFormatString();
            	// TFIMX-5383 - Need to update dateformat to use slash instead of dot because dot as date field separator NOT supported by TFIM3
            	if(DateUtil.isADateFormat(cs.getDataFormat(), dataFormatString)) {
            		dataFormatString = dataFormatString.replace('.', '/');
            	}
				text.append(escapeDelimiter(formatter.formatRawCellContents(
                        cell.getNumericCellValue(), cs.getDataFormat(), dataFormatString
                        )));
                return;
            }
        }

        // No supported styling applies to this cell
        XSSFCell xcell = (XSSFCell)cell;
        text.append(escapeDelimiter(xcell.getRawValue()));
    }


	@Override
	public void remove() {
		LOG.error("remove() operation is not supported by XlsxIterator"); 
		throw new UnsupportedOperationException("remove operation is not supported by XlsxIterator"); //$NON-NLS-1$
	}

}
