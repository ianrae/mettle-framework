package org.mef.framework.metadata;

public interface ValueHandler<T>
{
	Object toObj(T value);
	T fromObj(Object obj);
}