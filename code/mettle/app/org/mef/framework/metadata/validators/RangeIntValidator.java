package org.mef.framework.metadata.validators;

import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ErrorMessages;
import org.mef.framework.metadata.validate.ValidationErrors;


public class RangeIntValidator implements IValidator
{
	private int min;
	private int max;
	
	public RangeIntValidator(int min, int max)
	{
		this.min = min;
		this.max = max;
	}
	@Override
	public boolean validate(Object val, ValidationErrors errors) 
	{
		int n = (Integer)val;
		
		boolean ok = (n >= min && n <= max);
		if (! ok)
		{
//			errors.addError(String.format("value %d not in range %d to %d", n, min, max));
			errors.addError(ErrorMessages.RANGE_INT, n, min, max);
			return false;
		}
		return true;
	}
}