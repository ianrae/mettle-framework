package mef.core;
import java.util.ArrayList;
import java.util.List;

import mef.entities.User;
import models.UserModel;

import org.mef.framework.dao.IDAO;
import org.mef.framework.entities.Entity;
import org.mef.framework.entitydb.EntityDB;
import org.mef.framework.entitydb.IValueMatcher;
import org.mef.framework.fluent.FluentException;
import org.mef.framework.fluent.IQueryActionProcessor;
import org.mef.framework.fluent.QueryAction;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;


public class UserEbeanQueryProcessor  extends SfxBaseObj implements IQueryActionProcessor<User>
{
	UserModel model;
	String orderBy;
	String orderAsc; //"asc" or "desc"
	int limit;

	public UserEbeanQueryProcessor(SfxContext ctx)
	{
		super(ctx);
	}

	@Override
	public void start(List<QueryAction> actionL) 
	{
		orderBy = null;
		limit = -1;
		log("start");
	}

	@Override
	public User findOne() //exactly one
	{
		log("findOne");
		return null;
	}

	private void initResultLIfNeeded()
	{
//		if (resultL == null)
//		{
//			resultL = db.union(dataL, new ArrayList<T>());
//		}
//
//		if (orderBy != null)
//		{
//			resultL = db.orderBy(resultL, orderBy, orderAsc, String.class);
//		}
//
//		if (limit >= 0)
//		{
//			if (limit > resultL.size())
//			{
//				limit = resultL.size();
//			}
//			resultL = resultL.subList(0, limit);
//		}
	}
	@Override
	public User findAny() //0 or 1
	{
		log("findAny");
		initResultLIfNeeded();

//		if (resultL.size() == 0)
//		{
//			return null;
//		}
//		return resultL.get(0);
		return null;
	}
	@Override
	public List<User> findMany() 
	{
		log("findMany");
		initResultLIfNeeded();
		return null;
	}
	@Override
	public long findCount() 
	{
		log("findCount");
		initResultLIfNeeded();
		return 0;
	}

	@Override
	public void processAction(int index, QueryAction qaction) 
	{
		String action = qaction.action;

		log(String.format(" %d. %s", index, action));

		if (action.equals("ALL"))
		{
//			resultL = db.union(dataL, new ArrayList<T>());
		}
		else if (action.equals("WHERE"))
		{
//			resultL = findMatchByType(qaction);
		}
		else if (action.equals("AND"))
		{
//			List<T> tmp1 = findMatchByType(qaction);
//			resultL = db.intersection(resultL, tmp1);
		}
		else if (action.equals("OR"))
		{
//			List<T> tmp1 = findMatchByType(qaction);
//			resultL = db.union(resultL, tmp1);
		}
		else if (action.equals("ORDERBY"))
		{
			orderBy = qaction.fieldName;
			orderAsc = (String) qaction.obj;
		}
		else if (action.equals("LIMIT"))
		{
			limit = (Integer)qaction.obj;
		}
		else if (action.equals("FETCH"))
		{ //nothing to do
		}
		else
		{
			throw new FluentException("ActionProc: unknown action: " + action);
		}
	}

	private List<User> findMatchByType(QueryAction qaction)
	{
		if (qaction.obj == null)
		{
			throw new FluentException("ActionProc: obj is null");
		}

		if (qaction.op.equals("eq"))
		{
			if (qaction.obj instanceof Long)
			{
				return null; //db.findMatches(dataL, qaction.fieldName, (Long)qaction.obj);
			}
			else if (qaction.obj instanceof Integer)
			{
				return null; //db.findMatches(dataL, qaction.fieldName, (Integer)qaction.obj);
			}
			else if (qaction.obj instanceof String)
			{
				return null; //db.findMatches(dataL, qaction.fieldName, (String)qaction.obj);
			}
			else if (qaction.obj instanceof Entity)
			{
				return null; //db.findMatchesEntity(dataL, qaction.fieldName, (Entity)qaction.obj);
			}
			else
			{
				throw new FluentException("ActionProc: unsupported obj type: " + qaction.obj.getClass().getSimpleName());
			}
		}
		else if (qaction.op.equals("lt"))
		{
			return doCompare(qaction, IValueMatcher.LT);
		}
		else if (qaction.op.equals("le"))
		{
			return doCompare(qaction, IValueMatcher.LE);
		}
		else if (qaction.op.equals("gt"))
		{
			return doCompare(qaction, IValueMatcher.GT);
		}
		else if (qaction.op.equals("ge"))
		{
			return doCompare(qaction, IValueMatcher.GE);
		}
		else if (qaction.op.equals("neq"))
		{
			return doCompare(qaction, IValueMatcher.NEQ);
		}
		else
		{
			throw new FluentException("ActionProc: unsupported op type: " + qaction.op);
		}
	}

	private List<User> doCompare(QueryAction qaction, int matchType)
	{
		if (qaction.obj instanceof Integer)
		{
			return null; //db.findCompareMatches(dataL, qaction.fieldName, qaction.obj, Integer.class, matchType);
		}
		else if (qaction.obj instanceof String)
		{
			return null; //db.findCompareMatches(dataL, qaction.fieldName, qaction.obj, String.class, matchType);
		}
		else if (qaction.obj instanceof Long)
		{
			return null; //db.findCompareMatches(dataL, qaction.fieldName, qaction.obj, Long.class, matchType);
		}
		else
		{
			throw new FluentException("ActionProc: unsupported obj type: " + qaction.obj.getClass().getSimpleName());
		}
	}

	@Override
	public Class getRelationalFieldType(QueryAction qaction) 
	{
		String fieldName = qaction.fieldName;
		
		Class clazz = null; //db.getFieldType(dataL, fieldName);
		//!!currently only works if dataL not empty. fix later
		return clazz;
	}

}