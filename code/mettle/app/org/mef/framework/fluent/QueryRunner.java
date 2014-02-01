package org.mef.framework.fluent;
import java.util.List;


public class QueryRunner<T>
{
	private List<QueryAction> actionL;
	private QueryContext<T> queryctx;
	
	public QueryRunner(List<QueryAction> actionL, QueryContext<T> queryctx)
	{
		this.actionL = actionL;
		this.queryctx = queryctx;
	}

	public T executeAny() 
	{
		runProcessor();
		T result = queryctx.proc.findAny();
		return result;
	}
	
	private void runProcessor() 
	{
		queryctx.proc.start(actionL);
		
		int i = 0;
		for(QueryAction action : actionL)
		{
			String s = String.format("%s: %s %s", action.action, action.fieldName, action.op);
			System.out.println(s);
			if (isRelationalAction(action))
			{
				action = queryctx.proc.processRelationalAction(i, action);
			}
			
			if (action != null)
			{
				queryctx.proc.processAction(i, action);
			}
			i++;
		}
	}


	private boolean isRelationalAction(QueryAction qaction) 
	{
		if (qaction.action.equals("WHERE") && qaction.fieldName.contains("."))
		{
			return true;
		}
		return false;
	}

	public List<T> executeMany() 
	{
		runProcessor();
		List<T> resultL = queryctx.proc.findMany();
		return resultL;
	}
	public long executeCount() 
	{
		runProcessor();
		long count = queryctx.proc.findCount();
		return count;
	}
}