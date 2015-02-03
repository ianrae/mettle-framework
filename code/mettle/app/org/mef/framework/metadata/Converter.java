package org.mef.framework.metadata;

import java.util.Locale;

public abstract class Converter 
{
	public Converter()
	{}
	
	public String printInt(int n, Locale l)
	{
		return null;
	}
	public String printLong(long n, Locale l)
	{
		return null;
	}
	public String printString(String s, Locale l)
	{
		return null;
	}
	public String printDouble(double d, Locale l)
	{
		return null;
	}
	public String printBoolean(boolean b, Locale l)
	{
		return null;
	}
	public String printObject(Object obj, Locale l)
	{
		return null;
	}

	//--- parse
	public int parseInt(String s, Locale l)
	{
		return 0;
	}
	public long parseLong(String s, Locale l)
	{
		return 0;
	}
	public String parseString(String s, Locale l)
	{
		return null;
	}
	public double parseDouble(String s, Locale l)
	{
		return 0.0;
	}
	public boolean parseBoolean(String s, Locale l)
	{
		return false;
	}
	public Object parseObject(String s, Locale l)
	{
		return null;
	}
	
}
