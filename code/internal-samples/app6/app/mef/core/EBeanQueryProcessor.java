//package mef.core;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import mef.entities.User;
//import models.UserModel;
//
//import org.mef.framework.dao.IDAO;
//import org.mef.framework.entities.Entity;
//import org.mef.framework.entitydb.EntityDB;
//import org.mef.framework.entitydb.IValueMatcher;
//import org.mef.framework.fluent.FluentException;
//import org.mef.framework.fluent.IQueryActionProcessor;
//import org.mef.framework.fluent.QueryAction;
//import org.mef.framework.sfx.SfxBaseObj;
//import org.mef.framework.sfx.SfxContext;
//
//import boundaries.daos.UserDAO;
//
//import com.avaje.ebean.ExpressionList;
//
//import play.db.ebean.Model.Finder;
//
//
//public class EBeanQueryProcessor extends SfxBaseObj implements IQueryActionProcessor<User>
//{
//	Finder<Long,UserModel> find;
//	ExpressionList<UserModel> exprL;
//	List<User> resultL;
//	List<UserModel> modelL;
//	String orderBy;
//	String orderAsc; //"asc" or "desc"
//	int limit;
//
//	public EBeanQueryProcessor(SfxContext ctx, Finder<Long, UserModel> find)
//	{
//		super(ctx);
//		this.find = find;
//	}
//
//	@Override
//	public void start(List<QueryAction> actionL) 
//	{
//		resultL = null; //new ArrayList<User>();
//		modelL = null;
//		exprL = null;
//		orderBy = null;
//		limit = -1;
//		log("start");
//	}
//	
//
//
//	@Override
//	public User findOne() //exactly one
//	{
//		log("findOne");
//		if (exprL == null) //no where?
//		{
//			throw new FluentException("findOne, missing clause");
//		}
//		
//		UserModel model = exprL.findUnique();
//		User entity = UserDAO.createEntityFromModel(model);
//		return entity;
//	}
//
//	private void initResultLIfNeeded()
//	{
////		if (exprL == null)
////		{
////			modelL = find.all();
////		}
//
////		if (orderBy != null)
////		{
////			resultL = db.orderBy(resultL, orderBy, orderAsc, String.class);
////		}
////
////		if (limit >= 0)
////		{
////			if (limit > resultL.size())
////			{
////				limit = resultL.size();
////			}
////			resultL = resultL.subList(0, limit);
////		}
//		
//		//!!convert model to entity
//		resultL =  (List<User>) UserDAO.createEntityFromModel(modelL);
//	}
//	@Override
//	public User findAny() //0 or 1
//	{
//		log("findAny");
//		initResultLIfNeeded();
//
//		if (resultL.size() == 0)
//		{
//			return null;
//		}
//		return resultL.get(0);
//	}
//	@Override
//	public List<User> findMany() 
//	{
//		log("findMany");
//		initResultLIfNeeded();
//		return resultL;
//	}
//	@Override
//	public long findCount() 
//	{
//		log("findCount");
//		initResultLIfNeeded();
//		return resultL.size();
//	}
//	
//
//	@Override
//	public void processAction(int index, QueryAction qaction) 
//	{
//		String action = qaction.action;
//
//		log(String.format(" %d. %s", index, action));
//
//		if (action.equals("ALL"))
//		{
//			List<UserModel> L = find.all();
//			this.modelL = L;
//		}
//		else if (action.equals("WHERE"))
//		{
//			findMatchByType(qaction);
//		}
////		else if (action.equals("AND"))
////		{
////			List<User> tmp1 = findMatchByType(qaction);
////			resultL = db.intersection(resultL, tmp1);
////		}
////		else if (action.equals("OR"))
////		{
////			List<User> tmp1 = findMatchByType(qaction);
////			resultL = db.union(resultL, tmp1);
////		}
//		else if (action.equals("ORDERBY"))
//		{
//			orderBy = qaction.fieldName;
//			orderAsc = (String) qaction.obj;
//		}
//		else if (action.equals("LIMIT"))
//		{
//			limit = (Integer)qaction.obj;
//		}
//		else if (action.equals("FETCH"))
//		{ //nothing to do
//		}
//		else
//		{
//			throw new FluentException("ActionProc: unknown action: " + action);
//		}
//	}
//
//	private void findMatchByType(QueryAction qaction)
//	{
//		if (qaction.obj == null)
//		{
//			throw new FluentException("ActionProc: obj is null");
//		}
//		
//		if (exprL == null)
//		{
//			exprL = find.where();
//		}
//
//		if (qaction.op.equals("eq"))
//		{
//			if (qaction.obj instanceof Entity)
//			{
//				//Entity doesn't have id!! fix this
//				exprL.eq(qaction.fieldName + ".id", qaction.obj);
//			}
//			else
//			{
//				exprL.eq(qaction.fieldName, qaction.obj);
////				throw new FluentException("ActionProc: unsupported obj type: " + qaction.obj.getClass().getSimpleName());
//			}
//		}
//		else if (qaction.op.equals("lt"))
//		{
//			exprL.lt(qaction.fieldName, qaction.obj);
//		}
//		else if (qaction.op.equals("le"))
//		{
//			exprL.le(qaction.fieldName, qaction.obj);
//		}
//		else if (qaction.op.equals("gt"))
//		{
//			exprL.gt(qaction.fieldName, qaction.obj);
//		}
//		else if (qaction.op.equals("ge"))
//		{
//			exprL.ge(qaction.fieldName, qaction.obj);
//		}
//		else if (qaction.op.equals("neq"))
//		{
//			exprL.ne(qaction.fieldName, qaction.obj);
//		}
//		else
//		{
//			throw new FluentException("ActionProc: unsupported op type: " + qaction.op);
//		}
//	}
//
//
//	@Override
//	public Class<User> getRelationalFieldType(QueryAction qaction) 
//	{
//		String fieldName = qaction.fieldName;
//		
//		//!!use reflection to get type of field
//		Class<User> clazz = null; //db.getFieldType(dataL, fieldName);
//		return clazz;
//	}
//
//
//
//}