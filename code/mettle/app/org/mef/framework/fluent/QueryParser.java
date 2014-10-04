package org.mef.framework.fluent;
import java.util.ArrayList;
import java.util.List;

import org.mef.framework.sfx.SfxBaseObj;



public class QueryParser<T> 
{
	public static final char WHERE = 'W';
	public static final char EQ = '=';
	public static final char LT = '<';
	public static final char GT = '>';
	public static final char LE = 'a';
	public static final char GE = 'b';
	public static final char NEQ = 'c';
	public static final char LIKE = 'l';
	public static final char AND = '&';
	public static final char OR = '|';
	public static final char ORDERBY = 'O';
	public static final char FETCH = 'F';
	public static final char LIMIT = 'M';
	public static final char OFFSET = 'P';
	public static final char EQI = 'I';


	//private List<QueryX> queryL;
	private QueryContext<T> queryctx;
	private List<QueryAction> actionL;

	public QueryParser(QueryContext<T> queryctx)
	{
		this.queryctx = queryctx;
	}

	public T executeAny()
	{
		buildActionList();
		QueryRunner<T> runner = new QueryRunner<T>(actionL, queryctx);
		return runner.executeAny();
	}

	public List<T> executeMany() 
	{
		buildActionList();
		QueryRunner<T> runner = new QueryRunner<T>(actionL, queryctx);
		return runner.executeMany();
	}

	public long executeCount() 
	{
		buildActionList();
		QueryRunner<T> runner = new QueryRunner<T>(actionL, queryctx);
		return runner.executeCount();
	}


	private void buildActionList()
	{
		// [price W  ] [e s] [size &  ] [e f]
		/* L = null
		 * [price W],[e s] -> L1 = dataL.findBy('price', val)
		 * [size &][e f] -> L2 = dataL.findBy('price', val);
		 * L = intersect(L1,L2)
		 * if L is null then L = dataL.all()
		 */

		actionL = new ArrayList<QueryAction>();

		QueryAction currentAction = null;
		for(QStep x : queryctx.queryL)
		{
			switch(x.action)
			{
			case WHERE:
				if (currentAction != null)
				{
					throw new FluentException("misplaced WHERE"); //err!
				}
				else
				{
					currentAction = new QueryAction();
					currentAction.action = QueryAction.WHERE;
					currentAction.fieldName = x.fieldName;
					adjustForSubField(currentAction);
				}
				break;
			case AND:
				if (currentAction != null)
				{
					throw new FluentException("misplaced AND"); //err!
				}
				else
				{
					currentAction = new QueryAction();
					currentAction.action = QueryAction.AND;
					currentAction.fieldName = x.fieldName;
				}
				break;
			case OR:
				if (currentAction != null)
				{
					throw new FluentException("misplaced OR"); //err!
				}
				else
				{
					currentAction = new QueryAction();
					currentAction.action = QueryAction.OR;
					currentAction.fieldName = x.fieldName;
				}
				break;


			case EQI:
				if (currentAction == null)
				{
					throw new FluentException("misplaced EQI"); //err!
				}
				else
				{
					currentAction.op = "eqi";
					currentAction.obj = x.obj;
					actionL.add(currentAction);
					currentAction = null;
				}
				break;
			case EQ:
				if (currentAction == null)
				{
					throw new FluentException("misplaced EQ"); //err!
				}
				else
				{
					currentAction.op = "eq";
					currentAction.obj = x.obj;
					actionL.add(currentAction);
					currentAction = null;
				}
				break;
			case LT:
				if (currentAction == null)
				{
					throw new FluentException("misplaced LT"); //err!
				}
				else
				{
					currentAction.op = "lt";
					currentAction.obj = x.obj;
					actionL.add(currentAction);
					currentAction = null;
				}
				break;
			case GT:
				if (currentAction == null)
				{
					throw new FluentException("misplaced LT"); //err!
				}
				else
				{
					currentAction.op = "gt";
					currentAction.obj = x.obj;
					actionL.add(currentAction);
					currentAction = null;
				}
				break;
			case LE:
				if (currentAction == null)
				{
					throw new FluentException("misplaced LE"); //err!
				}
				else
				{
					currentAction.op = "le";
					currentAction.obj = x.obj;
					actionL.add(currentAction);
					currentAction = null;
				}
				break;
			case GE:
				if (currentAction == null)
				{
					throw new FluentException("misplaced GE"); //err!
				}
				else
				{
					currentAction.op = "ge";
					currentAction.obj = x.obj;
					actionL.add(currentAction);
					currentAction = null;
				}
				break;
			case NEQ:
				if (currentAction == null)
				{
					throw new FluentException("misplaced NEQ"); //err!
				}
				else
				{
					currentAction.op = "neq";
					currentAction.obj = x.obj;
					actionL.add(currentAction);
					currentAction = null;
				}
				break;
				
				
			case LIKE:
				if (currentAction == null)
				{
					throw new FluentException("misplaced LIKE"); //err!
				}
				else
				{
					currentAction.op = "like";
					currentAction.obj = x.obj;
					actionL.add(currentAction);
					currentAction = null;
				}
				break;


			case ORDERBY:
				if (actionL.size() == 0)
				{
					addNewAction(QueryAction.ALL, " ", null, null);
				}

				addNewAction(QueryAction.ORDERBY, " ", x.obj, x.fieldName);
				currentAction = null;
				break;

			case FETCH:
				if (actionL.size() == 0)
				{
					addNewAction(QueryAction.ALL, " ", null, null);
				}

				addNewAction(QueryAction.FETCH, " ", null, x.fieldName);
				currentAction = null;
				break;
			case LIMIT:
				if (actionL.size() == 0)
				{
					addNewAction(QueryAction.ALL, " ", null, null);
				}

				addNewAction(QueryAction.LIMIT, " ", x.obj, null);
				currentAction = null;
				break;


			default:
				//					System.out.println("UKNOWN CMD: " + x.action);
				throw new FluentException("UKNOWN CMD: " + x.action); //err!
			}
		}

		if (currentAction != null)
		{
			currentAction.op = "?";
			actionL.add(currentAction);
			currentAction = null;
		}
	}

	private void addNewAction(String action, String op, Object obj, String fieldName)
	{
		QueryAction currentAction = null;
		currentAction = new QueryAction();
		currentAction.action = action;
		currentAction.fieldName = fieldName;
		currentAction.op = op;
		currentAction.obj = obj;
		actionL.add(currentAction);
	}
	
	private void adjustForSubField(QueryAction qaction)
	{
		String s = qaction.fieldName;
		int pos = s.indexOf('.');
		if (pos < 0)
		{
			return; //no sub-field
		}
		qaction.fieldName = s.substring(0, pos);
		qaction.subFieldName = s.substring(pos + 1);
	}
}