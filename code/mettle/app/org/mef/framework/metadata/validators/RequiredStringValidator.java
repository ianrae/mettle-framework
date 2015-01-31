package org.mef.framework.metadata.validators;
import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ErrorMessages;
import org.mef.framework.metadata.validate.ValidationErrors;


public class RequiredStringValidator implements IValidator
{
	@Override
	public boolean validate(Object val, ValidationErrors errors) 
	{
		String s = (String)val;
		if (s == null)
		{
			errors.addError(ErrorMessages.REQUIRED);
			return false;
		}
		return true;
	}
}