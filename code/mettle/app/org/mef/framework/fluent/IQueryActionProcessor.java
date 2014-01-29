package org.mef.framework.fluent;
import java.util.List;


public interface IQueryActionProcessor<T>
{
	void start(List<QueryAction> actionL);
	T findOne(); //error if > 1
	T findAny();
	List<T> findMany();
	long findCount();
	void processAction(int index, QueryAction action);
}