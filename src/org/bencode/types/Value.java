package org.bencode.types;

/**
 * 
 * @author a.shloma
 * 
 * Superclass for types, contains method of marshalling
 * of each type, method that return String value of 
 * expression and method that return size of byte array 
 *
 * @param <T> type of value (for integer and string)
 */
public abstract class Value<T> {
	
	protected abstract byte[] write();
	protected abstract int getSize();
	protected abstract String getExpressionValue();
	protected abstract T getValue();

}
