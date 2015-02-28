package org.mef.framework.metadata;
import org.mef.framework.metadata.validate.IValidator;
import org.mef.framework.metadata.validate.ValContext;


public abstract class Value
{
	protected Object obj;
	protected Converter converter;
	protected IValidator validator;

	public Value()
	{}
	public Value(Object obj)
	{
		this.obj = obj;
	}

	//??deep copy needed!!

	public Object getUnderlyingValue()
	{
		return obj;
	}
	public void setUnderlyingValue(Object obj)
	{
		this.obj = obj;
	}

	public void validate(ValContext valctx)
	{
		if (validator != null)
		{
			validator.validate(valctx, this);
		}
	}

	protected abstract void parse(String input);
	protected abstract String render();
	@Override
	public String toString()
	{
		if (converter != null)
		{
			return converter.print(obj);
		}
		else
		{
			return render();
		}
	}
	public void fromString(String input)
	{
		if (converter != null)
		{
			Object object = converter.parse(input);
			setUnderlyingValue(object);
		}
		else
		{
			parse(input);
		}
	}
	public Converter getConverter() {
		return converter;
	}
	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	public IValidator getValidator() {
		return validator;
	}
	public void setValidator(IValidator validator) {
		this.validator = validator;
	}

}