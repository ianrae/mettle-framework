package org.mef.framework.fluent;
import java.util.ArrayList;
import java.util.List;

import org.mef.framework.dao.IDAO;
import org.mef.framework.entities.Entity;
import org.mef.framework.entitydb.EntityDB;
import org.mef.framework.entitydb.IValueMatcher;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;


public class EntityDBQueryProcessor<T>  extends SfxBaseObj implements IQueryActionProcessor<T>
{
	EntityDB<T> db = new EntityDB<T>();
	List<T> dataL;
	List<T> resultL;
	String orderBy;
	String orderAsc; //"asc" or "desc"
	int limit;
	IDAOObserver<T> observer;

	public EntityDBQueryProcessor(SfxContext ctx, List<T> dataL)
	{
		super(ctx);
		this.dataL = dataL;
	}

	@Override
	public void start(List<QueryAction> actionL) 
	{
		resultL = null; //new ArrayList<T>();
		orderBy = null;
		limit = -1;
		log("start");
	}

	private void initResultLIfNeeded()
	{
		if (resultL == null)
		{
			resultL = db.union(dataL, new ArrayList<T>());
		}

		if (orderBy != null)
		{
			resultL = db.orderBy(resultL, orderBy, orderAsc, String.class);
		}

		if (limit >= 0)
		{
			if (limit > resultL.size())
			{
				limit = resultL.size();
			}
			resultL = resultL.subList(0, limit);
		}
	}
	@Override
	public T findAny() //0 or 1
	{
		log("findAny");
		initResultLIfNeeded();

		if (resultL.size() == 0)
		{
			return null;
		}
		
		if (observer != null)
		{
			T entity = resultL.get(0);
			if (entity != null)
			{
				this.observer.onQueryOne(entity);
			}
		}
		return resultL.get(0);
	}
	@Override
	public List<T> findMany() 
	{
		log("findMany");
		initResultLIfNeeded();
		if (observer != null)
		{
			this.observer.onQueryMany(resultL);
		}
		
		return resultL;
	}
	@Override
	public long findCount() 
	{
		log("findCount");
		initResultLIfNeeded();
		return resultL.size();
	}

	@Override
	public void processAction(int index, QueryAction qaction) 
	{
		String action = qaction.action;

		log(String.format(" %d. %s", index, action));

		if (action.equals("ALL"))
		{
			resultL = db.union(dataL, new ArrayList<T>());
		}
		else if (action.equals("WHERE"))
		{
			resultL = findMatchByType(qaction);
		}
		else if (action.equals("AND"))
		{
			List<T> tmp1 = findMatchByType(qaction);
			resultL = db.intersection(resultL, tmp1);
		}
		else if (action.equals("OR"))
		{
			List<T> tmp1 = findMatchByType(qaction);
			resultL = db.union(resultL, tmp1);
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

	private List<T> findMatchByType(QueryAction qaction)
	{
		if (qaction.obj == null)
		{
			throw new FluentException("ActionProc: obj is null");
		}

		if (qaction.op.equals("eq"))
		{
			if (qaction.obj instanceof Long)
			{
				return db.findMatches(dataL, qaction.fieldName, (Long)qaction.obj);
			}
			else if (qaction.obj instanceof Integer)
			{
				return db.findMatches(dataL, qaction.fieldName, (Integer)qaction.obj);
			}
			else if (qaction.obj instanceof String)
			{
				return db.findMatches(dataL, qaction.fieldName, (String)qaction.obj);
			}
			else if (qaction.obj instanceof Boolean)
			{
				return db.findMatches(dataL, qaction.fieldName, (Boolean)qaction.obj);
			}
			else if (qaction.obj instanceof Entity)
			{
				return db.findMatchesEntity(dataL, qaction.fieldName, (Entity)qaction.obj);
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

	private List<T> doCompare(QueryAction qaction, int matchType)
	{
		if (qaction.obj instanceof Integer)
		{
			return db.findCompareMatches(dataL, qaction.fieldName, qaction.obj, Integer.class, matchType);
		}
		else if (qaction.obj instanceof String)
		{
			return db.findCompareMatches(dataL, qaction.fieldName, qaction.obj, String.class, matchType);
		}
		else if (qaction.obj instanceof Long)
		{
			return db.findCompareMatches(dataL, qaction.fieldName, qaction.obj, Long.class, matchType);
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
		
		Class clazz = db.getFieldType(dataL, fieldName);
		//!!currently only works if dataL not empty. fix later
		return clazz;
	}

	@Override
	public void setObserver(IDAOObserver observer) 
	{
		this.observer = observer;
	}

}