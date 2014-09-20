
package org.mef.framework.binder;

import java.util.ArrayList;
import java.util.List;

import org.mef.framework.binder.input.IInput;
import org.mef.framework.binder.input.InputValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;

import play.data.Form;
import play.data.validation.ValidationError;

public class BinderHelper 
{
	public BinderHelper()
	{
	}
	
	public static void validateInput(Object object, Form<?> filledForm, int inputType)
	{
		BinderHelper helper = new BinderHelper();
		helper.validate(object, filledForm, inputType);
	}
	
	public void validate(Object entity, Form<?> filledForm, int inputType) 
	{
		//and do mef validation
		if (entity != null && entity instanceof IInput)
		{
			List<ValidationError> verrors = new ArrayList<ValidationError>();
			IInput input = (IInput)entity;
			Errors errors = doValidation(input, inputType);
			if (errors.getAllErrors().size() > 0)
			{
				for(ObjectError err : errors.getGlobalErrors())
				{
					String msg = err.getCode();
					if (isNullOrEmpty(msg))
					{
						msg = err.getDefaultMessage();
					}
					filledForm.reject(msg);
				}

				for(FieldError err : errors.getFieldErrors())
				{
					String msg = err.getCode();
					if (isNullOrEmpty(msg))
					{
						msg = err.getDefaultMessage();
					}
					filledForm.reject(err.getField(), msg);
				}					
			}
		}
	}
		
	
	private boolean isNullOrEmpty(String msg) 
	{
		if (msg == null)
		{
			return true;
		}
		return msg.isEmpty();
	}
	private Errors doValidation(IInput input, int inputType)
	{
		Errors errors = new BeanPropertyBindingResult(input, input.getClass().getSimpleName());
		InputValidator pval = new InputValidator(inputType);
		pval.entity = input;
		ValidationUtils.invokeValidator(pval, input, errors);
		return errors;
	}

}
