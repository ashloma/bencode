package org.bencode.types;

import javax.naming.OperationNotSupportedException;

/**
 * Interface describe behaviour of complex type object
 * 
 * @author a.shloma
 *
 */
public interface ComplexType {
	
	public void addValue(Value<?> val) throws OperationNotSupportedException;
	public void addComplexValue(ComplexType val) throws OperationNotSupportedException;

}
