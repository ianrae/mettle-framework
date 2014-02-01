package org.mef.framework.fluent;
import java.util.Date;

import org.mef.framework.entities.Entity;


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
	public Query3<T> eq(Entity entity)
	{
		this.addx(QueryParser.EQ, 'e', entity);
		return new Query3<T>(queryctx);
	}
	
	//neq
	public Query3<T> neq(String val)
	{
		this.addx(QueryParser.NEQ, 's', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> neq(Date dt)
	{
		this.addx(QueryParser.NEQ, 'd', dt);
		return new Query3<T>(queryctx);
	}
	public Query3<T> neq(Double val)
	{
		this.addx(QueryParser.NEQ, 'f', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> neq(long val)
	{
		this.addx(QueryParser.NEQ, 'l', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> neq(int val)
	{
		this.addx(QueryParser.NEQ, 'i', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> neq(boolean b)
	{
		this.addx(QueryParser.NEQ, 'b', b);
		return new Query3<T>(queryctx);
	}
	public Query3<T> neq(Entity entity)
	{
		this.addx(QueryParser.NEQ, 'e', entity);
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
	

	//le
	public Query3<T> le(String val)
	{
		this.addx(QueryParser.LE, 's', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> le(Date dt)
	{
		this.addx(QueryParser.LE, 'd', dt);
		return new Query3<T>(queryctx);
	}
	public Query3<T> le(Double val)
	{
		this.addx(QueryParser.LE, 'f', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> le(long val)
	{
		this.addx(QueryParser.LE, 'l', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> le(int val)
	{
		this.addx(QueryParser.LE, 'i', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> le(boolean b)
	{
		this.addx(QueryParser.LE, 'b', b);
		return new Query3<T>(queryctx);
	}
	
	
	
	//gt
	public Query3<T> gt(String val)
	{
		this.addx(QueryParser.GT, 's', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> gt(Date dt)
	{
		this.addx(QueryParser.GT, 'd', dt);
		return new Query3<T>(queryctx);
	}
	public Query3<T> gt(Double val)
	{
		this.addx(QueryParser.GT, 'f', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> gt(long val)
	{
		this.addx(QueryParser.GT, 'l', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> gt(int val)
	{
		this.addx(QueryParser.GT, 'i', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> gt(boolean b)
	{
		this.addx(QueryParser.GT, 'b', b);
		return new Query3<T>(queryctx);
	}
	
	
	//ge
	public Query3<T> ge(String val)
	{
		this.addx(QueryParser.GE, 's', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> ge(Date dt)
	{
		this.addx(QueryParser.GE, 'd', dt);
		return new Query3<T>(queryctx);
	}
	public Query3<T> ge(Double val)
	{
		this.addx(QueryParser.GE, 'f', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> ge(long val)
	{
		this.addx(QueryParser.GE, 'l', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> ge(int val)
	{
		this.addx(QueryParser.GE, 'i', val);
		return new Query3<T>(queryctx);
	}
	public Query3<T> ge(boolean b)
	{
		this.addx(QueryParser.GE, 'b', b);
		return new Query3<T>(queryctx);
	}
	
	
	
	//like
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