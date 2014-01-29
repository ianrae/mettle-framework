package org.mef.framework.fluent;
import java.util.List;



public class Query3<T> extends QueryBase<T>
{
	public Query3(QueryContext<T> queryctx)
	{
		this.queryctx = queryctx;
	}

	public T findAny()
	{
		return dofindAny();
	}
	//error if > 1
	public T findUnique()
	{
		return doFindUnique();
	}
	public List<T> findMany()
	{
		return doFindMany();
	}
	public long findCount()
	{
		return doFindCount();
	}
	public String dumpQuery()
	{
		return doDumpQuery();
	}

	public Query2<T> and(String fieldName)
	{
		this.addx('&', ' ', null, fieldName);
		return new Query2<T>(queryctx);
	}
	public Query2<T> or(String fieldName)
	{
		this.addx('|', ' ', null, fieldName);
		return new Query2<T>(queryctx);
	}
}