package org.mef.framework.metadata;
import org.mef.framework.metadata.validate.ValContext;


public interface ValueContainer
{
	void validate(ValContext vtx);
	void copyTo(Object model);
	void copyFrom(Object model);
}