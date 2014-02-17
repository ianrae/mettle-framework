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

import boundaries.daos.UserDAO;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionList;


public class UserEbeanQueryProcessor  extends SfxBaseObj implements IQueryActionProcessor<User>
{
	String orderBy;
	String orderAsc; //"asc" or "desc"
	int limit;
//	ExpressionList expr;
	Expression expr1;
	Expression expr2;

	public UserEbeanQueryProcessor(SfxContext ctx)
	{
		super(ctx);
	}

	@Override
	public void start(List<QueryAction> actionL) 
	{
		orderBy = null;
		limit = -1;
		expr1 = expr2 = null;
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
		List<UserModel> mL = null;
		
		if (expr1 == null)
		{
			mL = UserModel.find.all();
		}
		else
		{
			mL = UserModel.find.where(expr1).findList();
		}

		List<User> uL = UserDAO.createEntityFromModel(mL);
		return uL;
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

		log(String.format(" %d. %s -- %s %s", index, action, qaction.op, qaction.fieldName));

		if (action.equals("ALL"))
		{
//			resultL = db.union(dataL, new ArrayList<T>());
		}
		else if (action.equals("WHERE"))
		{
//			resultL = findMatchByType(qaction);
			if (expr1 == null)
			{
				if (opIs(qaction, "eq"))
				{
					expr1 = Expr.eq(qaction.fieldName, qaction.obj);
				}
				else if (opIs(qaction, "neq"))
				{
					expr1 = Expr.ne(qaction.fieldName, qaction.obj);
				}
				else if (opIs(qaction, "lt"))
				{
					expr1 = Expr.lt(qaction.fieldName, qaction.obj);
				}
				else if (opIs(qaction, "gt"))
				{
					expr1 = Expr.gt(qaction.fieldName, qaction.obj);
				}
				else if (opIs(qaction, "le"))
				{
					expr1 = Expr.le(qaction.fieldName, qaction.obj);
				}
				else if (opIs(qaction, "ge"))
				{
					expr1 = Expr.ge(qaction.fieldName, qaction.obj);
				}
				else
				{
					throw new FluentException("ActionProc: unsupported obj type: " + qaction.obj.getClass().getSimpleName());
				}
			}
		}
		else if (action.equals("AND"))
		{
//			List<T> tmp1 = findMatchByType(qaction);
//			resultL = db.intersection(resultL, tmp1);
		}
		else if (action.equals("OR"))
		{
			expr2 = Expr.eq(qaction.fieldName, qaction.obj);
			expr1 = Expr.or(expr1, expr2);
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
	
	private boolean opIs(QueryAction qaction, String op)
	{
		return qaction.op.equals(op);
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