package org.mef.framework.metadata.validators;
import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ValidationErrors;


public class NotEmptyStringValidator implements IValidator
{
	@Override
	public boolean validate(Object val, ValidationErrors errors) 
	{
		String s = (String)val;
		if (s.isEmpty())
		{
			errors.addError("empty string not allowed");
			return false;
		}
		return true;
	}
}