package persistence;

import clog.JsonTests;
import clog.JsonTests.Thing;

public class ReferenceDesc
{
	public BaseParser parser;
	
	public String refName;  //name of file
	public Class refClass;
	public Thing target;

	public int refId;
}