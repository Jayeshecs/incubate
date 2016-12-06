package beans.fakename;

import java.util.ArrayList;
import java.util.List;

import functions.CallbackFunction;
import utils.Utils;

public class FakeNameRecordStructure {
	
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
		fieldIndex.add(FakeNameFieldsEnum.FULLNAME);
	}

	public FakeNameFieldsEnum atIndex(int index) {
		return fieldIndex.get(index);
	}
}