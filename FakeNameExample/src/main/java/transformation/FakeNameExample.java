/**
 * 
 */
package transformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.sequencevectors.iterators.AbstractSequenceIterator;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.LineReader;

import beans.fakename.FakeNameBean;
import beans.fakename.FakeNameFieldsEnum;
import beans.fakename.FakeNameRecordStructure;
import iterators.fakename.FakeNameBeanIterator;
import utils.Utils;

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
		
		FakeNameFieldsEnum field = FakeNameFieldsEnum.FULLNAME;
		double accuracy5 = 0.9;
		double accuracy9 = 0.99;
		
		int samples = 20;
		for(FakeNameBean record : testRecords) {
			List<String> similarWordsInVocabTo = vec.similarWordsInVocabTo(record.getValue(field), accuracy5);
			List<String> similarWordsInVocabTo9 = vec.similarWordsInVocabTo(record.getValue(field), accuracy9);
			if(similarWordsInVocabTo != null && !similarWordsInVocabTo.isEmpty()) {
				LOG.info("Similar word(s) for '" + record.getValue(field) + "' with "+ (int)(accuracy5*100) +"% accuracy are " + similarWordsInVocabTo);
				samples--;
			}
			if(similarWordsInVocabTo9 != null && !similarWordsInVocabTo9.isEmpty()) {
				LOG.info("Similar word(s) for '" + record.getValue(field) + "' with " + (int)(accuracy9*100) +"% accuracy are " + similarWordsInVocabTo9);
				samples--;
			}
			if(samples <= 0) {
				LOG.info("Press ENTER to continue..."); System.in.read();
				samples = 10;
			}
		}
		
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
