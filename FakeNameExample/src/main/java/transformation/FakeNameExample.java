/**
 * 
 */
package transformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.sequencevectors.iterators.AbstractSequenceIterator;
import org.deeplearning4j.models.sequencevectors.sequence.Sequence;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.LineReader;

/**
 * @author Prajapati
 *
 */
public class FakeNameExample {

    private static Logger LOG = LoggerFactory.getLogger(FakeNameExample.class);
	
	public static void main(String[] args) throws Exception {
		String[] trainingDatas = { "training/male_10000.csv", "training/female_10000.csv"};
		String[] testData = { "test/male_5000_female_5000.csv" };
		
		LOG.info("Loading training records ...");
		List<FakeNameBean> trainingRecords = new ArrayList<FakeNameBean>();
		for(String trainingData : trainingDatas) {
			loadFakeNameBeans(trainingRecords, trainingData);
		}
		LOG.info("Total training records loaded - " + trainingRecords.size());
		LOG.info("");
		LOG.info("Press ENTER to continue..."); System.in.read();
		
		LOG.info("Building model....");
		Word2Vec vec = new Word2Vec.Builder()
	        .minWordFrequency(5)
	        .iterations(3)
	        .layerSize(1000)
	        .seed(42)
	        .windowSize(5)
	        .iterate(new AbstractSequenceIterator<VocabWord>(new FakeNameBeanIterator(trainingRecords)) {
	        	// DO NOTHING
	        })
	        //.tokenizerFactory(t)
	        .build();
		
		LOG.info("");
		LOG.info("Start training Word2Vec model ...");
        vec.fit();
		LOG.info("Training Word2Vec model completed!");
		LOG.info("");
		LOG.info("Press ENTER to continue..."); System.in.read();
		
		LOG.info("Loading test records ...");
		List<FakeNameBean> testRecords = new ArrayList<FakeNameBean>();
		loadFakeNameBeans(testRecords, testData[0]);
		LOG.info("Total test records loaded - " + testRecords.size());
		
		// Write word vectors
        WordVectorSerializer.writeWordVectors(vec, "FakeNameVectors.txt");
        
//        BarnesHutTsne tsne = plotTSNE();
//        LOG.info("Press ENTER to continue..."); System.in.read();
//        
//        UiServer server = UiServer.getInstance();
//        LOG.info("Started on port " + server.getPort());
//		LOG.info("Press ENTER to continue..."); System.in.read();
		
		FakeNameFieldsEnum field = FakeNameFieldsEnum.NAME;
		double accuracy5 = 0.7;
		double accuracy9 = 0.9;
		
		int samples = 20;
		for(FakeNameBean record : testRecords) {
			List<String> similarWordsInVocabTo = vec.similarWordsInVocabTo(record.getValue(field), accuracy5);
			List<String> similarWordsInVocabTo9 = vec.similarWordsInVocabTo(record.getValue(field), accuracy9);
			if(similarWordsInVocabTo != null && !similarWordsInVocabTo.isEmpty()) {
				LOG.info("Similar word(s) for '" + record.getValue(field) + "' with 70% accuracy are " + similarWordsInVocabTo);
				samples--;
			}
			if(similarWordsInVocabTo9 != null && !similarWordsInVocabTo9.isEmpty()) {
				LOG.info("Similar word(s) for '" + record.getValue(field) + "' with 90% accuracy are " + similarWordsInVocabTo9);
				samples--;
			}
			if(samples <= 0) {
				LOG.info("Press ENTER to continue..."); System.in.read();
				samples = 10;
			}
		}
//        
//        vec.lookupTable().plotVocab(tsne, 4, server.getConnectionInfo());
//        
		
		// Write word vectors
//        WordVectorSerializer.writeTsneFormat(vec, tsne.getData(), new File("FakeNameVectorsTsne.csv"));
        
        //output: [night, week, year, game, season, during, office, until, -]
		
	}

	private static BarnesHutTsne plotTSNE() {
		LOG.info("Plot TSNE....");
        return new BarnesHutTsne.Builder()
                .setMaxIter(1000)
                .stopLyingIteration(250)
                .learningRate(500)
                .useAdaGrad(false)
                .theta(0.5)
                .setMomentum(0.5)
                .normalize(true)
                //.usePca(false) - not available!
                .build();
	}

	private static void loadFakeNameBeans(List<FakeNameBean> fakeNameBeans, String classpathResource) throws IOException {
		LOG.info("Loading records from - " + classpathResource);
		try (InputStream is = Utils.classpathResourceToInputStream(classpathResource)) {
			LineReader lineReader = new LineReader(new BufferedReader(new InputStreamReader(is)));
			FakeNameRecordStructure structure = new FakeNameRecordStructure(lineReader.readLine()); // first line is header
			String line = null;
			while((line = lineReader.readLine()) != null) {
				fakeNameBeans.add(new FakeNameBean(structure, line));
			}
		}
	}
}

class Utils {

    private static Logger LOG = LoggerFactory.getLogger(Utils.class);

	public static InputStream classpathResourceToInputStream(String resourcePath) {
		try {
			return new ClassPathResource(resourcePath).getInputStream();
		} catch (IOException e) {
			LOG.error("Unable to obtain input stream from given resource path - {}", resourcePath, e);
			throw new IllegalArgumentException("Unable to obtain input stream from given resource path - " + resourcePath, e);
		}
	}

	public static void foreach(String record, CallbackFunction<String> callback) {
		String[] split = record.split(",");
		int index = 0;
		for(String field : split) {
			callback.call(index++, field);
		}
	}
	
}


enum FakeNameFieldsEnum {
	NUMBER,
	TITLE,
	NAME,
	MIDDLE_INITIAL,
	SURNAME,
	MOTHER_MAIDEN,
	GENDER,
	BIRTHDAY,
	AGE,
	HEIGHT,
	WEIGHT, 
	BLOODTYPE;
}

interface CallbackFunction<T> {
	
	void call(int index, T data);
}

class FakeNameRecordStructure {
	
	private List<FakeNameFieldsEnum> fieldIndex = new ArrayList<FakeNameFieldsEnum>();

	/**
	 * Header columns separated by comma => Number,Title,GivenName,MiddleInitial,Surname,MothersMaiden,Birthday,Gender,Age,BloodType,Kilograms,FeetInches
	 * 
	 * @param header
	 */
	public FakeNameRecordStructure(String header) {
		Utils.foreach(header, new CallbackFunction<String>() {

			public void call(int index, String field) {
    			if(field.trim().equalsIgnoreCase("Number")) {
    				fieldIndex.add(FakeNameFieldsEnum.NUMBER);
    			} else if(field.trim().equalsIgnoreCase("Title")) {
    				fieldIndex.add(FakeNameFieldsEnum.TITLE);
    			} else if(field.trim().equalsIgnoreCase("GivenName")) {
    				fieldIndex.add(FakeNameFieldsEnum.NAME);
    			} else if(field.trim().equalsIgnoreCase("MiddleInitial")) {
    				fieldIndex.add(FakeNameFieldsEnum.MIDDLE_INITIAL);
    			} else if(field.trim().equalsIgnoreCase("Surname")) {
    				fieldIndex.add(FakeNameFieldsEnum.SURNAME);
    			} else if(field.trim().equalsIgnoreCase("MothersMaiden")) {
    				fieldIndex.add(FakeNameFieldsEnum.MOTHER_MAIDEN);
    			} else if(field.trim().equalsIgnoreCase("Birthday")) {
    				fieldIndex.add(FakeNameFieldsEnum.BIRTHDAY);
    			} else if(field.trim().equalsIgnoreCase("Gender")) {
    				fieldIndex.add(FakeNameFieldsEnum.GENDER);
    			} else if(field.trim().equalsIgnoreCase("Age")) {
    				fieldIndex.add(FakeNameFieldsEnum.AGE);
    			} else if(field.trim().equalsIgnoreCase("BloodType")) {
    				fieldIndex.add(FakeNameFieldsEnum.BLOODTYPE);
    			} else if(field.trim().equalsIgnoreCase("Kilograms")) {
    				fieldIndex.add(FakeNameFieldsEnum.WEIGHT);
    			} else if(field.trim().equalsIgnoreCase("FeetInches")) {
    				fieldIndex.add(FakeNameFieldsEnum.HEIGHT);
    			} else {
    				throw new IllegalArgumentException("Header is invalid. Unknown field: " + field);
    			}
			}
		});
	}

	public FakeNameFieldsEnum atIndex(int index) {
		return fieldIndex.get(index);
	}
}

class FakeNameBean {
	
	private Map<FakeNameFieldsEnum, String> data = new HashMap<FakeNameFieldsEnum, String>(FakeNameFieldsEnum.values().length);
	
	/**
	 * @param structure
	 * @param record
	 */
	public FakeNameBean(final FakeNameRecordStructure structure, String record) {
		Utils.foreach(record, new CallbackFunction<String>() {

			public void call(int index, String data) {
				FakeNameBean.this.data.put(structure.atIndex(index), data);
			}
		});
	}
	
	public String getValue(FakeNameFieldsEnum field) {
		return data.get(field);
	}
}


class FakeNameBeanIterator implements Iterable<Sequence<VocabWord>>, Iterator<Sequence<VocabWord>> {
	
	private Iterator<FakeNameBean> iterator;
	private FakeNameFieldsEnum[] values = //FakeNameFieldsEnum.values();
		{FakeNameFieldsEnum.NAME, FakeNameFieldsEnum.SURNAME, FakeNameFieldsEnum.TITLE, FakeNameFieldsEnum.GENDER};
	private List<VocabWord> labels = new ArrayList<>();

	public FakeNameBeanIterator(List<FakeNameBean> records) {
		this.iterator = records.iterator();
		for(FakeNameFieldsEnum field : values) {
			VocabWord label = new VocabWord();
			label.setWord(field.name());
			labels.add(label);
		}
	}

	@Override
	public Iterator<Sequence<VocabWord>> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Sequence<VocabWord> next() {
		FakeNameBean next = iterator.next();
		Sequence<VocabWord> result = new Sequence<>();
		for(FakeNameFieldsEnum field : values) {
			VocabWord element = new VocabWord();
			element.setWord(next.getValue(field));
			result.addElement(element);
			result.setSequenceLabels(labels);
		}
		return result;
	}
	
}
