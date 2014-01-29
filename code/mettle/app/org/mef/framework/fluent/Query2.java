package org.mef.framework.fluent;
import java.util.Date;


public class Query2<T> extends QueryBase<T>
{
	public Query2(QueryContext<T> queryctx)
	{
		this.queryctx = queryctx;
	}


	//eq
	public Query3<T> eq(String val)
	{
		this.addx(QueryParser.EQ, 's', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> eq(Date dt)
	{
		this.addx(QueryParser.EQ, 'd', dt);
		return new Query3<T>(queryctx);
	}
	public Query3<T> eq(Double val)
	{
		this.addx(QueryParser.EQ, 'f', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> eq(long val)
	{
		this.addx(QueryParser.EQ, 'l', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> eq(int val)
	{
		this.addx(QueryParser.EQ, 'i', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> eq(boolean b)
	{
		this.addx(QueryParser.EQ, 'b', b);
		return new Query3<T>(queryctx);
	}
	
	//lt
	public Query3<T> lt(String val)
	{
		this.addx(QueryParser.LT, 's', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> lt(Date dt)
	{
		this.addx(QueryParser.LT, 'd', dt);
		return new Query3<T>(queryctx);
	}
	public Query3<T> lt(Double val)
	{
		this.addx(QueryParser.LT, 'f', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> lt(long val)
	{
		this.addx(QueryParser.LT, 'l', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> lt(int val)
	{
		this.addx(QueryParser.LT, 'i', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> lt(boolean b)
	{
		this.addx(QueryParser.LT, 'b', b);
		return new Query3<T>(queryctx);
	}
	
	public Query3<T> like(String val)
	{
		this.addx(QueryParser.LIKE, 's', val);
		return new Query3<T>(queryctx);
	}

	//neq
	//lt,gt
	//le,ge
	//like
	//isNull,isNotNull
}