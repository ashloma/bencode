package org.bencode.types;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import javax.naming.OperationNotSupportedException;
/**
 * List type - contains integer and string values
 * 
 * @author a.shloma
 *
 */

public class ListType extends Value<LinkedList<Value<?>>> implements ComplexType {

	private final LinkedList<Value<?>> listElements = new LinkedList<>();
	
	/**
	 * Add simple type value to list type
	 */
	public void addValue(Value<?> val) throws OperationNotSupportedException {
		if (val == null) {
			throw new OperationNotSupportedException("Trying to add null");
		}
		listElements.add(val);
	}
	
	/**
	 * Add complex type value to list type
	 */
	public void addComplexValue(ComplexType val) throws OperationNotSupportedException {
		if (val == null) {
			throw new OperationNotSupportedException("Trying to add null");
		}
		
		try {
			listElements.add((Value<?>)val);
		}
		catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected byte[] write() {

		ByteBuffer buf = ByteBuffer.allocate(getSize());
		
		buf.put((byte)'l');
		for (Value<?> val : listElements) {
			buf.put(val.write());
		}
		buf.put((byte)'e');
		
		return buf.array();
	}

	@Override
	protected String getExpressionValue() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("l");
		for (Value<?> val : listElements) {
			sb.append(val.getExpressionValue());
		}
		sb.append("e");
		
		return sb.toString();
	}

	@Override
	protected int getSize() {
		// count size for allocate
		int size = 0;
		for (Value<?> val : listElements) {
			size += val.getSize();
		}
		return size + 2;
	}

	@Override
	protected LinkedList<Value<?>> getValue() {
		return listElements;
	}

}
