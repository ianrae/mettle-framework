package org.mef.framework.binder.input;

import org.springframework.validation.Errors;

public interface IInput 
{
	void validateInput(Errors e, int inputType);
}

