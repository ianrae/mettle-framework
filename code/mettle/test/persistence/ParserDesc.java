package persistence;

import java.util.ArrayList;

import clog.JsonTests;
import clog.JsonTests.Thing;

public class ParserDesc
{
	public BaseParser parser;
	public ArrayList<Thing> thingL = new ArrayList<JsonTests.Thing>();
	
	public ParserDesc(BaseParser parser)
	{
		this.parser = parser;
	}
}