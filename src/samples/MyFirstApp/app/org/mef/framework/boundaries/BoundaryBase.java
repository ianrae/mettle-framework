package org.mef.framework.boundaries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mef.entities.Task;
import mef.presenters.HomePagePresenter;
import models.TaskModel;

import org.mef.framework.binder.IFormBinder;
import org.mef.framework.commands.Command;
import org.mef.framework.entities.Entity;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import boundaries.binders.HomeFormBinder;

import controllers.routes;

import play.api.mvc.Call;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Result;

public abstract class BoundaryBase extends SfxBaseObj
{
//	public Result result;
	protected Command _cmd;

	public BoundaryBase(SfxContext ctx)
	{
		super(ctx);
	}
	
	//covariant return
	public abstract Reply process(Command cmd);
	
	protected void begin(Command cmd)
	{
		_cmd = cmd;
		//!!add http cgi params as cmd.setParameters
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		cmd.setParameters(dynamicForm.data());
	}

	public Map<String, List<ValidationError>> getValidationErrors()
	{
		IFormBinder binder = _cmd.getFormBinder();
		if (binder == null)
		{
			return new HashMap<String, List<ValidationError>>(); //empty
		}

		Map<String, List<ValidationError>> map = (Map<String, List<ValidationError>>) binder.getValidationErrors();
		return map;
	}

}
