package org.bencode.types;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;


/**
 * Serialize and deserialize B-encode format.
 * Contains list of values.
 * 
 * @author a.shloma
 *
 */
public class Parser {
	
	private static List<Value<?>> values = new LinkedList<>();
	
	/**
	 * Deserialize byte array into B-encode format
	 * @param data - byte array
	 * @throws OperationNotSupportedException 
	 */
	public static void deserialize(byte[] data) throws OperationNotSupportedException {
		
		values = new LinkedList<>();
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		int start = 0;
		
		boolean isInnerType = false;
		boolean isInteger = false;
		
		Map<Integer, ComplexType> typeMap = null;

		ComplexType currType = null;
		
		int deepSize = 0;
		
		while (buf.hasRemaining()) {
			byte _byte = buf.get();
			if (_byte > 0) {
				switch(_byte) {
				case 'i': {
					// parsing integer value
					byte[] arr = new byte[8];
					buf.get(arr, 0, 8);
										
					isInteger = true;
					// add integer value to complex type
					if (isInnerType) {
						addInnerType(currType, new IntegerValue(arr));
					}
					
					// add simple integer value
					else {
						values.add(new IntegerValue(arr));
					}
					start = buf.position();
					break;
				}
				case 'l': {
					// parsing list type
					if (deepSize == 0) {
						typeMap = new HashMap<>();
						currType = new ListType();
						isInnerType = true;
					}
					else {
						typeMap.put(deepSize, currType);
						currType = new ListType();
					}
					
					deepSize++;
					start = buf.position();
					break;
				}
				case 'd': {
					// parsing dictionary type
					
					if (deepSize == 0) {
						typeMap = new HashMap<>();
						currType = new DictionaryType();
						isInnerType = true;
					}
					else {
						typeMap.put(deepSize, currType);
						currType = new DictionaryType();
					}
					deepSize++;
					
					start = buf.position();
					break;
				}
				case 'e': {
					
					// close last level of complex type
					if (isInteger) {
						isInteger = false;
					}
					else if (isInnerType) {
						
						if (deepSize > 1) {
							ComplexType temp = typeMap.get(deepSize - 1);
							temp.addComplexValue(currType);
							currType = temp;
							
						}
						else {
							// add complex type 
							values.add((Value<?>) currType);
							currType = null;
							isInnerType = false;
						}
						deepSize--;	
						
					}
					
					start = buf.position();
					break;
				}
				default: {
					// parsing string value
					int length = (int) buf.getLong(start);
					// skip separator
					buf.get();
					
					byte[] arr = new byte[length * 2];
					buf.get(arr, 0, arr.length);
					// add value to list
					if (isInnerType) {
						currType.addValue(new StringValue(arr));
					}
					// add simple value
					else {
						values.add(new StringValue(arr));
					}
					start = buf.position();
					break;
				}	
				}
			}
		}
	}
	
	/**
	 * Serializing B-encode format into byte array
	 * @return byte array
	 */
	public static byte[] serialize() {
		// count size for array allocation
		int size = 0;
		
		for (Value<?> val : values) {
			size += val.getSize();
		}
		
		// allocate size of buffer
		ByteBuffer buf = ByteBuffer.allocate(size);
		
		for (Value<?> val : values) {
			buf.put(val.write());
		}
		
		
		return buf.array();
	}
	
	/**
	 * Put value to expression
	 * @param val
	 */
	public static void putValue(Value<?> val) {
		values.add(val);
	}
	
	/**
	 * @return expression in B-encode format
	 */
	public static String getExpression() {
		StringBuffer sb = new StringBuffer();
		
		for (Value<?> val : values) {
			sb.append(val.getExpressionValue());
		}
		
		return sb.toString();
	}
	
	public static int getSize() {
		return values.size();
	}
	
	public static List<Value<?>> getValues() {
		return values;
	}
	
	public static void resetExpression() {
		values = new LinkedList<>();
	}
	
	/**
	 * Create inner type
	 * @param outer outer complex type 
	 * @param inner inner complex or simple type
	 * @throws OperationNotSupportedException
	 */
	private static void addInnerType(ComplexType outer, Value<?> inner) throws OperationNotSupportedException {
		outer.addValue(inner);
	}

}
