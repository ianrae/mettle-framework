package persistence;

import java.util.ArrayList;


public class ParserDesc
{
	public BaseParser parser;
	public ArrayList<Thing> thingL = new ArrayList<Thing>();
	
	public ParserDesc(BaseParser parser)
	{
		this.parser = parser;
	}
}