package org.bencode.types;

import java.nio.ByteBuffer;

import javax.naming.OperationNotSupportedException;

/**
 * 
 * @author a.shloma
 * 
 * Contains string value. Parsing each literal as char value (2 bytes)
 * 
 */
public class StringValue extends Value<String> implements Comparable<StringValue> {

	private final String value;

	public StringValue(byte[] data) {
		value = read(data);
	}

	public StringValue(String val) throws OperationNotSupportedException {
		if (val == null) {
			throw new OperationNotSupportedException("Trying to add null");
		}
		value = val;
	}

	protected String read(byte[] data) {
		
		char[] ch = new char[data.length / 2];
		int j = 0;
		for (int i = 0; i < ch.length; i++) {
			ch[i] = (char) ((data[j++] << 8) + data[j++]);
		}
		
		return String.valueOf(ch);
	}

	@Override
	protected byte[] write() {

		char[] ch = value.toCharArray();
		byte[] data = new byte[ch.length * 2];

		int j = 0;
		for (int i = 0; i < ch.length; i++) {
			data[j++] = (byte) (ch[i] >> 8);
			data[j++] = (byte) ch[i];
		}
		
		ByteBuffer bb = ByteBuffer.allocate(data.length + 9);
		bb.putLong(ch.length);
		bb.put((byte)':');
		bb.put(data);

		return bb.array();
	}

	@Override
	protected String getExpressionValue() {
		StringBuilder sb = new StringBuilder();
		sb.append(value.length());
		sb.append(":");
		sb.append(value);
		
		return sb.toString();
	}
	
	@Override
	protected int getSize() {
		return (int)(value.length() * 2 + 9);
	}

	@Override
	public int compareTo(StringValue o) {
		return value.compareTo(o.getValue());		
	}
	
	@Override
	protected String getValue() {
		return value;		
	}


}
