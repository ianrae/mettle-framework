package org.mef.framework.sfx;

import java.util.ArrayList;

public class SfxTextBuilder {

	private ArrayList<String> _lineL;
	public static String newline = System.getProperty("line.separator");
	
	public SfxTextBuilder()
	{
		_lineL = new ArrayList<String>();
	}
	
	public void append(String format, String... args)
	{
		//6Dec2010. This is bizarre. This code used to work, but now String.format was not recogizing
		//the args or throwing a missing args exception.
		//This hack appears to fix things.  Figure this out properly later!!
		Object[] ar = new String[args.length];
		for(int i = 0; i < ar.length; i++)
		{
			ar[i] = args[i];
		}
		                         
//		String s = String.format(format, (Object)args);
		String s = String.format(format, ar);
		_lineL.add(s);
	}
	public void appendString(String s)
	{
		_lineL.add(s);
	}
	
	public String getText()
	{
		StringBuilder sb = new StringBuilder();
		for(String line : _lineL)
		{
			sb.append(line);
			sb.append(newline);
		}
		
		return sb.toString();
	}
	public ArrayList<String> getAllLines()
	{
		return _lineL;
	}

	public void append(SfxTextBuilder builder)
	{
		appendString(builder.getText());
	}
}
