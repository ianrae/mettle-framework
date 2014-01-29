package org.mef.framework.fluent;
import java.util.List;



public class QueryContext<T>
{
	public List<QStep> queryL;
	public IQueryActionProcessor<T> proc;
}