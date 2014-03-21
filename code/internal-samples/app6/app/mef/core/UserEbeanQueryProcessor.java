package mef.core;
import java.util.List;

import mef.entities.User;
import models.UserModel;

import org.mef.framework.fluent.FluentException;
import org.mef.framework.fluent.IQueryActionProcessor;
import org.mef.framework.fluent.QueryAction;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import boundaries.daos.UserDAO;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.Query;


public class UserEbeanQueryProcessor  extends SfxBaseObj implements IQueryActionProcessor<User>
{
	String orderBy;
	String orderAsc; //"asc" or "desc"
	int limit;
	Expression expr1;
	Expression expr2;
	private String fetch;

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
		fetch = null;
		log("start");
	}

	@Override
	public User findOne() //exactly one
	{
		log("findOne");
		Query<UserModel> qry = createQueryObj();
		UserModel m = null;
		
		if (expr1 == null)
		{
			m = qry.findUnique();
		}
		else
		{
			m = qry.where(expr1).findUnique();
		}

		User u = UserDAO.createEntityFromModel(m);
		return u;
	}

	@Override
	public User findAny() //0 or 1
	{
		log("findAny");
		
		Query<UserModel> qry = createQueryObj();
		List<UserModel> mL = null;
		
		if (expr1 == null)
		{
			mL = qry.findList();
		}
		else
		{
			mL = qry.where(expr1).findList();
		}
		
		if (mL == null || mL.size() == 0)
		{
			return null;
		}
		
		UserModel m = mL.get(0);
		User u = UserDAO.createEntityFromModel(m);
		return u;
	}
	
	private Query<UserModel> createQueryObj()
	{
		Query<UserModel> qry = UserModel.find;
		
		if (fetch != null)
		{
			qry = qry.fetch(fetch);
		}
		
		if (orderBy != null)
		{
//			resultL = db.orderBy(resultL, orderBy, orderAsc, String.class);
			qry = UserModel.find.orderBy(orderBy + " " + orderAsc);
		}
		
		if (this.limit >= 0)
		{
			qry = qry.setMaxRows(limit);
		}
		return qry;
	}
	
	@Override
	public List<User> findMany() 
	{
		log("findMany");
		List<UserModel> mL = null;
		Query<UserModel> qry = createQueryObj();
		
		if (expr1 == null)
		{
			mL = qry.findList();
		}
		else
		{
			mL = qry.where(expr1).findList();
		}

		List<User> uL = UserDAO.createEntityFromModel(mL);
		return uL;
	}
	@Override
	public long findCount() 
	{
		log("findCount");
		Query<UserModel> qry = createQueryObj();
		
		int rowCount = 0;
		if (expr1 == null)
		{
			rowCount = qry.findRowCount();
		}
		else
		{
			rowCount = qry.where(expr1).findRowCount();
		}
		return rowCount;
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
			if (expr1 == null)
			{
				expr1 = doExpr(qaction);
			}
		}
		else if (action.equals("AND"))
		{
			expr2 = doExpr(qaction);
			expr1 = Expr.and(expr1, expr2);
		}
		else if (action.equals("OR"))
		{
			expr2 = Expr.eq(qaction.fieldName, qaction.obj);
			expr1 = Expr.or(expr1, expr2);
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
		{ 
			fetch = (String)qaction.obj; //only 1 fetch supported for now!!
		}
		else
		{
			throw new FluentException("ActionProc: unknown action: " + action);
		}
	}

	private Expression doExpr(QueryAction qaction)
	{
		Expression expr = null;
		
		if (opIs(qaction, "eq"))
		{
			expr = Expr.eq(qaction.fieldName, qaction.obj);
		}
		else if (opIs(qaction, "neq"))
		{
			expr = Expr.ne(qaction.fieldName, qaction.obj);
		}
		else if (opIs(qaction, "lt"))
		{
			expr = Expr.lt(qaction.fieldName, qaction.obj);
		}
		else if (opIs(qaction, "gt"))
		{
			expr = Expr.gt(qaction.fieldName, qaction.obj);
		}
		else if (opIs(qaction, "le"))
		{
			expr = Expr.le(qaction.fieldName, qaction.obj);
		}
		else if (opIs(qaction, "ge"))
		{
			expr = Expr.ge(qaction.fieldName, qaction.obj);
		}
		else
		{
			throw new FluentException("ActionProc: unsupported obj type: " + qaction.obj.getClass().getSimpleName());
		}
		return expr;
	}
	
	private boolean opIs(QueryAction qaction, String op)
	{
		return qaction.op.equals(op);
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