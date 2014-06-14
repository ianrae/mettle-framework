package org.mef.framework.binder;

import java.util.List;
import java.util.Map;

import play.db.ebean.Model;


//public interface IFormBinder
public interface IFormBinder<M,E>
{
	boolean bind();

	//	Object getObject();
	//	Object getRawObject();
	Object getValidationErrors();
	//}

	M getInputModel(); //return even if bind failed. may be partially filled

	E getEntity(); //entity or thing
	E convert(M model);
}