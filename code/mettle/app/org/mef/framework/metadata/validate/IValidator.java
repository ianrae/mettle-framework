package org.mef.framework.metadata.validate;

import org.mef.framework.metadata.Value;


public interface IValidator
{
	void validate(ValContext valctx, Value value);
}