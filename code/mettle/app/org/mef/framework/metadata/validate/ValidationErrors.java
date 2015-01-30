package org.mef.framework.metadata.validate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    	spec.message = message;
    	
    	List<ValidationErrorSpec> L = map.get(itemName);
    	if (L == null)
    	{
    		L = new ArrayList<ValidationErrorSpec>();
    	}
    	L.add(spec);
    	map.put(itemName, L);
    }
}