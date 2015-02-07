package org.mef.framework.metadata;

import java.util.Locale;

public interface Converter 
{
	String print(Object obj, Locale l);
	Object parse(String s, Locale l);
}
