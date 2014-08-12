package org.mef.framework.fluent;

import java.util.List;

public interface IDAOObserver<T>
{
	void onQueryOne(T entity);
	void onQueryMany(List<T> list);
	void onSave(T entity);
	void onUpdate(T entity);
	void onDelete(long id);
}

