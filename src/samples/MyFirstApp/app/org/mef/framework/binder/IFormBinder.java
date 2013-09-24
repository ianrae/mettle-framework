package org.mef.framework.binder;

import mef.entities.Task;

public interface IFormBinder
{
	boolean bind();

	Task getObject();
}