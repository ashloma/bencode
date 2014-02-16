package org.bencode.types;

import java.nio.ByteBuffer;


/**
 * 
 * @author a.shloma
 * 
 * Contains integer value. Store in long type (8 bytes)
 *
 */

public class IntegerValue extends Value<Long> {
	
	private final long value;
	
	public IntegerValue(byte[] data) {
		value = read(data);
	}
	
	public IntegerValue(long val) {
		value = val;
	}

	public Long read(byte[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.put(data);
	    buffer.flip();
	    return buffer.getLong();
	}

	@Override
	protected byte[] write() {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.put((byte) 'i');
		buffer.putLong(value);
		buffer.put((byte) 'e');
		return buffer.array();
	}
	
	@Override
	protected String getExpressionValue() {
		StringBuilder sb = new StringBuilder();
		sb.append("i");
		sb.append(value);
		sb.append("e");
		
		return sb.toString();
	}
	
	@Override
	protected int getSize() {
		return 10;
	}

	@Override
	protected Long getValue() {
		// TODO Auto-generated method stub
		return value;
	}

}
