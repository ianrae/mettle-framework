package org.mef.tools.mgen.parser;

import java.util.ArrayList;
import java.util.List;

public class FieldDef
{
	public String name;
	public String typeName;
	public List<String> annotationL = new ArrayList<String>();
	public boolean isSeedField;  //true means we search for records matching this field during db:seed 
	
	public boolean getStringType()
	{
		return (typeName.equals("String"));
	}
	public boolean getBooleanType()
	{
		return (typeName.equals("boolean"));
	}
	public boolean getIntType()
	{
		return (typeName.equals("int"));
	}
	public boolean getDateType()
	{
		return (typeName.equals("Date"));
	}
}