package org.bencode;

import javax.naming.OperationNotSupportedException;

import org.bencode.types.DictionaryType;
import org.bencode.types.IntegerValue;
import org.bencode.types.ListType;

public class Runner {
	
	public static void main(String[] args) throws OperationNotSupportedException {
		
		// inner dictionaries
		DictionaryType dict0 = Bencode.createDictionary();
		Bencode.putValueToDictionary(dict0, "map-key", new IntegerValue(77));
		DictionaryType dict = Bencode.createDictionary();
		Bencode.putValueToDictionary(dict, "test-value", dict0);

		Bencode.addDictionaryType(dict);

		byte[] data = Bencode.serialize();
		Bencode.deserialized(data);
		System.out.println(Bencode.showExpression());
		// ---------------------------------------------------------------------
		// inner lists
		Bencode.resetExpression();
		
		ListType list0 = Bencode.createListType();
		Bencode.addIntegerValue(list0, -128);
		
		
		ListType list1 = Bencode.createListType();
		Bencode.addStringValue(list1,"list-first-inner");
		
		ListType list2 = Bencode.createListType();
		Bencode.addStringValue(list2,"list-second-inner");
		
		Bencode.addInnerListType(list1, list2);
		
		Bencode.addInnerListType(list0, list1);
		
		Bencode.addListType(list0);
		
		byte[] data1 = Bencode.serialize();
		Bencode.deserialized(data1);
		System.out.println(Bencode.showExpression());
		
	}

}
