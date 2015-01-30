package org.mef.framework.metadata.validate;

public interface IValidator
{
	boolean validate(Object val, ValidationErrors errors);
}