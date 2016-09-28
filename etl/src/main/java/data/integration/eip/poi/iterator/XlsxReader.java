/**
 * 
 */
package data.integration.eip.poi.iterator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Component;

/**
 * Objective of this class is to provide method {@link #split(Exchange, InputStream)} which can be used for reading XLSX worksheet data as record in the form List<String>
 * 
 * @author Prajapati
 */
@Component
public class XlsxReader {
	
	public static final String PROP_WORKSHEET_NAME = "worksheet.name";
	public static final String PROP_WORKSHEET_INDEX = "worksheet.index";
	public static final String PROP_FORMAT_LOCALE = "format.locale";
	
	private static Logger LOG = Logger.getLogger(XlsxReader.class);

	/**
	 * Split the given inputStream XLSX content in individual record from worksheet identified by name described in worksheet.name
	 * 
	 * @param exchange Exchange object from where {@link #PROP_WORKSHEET_NAME} will be retrieved
	 * @param inputStream
	 * @return Iterator<List<String>>
	 * @throws IllegalArgumentException - if {@link #PROP_WORKSHEET_NAME} is not set in Exchange object
	 */
	public Iterator<String> split(Exchange exchange, @Body InputStream inputStream) {
		String worksheetName = exchange.getProperty(PROP_WORKSHEET_NAME, String.class); //$NON-NLS-1$
		String worksheetIndex = exchange.getProperty(PROP_WORKSHEET_INDEX, String.class); //$NON-NLS-1$
		if(worksheetName == null && worksheetIndex == null) {
			LOG.error("Exchange property 'worksheet.name' i.e. XlsxReader.PROP_WORKSHEET_NAME or 'worksheet.index' i.e. i.e. XlsxReader.PROP_WORKSHEET_INDEX must be set before using XlsxReader#split()");
			throw new IllegalArgumentException("Exchange property 'worksheet.name' i.e. XlsxReader.PROP_WORKSHEET_NAME or 'worksheet.index' i.e. i.e. XlsxReader.PROP_WORKSHEET_INDEX must be set before using XlsxReader#split()");
		}
		XlsxIterator xlsxIterator = worksheetIndex != null ? 
				new XlsxIterator(Integer.parseInt(worksheetIndex), inputStream) : 
					new XlsxIterator(worksheetName, inputStream);
		String localeStr = exchange.getProperty(PROP_FORMAT_LOCALE, String.class); //$NON-NLS-1$
		if(localeStr != null) {
			LOG.info("Custom locale : " + localeStr);
			String[] localeParts = localeStr.split("_");
			Locale.Builder builder = new Locale.Builder();
			builder.setLanguageTag(localeParts[0]);
			if(localeParts.length > 1) {
				builder.setRegion(localeParts[1]);
			}
			if(localeParts.length > 2) {
				builder.setVariant(localeParts[2]);
			}
			Locale locale = builder.build();
			LOG.info("Derived locale: " + locale); //$NON-NLS-1$
			xlsxIterator.setLocale(locale);
		} else {
			LOG.info("No custom locale supplied. Default locale will be used for formatting."); //$NON-NLS-1$
		}
		try {
			xlsxIterator.init();
		} catch (IOException e) {
			LOG.error("IOException occurred while initializing XlsxIterator", e);
			throw new IllegalStateException("Unable to read body as XLSX content", e);
		} catch (InvalidFormatException e) {
			LOG.error("InvalidFormatException occurred while initializing XlsxIterator", e);
			throw new IllegalStateException("Unable to read body as XLS content", e);
		}
		return xlsxIterator;
	}
}
