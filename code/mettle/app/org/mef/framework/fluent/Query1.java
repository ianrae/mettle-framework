package org.mef.framework.fluent;
import java.util.List;



public class Query1<T>  extends QueryBase<T>
{
	public Query1(QueryContext<T> queryctx)
	{
		this.queryctx = queryctx;
	}

	//find 0 or 1
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


	//X2
	public Query2<T> where(String fieldName)
	{
		this.addx(QueryParser.WHERE, ' ', null, fieldName);
		return new Query2<T>(queryctx);
	}
	public Query1<T> orderBy(String fieldName)
	{
		return this.orderBy(fieldName, "asc");
	}
	public Query1<T> orderBy(String fieldName, String asc)
	{
		this.addx(QueryParser.ORDERBY, ' ', asc, fieldName);
		return this;
	}
	public Query1<T> fetch(String relationFieldName)
	{
		this.addx(QueryParser.FETCH, ' ', null, relationFieldName);
		return this;
	}
	public Query1<T> limit(int num)
	{
		this.addx(QueryParser.LIMIT, ' ', new Integer(num), null);
		return this;
	}
	public Query1<T> offset(int offset)
	{
		this.addx(QueryParser.OFFSET, ' ', new Integer(offset), null);
		return this;
	}
	//groupBy doesn't make sense for an entity db since you can't combine entity objects
}