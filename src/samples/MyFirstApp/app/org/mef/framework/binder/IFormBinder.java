package org.mef.framework.binder;

import java.util.List;
import java.util.Map;

import mef.entities.Task;

public interface IFormBinder
{
	boolean bind();

	Object getObject();

	Object getValidationErrors();
}