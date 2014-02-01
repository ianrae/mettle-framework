package org.mef.framework.fluent;
import java.util.List;

import org.mef.framework.dao.IDAO;


public interface IQueryActionProcessor<T>
{
	void start(List<QueryAction> actionL);
	T findOne(); //error if > 1
	T findAny();
	List<T> findMany();
	long findCount();
	void processAction(int index, QueryAction action);
	
	Class getRelationalFieldType(QueryAction action);
	QueryAction processRelationalAction(int i, QueryAction action, IDAO dao);
}