package org.mef.framework.metadata;

public interface ValueHandler<T> extends Converter
{
	Object toObj(T value);
	T fromObj(Object obj);
	
	Object copy(Object value);
	Object fromString(String sVal);	
}