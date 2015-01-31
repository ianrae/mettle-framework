package org.mef.framework.metadata.validate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import play.i18n.Messages;


public class ValidationErrors
{
    public Map<String,List<ValidationErrorSpec>> map;
    String itemName;
    
    public String getItemName()
    {
    	return itemName;
    }
    
    public void addError(String message)
    {
    	ValidationErrorSpec spec = new ValidationErrorSpec();
    	spec.key = itemName;
    	spec.message = getMessageFromConf(message);
    	
    	List<ValidationErrorSpec> L = map.get(itemName);
    	if (L == null)
    	{
    		L = new ArrayList<ValidationErrorSpec>();
    	}
    	L.add(spec);
    	map.put(itemName, L);
    }
    
	private String getMessageFromConf(String message, Object... arguments) 
	{
		String s = Messages.get(message, arguments);
		return s;
	}
    
    

}