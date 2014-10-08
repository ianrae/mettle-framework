package org.mef.framework.fluent;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.mef.framework.dao.IDAO;
import org.mef.framework.entities.Entity;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;


public class QueryRunner<T> extends SfxBaseObj
{
	private List<QueryAction> actionL;
	private QueryContext<T> queryctx;

	public QueryRunner(List<QueryAction> actionL, QueryContext<T> queryctx)
	{
		super(queryctx.getSfxContext());
		this.actionL = actionL;
		this.queryctx = queryctx;

		//init proc
		ProcRegistry registry = (ProcRegistry) this.getInstance(ProcRegistry.class);
		Class clazz = queryctx.classOfT;
		queryctx.proc = registry.findProc(clazz);

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
//			System.out.println(s);
			if (isRelationalAction(action))
			{
				action = processRelationalAction(i, action);
				//				action = queryctx.proc.processRelationalAction(i, action, this.queryctx);
			}

			if (action != null)
			{
				queryctx.proc.processAction(i, action);
			}
			i++;
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private QueryAction processRelationalAction(int i, QueryAction action) 
	{
		Class clazz = queryctx.proc.getRelationalFieldType(action);
		if (clazz == null)
		{
			return null; //is this an error?
		}

		ProcRegistry registry = (ProcRegistry) this.getInstance(ProcRegistry.class);
		IQueryActionProcessor otherProc = registry.findProc(clazz);

		String sav1 = action.fieldName; //addr
		String sav2 = action.subFieldName; //street

		action.fieldName = sav2;
		List<QueryAction> L = new ArrayList<QueryAction>();
		L.add(action);
		otherProc.start(actionL);
		otherProc.processAction(0, action);

		Entity entity = (Entity) otherProc.findAny();

		//so we have found the address object that matches addr.street
		//adjust the action so now we look for it

		QueryAction newAction = new QueryAction();
		newAction.action = action.action;
		newAction.fieldName = sav1;
		newAction.obj = entity;
		newAction.op = action.op;
		return newAction;
	}

	private boolean isRelationalAction(QueryAction qaction) 
	{
		if (qaction.action.equals("WHERE") && qaction.subFieldName != null)
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