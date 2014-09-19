package org.mef.framework.binder.input;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InputValidator implements Validator 
	{
		public IInput entity;

	    @Override
	    public boolean supports(Class<?> clazz) 
	    {
	    	Class<?>[] ar = clazz.getInterfaces();
	    	for(Class<?> izz : ar)
	    	{
	    		if (izz == IInput.class)
	    		{
	    			return true;
	    		}
	    	}
	    	return true;
	    }

	    @Override
	    public void validate(Object target, Errors errors) 
	    {
	    	entity.validateInput(errors);
	    }
	}	