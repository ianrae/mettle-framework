package org.mef.framework.metadata;
import java.util.ArrayList;
import java.util.List;

import org.mef.framework.metadata.validate.ValContext;


public class ListValue implements ValueContainer
{
	private List<Value> list;
	
	public ListValue()
	{
		list = new ArrayList<Value>();
	}
	
	public ListValue(ListValue src)
	{
		list = new ArrayList<Value>();
		for(Value val : src.list)
		{
			Value copy = new Value(val);
			list.add(copy);
		}
	}
	
	public void addElement(Value val)
	{
		list.add(val);
	}

	public Value getIth(int index) 
	{
		Value val = list.get(index);
		return val;
	}

	public Object size() 
	{
		return list.size();
	}
	
	//validation
	public void validateContainer(ValContext vtx)
	{
		for(Value val : list)
		{
			vtx.validate(val);
		}
	}
}