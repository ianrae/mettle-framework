package org.mef.framework.metadata;

import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ValidationErrors;

public abstract class StringValueAndValidator extends StringValue implements IValidator
{
	public StringValueAndValidator()
	{
		this(null, ""); //oops!!
	}
	public StringValueAndValidator(String val, String itemName)
	{
		super(val);
		this.setValidator(itemName, this);
	}
	
	@Override
	public abstract boolean validate(Object val, ValidationErrors errors);
}