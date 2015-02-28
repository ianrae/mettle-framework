package org.mef.framework.metadata.validators;
import org.mef.framework.metadata.Value;
import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ErrorMessages;
import org.mef.framework.metadata.validate.ValContext;
import org.mef.framework.metadata.validate.ValidationErrors;


public class NotEmptyStringValidator implements IValidator
{
	@Override
	public void validate(ValContext valctx, Value obj) 
	{
		String s = obj.toString();
		if (s == null || s.isEmpty())
		{
			valctx.addError(ErrorMessages.NOTEMPTY);
		}
	}
}