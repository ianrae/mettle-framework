package org.mef.framework.metadata.validate;


public interface IValidator
{
	boolean validate(ValContext valctx, Object obj);
}