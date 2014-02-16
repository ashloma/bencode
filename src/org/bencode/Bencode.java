package org.bencode;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bencode.types.DictionaryType;
import org.bencode.types.IntegerValue;
import org.bencode.types.ListType;
import org.bencode.types.Parser;
import org.bencode.types.StringValue;
import org.bencode.types.Value;

/**
 * B-encode format serialize/deserialize util
 * 
 * @author a.shloma
 *
 */
public class Bencode {

	/**
	 * Add integer value to b-encode format
	 * 
	 * @param val
	 */
	public static void addIntegerValue(long val) {
		Parser.putValue(new IntegerValue(val));
	}

	/**
	 * Add integer value to list type
	 * 
	 * @param list
	 * @param val
	 * @throws OperationNotSupportedException
	 */
	public static void addIntegerValue(ListType list, long val)
			throws OperationNotSupportedException {
		list.addValue(new IntegerValue(val));
	}

	/**
	 * Add string value to b-encode format
	 * 
	 * @param val
	 * @throws OperationNotSupportedException
	 */
	public static void addStringValue(String val)
			throws OperationNotSupportedException {
		Parser.putValue(new StringValue(val));
	}

	/**
	 * Add string value to list type
	 * 
	 * @param list
	 * @param val
	 * @throws OperationNotSupportedException
	 */
	public static void addStringValue(ListType list, String val)
			throws OperationNotSupportedException {
		list.addValue(new StringValue(val));
	}

	/**
	 * Add list type to b-encode format
	 * 
	 * @param list
	 */
	public static void addListType(ListType list) {
		Parser.putValue(list);
	}

	/**
	 * Create inner list type
	 * 
	 * @param list
	 * @throws OperationNotSupportedException
	 */
	public static void addInnerListType(ListType outer, ListType inner)
			throws OperationNotSupportedException {
		outer.addValue(inner);
	}

	/**
	 * Create list type
	 * 
	 * @return ListType object
	 */
	public static ListType createListType() {
		return new ListType();
	}

	/**
	 * Create dictionary type
	 * 
	 * @return
	 */
	public static DictionaryType createDictionary() {
		return new DictionaryType();
	}

	/**
	 * Add pair key-value to dictionary Support only long, String and ListType
	 * 
	 * @param dictionary
	 * @param key
	 * @param val
	 * @throws OperationNotSupportedException
	 */
	public static void putValueToDictionary(DictionaryType dictionary,
			String key, Value<?> val) throws OperationNotSupportedException {

		dictionary.addKey(new StringValue(key));
		dictionary.addValue(val);

	}

	/**
	 * Add dictionary type to b-encode format
	 * 
	 * @param dictionary
	 */
	public static void addDictionaryType(DictionaryType dictionary) {
		Parser.putValue(dictionary);
	}

	/**
	 * Serialized b-encode format to byte array
	 * 
	 * @return byte array
	 */
	public static byte[] serialize() {
		if (Parser.getSize() == 0) {
			return null;
		}
		return Parser.serialize();
	}

	/**
	 * Deserialized b-encode format
	 * 
	 * @param data
	 * @throws OperationNotSupportedException
	 */
	public static void deserialized(byte[] data)
			throws OperationNotSupportedException {
		Parser.deserialize(data);
	}

	/**
	 * Show b-encode format expression
	 * 
	 * @return String of expression
	 */
	public static String showExpression() {
		if (Parser.getSize() == 0) {
			return "empty";
		} else {
			return Parser.getExpression();
		}
	}

	/**
	 * @return list of parsed values
	 */
	public static List<Value<?>> getValues() {
		return Parser.getValues();
	}

	/**
	 * Reset expression in b-encode format
	 */
	public static void resetExpression() {
		Parser.resetExpression();
	}

}
