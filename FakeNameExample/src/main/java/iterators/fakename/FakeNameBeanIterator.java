package iterators.fakename;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deeplearning4j.models.sequencevectors.sequence.Sequence;
import org.deeplearning4j.models.word2vec.VocabWord;

import beans.fakename.FakeNameBean;
import beans.fakename.FakeNameFieldsEnum;

public class FakeNameBeanIterator implements Iterable<Sequence<VocabWord>>, Iterator<Sequence<VocabWord>> {
	
	private Iterator<FakeNameBean> iterator;
	private FakeNameFieldsEnum[] values = //FakeNameFieldsEnum.values();
		{/*FakeNameFieldsEnum.NAME, FakeNameFieldsEnum.SURNAME, */FakeNameFieldsEnum.TITLE, FakeNameFieldsEnum.GENDER, FakeNameFieldsEnum.FULLNAME};
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
			String value = next.getValue(field);
			VocabWord element = new VocabWord();
			element.setWord(value);
			result.addElement(element);
			result.setSequenceLabels(labels);
		}
		return result;
	}
	
}