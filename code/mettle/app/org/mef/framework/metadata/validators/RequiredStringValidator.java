package org.mef.framework.metadata.validators;
import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ErrorMessages;
import org.mef.framework.metadata.validate.ValContext;
import org.mef.framework.metadata.validate.ValidationErrors;


public class RequiredStringValidator implements IValidator
{
	@Override
	public boolean validate(ValContext valctx, Object obj) 
	{
		String s = (String)obj;
		if (s == null)
		{
			valctx.addError(ErrorMessages.REQUIRED);
			return false;
		}
		return true;
	}
}