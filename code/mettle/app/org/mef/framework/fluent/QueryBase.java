package org.mef.framework.fluent;
import java.util.List;


public class QueryBase<T>
{
	//protected List<QueryX> queryL;
	protected QueryContext<T> queryctx;

	protected void addx(char action, char type, Object obj)
	{
		addx(action, type, obj, null);
	}
	protected void addx(char action, char type, Object obj, String fieldName)
	{
		QStep x = new QStep();
		x.action = action;
		x.type = type;
		x.obj = obj;
		x.fieldName = fieldName;
		queryctx.queryL.add(x);
	}


	protected T dofindAny()
	{
		QueryParser<T> runner = new QueryParser<T>(queryctx);
		T result = runner.executeAny();
		return result;
	}
	//		public Car findAnyIfExists()
	//		{
	//			return null;
	//		}
	protected List<T> doFindMany()
	{
		QueryParser<T> runner = new QueryParser<T>(this.queryctx);
		List<T> resultL = runner.executeMany();
		return resultL;
	}
	protected T doFindUnique()
	{
		QueryParser<T> runner = new QueryParser<T>(this.queryctx);
		List<T> resultL = runner.executeMany(); //!!more efficient latet (use limit 2)
		if (resultL.size() > 1)
		{
			throw new FluentException("FindUnique found > 1 result");
		}
		
		if (resultL.size() == 0)
		{
			return null;
		}
		return resultL.get(0);
	}
	protected long doFindCount()
	{
		QueryParser<T> runner = new QueryParser<T>(this.queryctx);
		long count = runner.executeCount();
		return count;
	}
	
	protected String doDumpQuery()
	{
		String s = "";
		for(QStep x : queryctx.queryL)
		{
			if (x.fieldName == null)
			{
				s += String.format(" [%c %c]", x.action, x.type);
			}
			else
			{
				s += String.format(" [%s %c %c]", x.fieldName, x.action, x.type);
			}
		}
		return s;
	}
}