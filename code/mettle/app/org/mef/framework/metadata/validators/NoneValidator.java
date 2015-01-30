package org.mef.framework.metadata.validators;
import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ValidationErrors;


public class NoneValidator implements IValidator
{
	@Override
	public boolean validate(Object val, ValidationErrors errors) 
	{
		return true;
	}
}