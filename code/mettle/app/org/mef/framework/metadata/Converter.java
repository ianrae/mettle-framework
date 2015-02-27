package org.mef.framework.metadata;


public interface Converter 
{
	String print(Object obj);
	Object parse(String s);
}
