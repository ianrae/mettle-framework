package org.mef.framework.metadata.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.framework.metadata.Value;



public class ValContext
{
	private int failCount;
    private Map<String,List<ValidationErrorSpec>> mapErrors;
	
	public ValContext()
	{
		mapErrors = new HashMap<String,List<ValidationErrorSpec>>();
	}
	
	public void validate(Value val)
	{
		ValidationErrors errors = new ValidationErrors();
		errors.map = mapErrors;
//		errors.itemName = val.getItemName();
//		if (! val.validate(errors))
//		{
//			failCount++;
//		}
	}
	
	public void addError(String fmt, Object...strings)
	{}
	
	public int getFailCount()
	{
		return failCount;
	}
	
    public Map<String,List<ValidationErrorSpec>> getErrors()
    {
    	return mapErrors;
    }
    
    public List<ValidationErrorSpec> getFlattendErrorList()
    {
    	List<ValidationErrorSpec> resultL = new ArrayList<ValidationErrorSpec>();
    	
		for(String key : mapErrors.keySet())
		{
			List<ValidationErrorSpec> L = mapErrors.get(key);
			for(ValidationErrorSpec spec : L)
			{
				resultL.add(spec);
			}
		}
		return resultL;
    }
}