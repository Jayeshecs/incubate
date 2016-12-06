package beans.fakename;

import java.util.HashMap;
import java.util.Map;

import functions.CallbackFunction;
import utils.Utils;

public class FakeNameBean {
	
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
		data.put(FakeNameFieldsEnum.FULLNAME, getValue(FakeNameFieldsEnum.NAME) + getValue(FakeNameFieldsEnum.SURNAME)); 
	}
	
	public String getValue(FakeNameFieldsEnum field) {
		return data.get(field);
	}
}