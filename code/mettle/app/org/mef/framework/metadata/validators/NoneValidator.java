package org.mef.framework.metadata.validators;
import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ValContext;
import org.mef.framework.metadata.validate.ValidationErrors;


public class NoneValidator implements IValidator
{

	@Override
	public boolean validate(ValContext valctx, Object obj) {
		return true;
	}
}