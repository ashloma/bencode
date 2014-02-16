package org.bencode.types;

import java.nio.ByteBuffer;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.naming.OperationNotSupportedException;

/**
 * Dictionary type - contains pair key-value
 * Key must be only string type
 * Value can be integer, string and list
 * 
 * @author a.shloma
 *
 */
public class DictionaryType extends Value<SortedMap<StringValue, Value<?>>> implements ComplexType {
	
	private final SortedMap<StringValue, Value<?>> map = new TreeMap<>();
	private StringValue currKey;

	protected  byte[] write() {
		ByteBuffer buf = ByteBuffer.allocate(getSize());
		
		buf.put((byte) 'd');
		
		for (StringValue key : map.keySet()) {
			buf.put(key.write());
			buf.put(map.get(key).write());
		}
		
		buf.put((byte) 'e');
		
		return buf.array();
	}
	
	public void addComplexValue(ComplexType val) throws OperationNotSupportedException {
		if (val == null) {
			throw new OperationNotSupportedException("Trying to add null");
		}
		try {
			if (currKey != null) {
				map.put(currKey, (Value<?>)val);
			}
		}
		catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	protected int getSize() {
		int size = 0;
		// count size for each pair key-value
		for (StringValue key: map.keySet()) {
			size += key.getSize() + map.get(key).getSize();
		}
		
		return size + 2;
	}

	protected String getExpressionValue() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("d");
		for (StringValue key: map.keySet()) {
			sb.append(key.getExpressionValue());
			sb.append(map.get(key).getExpressionValue());
		}
		sb.append("e");
		
		
		return sb.toString();
	}
	
	public void addKey(StringValue key) {
		currKey = key;
	}
	
	public void addValue(Value<?> val) throws OperationNotSupportedException {
		
		if (val == null) {
			throw new OperationNotSupportedException("Trying to add null");
		}
		
		if (currKey == null) {
			currKey = (StringValue) val;
		}
		else {
			map.put(currKey, val);
			currKey = null;
		}


	}

	protected SortedMap<StringValue, Value<?>> getValue() {
		return map;
	}

}
