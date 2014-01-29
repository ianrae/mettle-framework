

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.mef.framework.sfx.SfxTextReader;

import tools.BaseTest;


public class FluentTests extends BaseTest 
{
	public static class Car
	{

	}
	
	public static class FluentException extends RuntimeException
	{
		public FluentException(String msg)
		{
			super(msg);
		}
	}
	
	public static class QueryAction
	{
		public static final String WHERE = "WHERE";
		public static final String AND = "AND";
		public static final String OR = "OR";
		public static final String ALL = "ALL";
		public static final String ORDERBY = "ORDERBY";
		public static final String FETCH = "FETCH";
		public static final String LIMIT = "LIMIT";
		public static final String OFFSET = "OFFSET"; 
		
		String action;
		String op;
		String fieldName;
		Object obj;
	}
	
	public interface IQueryActionProcessor<T>
	{
		void start(List<QueryAction> actionL);
		T findOne(); //error if > 1
		T findAny();
		List<T> findMany();
		long findCount();
		void processAction(int index, QueryAction action);
	}
	
	public static class QueryContext<T>
	{
		public List<QStep> queryL;
		public IQueryActionProcessor<T> proc;
	}

	public static class QueryRunner<T>
	{
		public List<QueryAction> actionL;
		QueryContext<T> queryctx;
		
		public QueryRunner(List<QueryAction> actionL, QueryContext<T> queryctx)
		{
			this.actionL = actionL;
			this.queryctx = queryctx;
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
				System.out.println(s);
				queryctx.proc.processAction(i, action);
				i++;
			}
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

	public static class QueryParser<T>
	{
		public static final char WHERE = 'W';
		public static final char EQ = 'e';
		public static final char LIKE = 'l';
		public static final char AND = '&';
		public static final char OR = '|';
		public static final char ORDERBY = 'O';
		public static final char FETCH = 'F';
		public static final char LIMIT = 'M';
		public static final char OFFSET = 'P';


		//private List<QueryX> queryL;
		private QueryContext<T> queryctx;
		List<QueryAction> actionL;

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
					
					addNewAction(QueryAction.ORDERBY, " ", null, x.fieldName);
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
	}

	public static class QStep
	{
		public char action;
		public char type;
		public Object obj;
		public String fieldName;
	}

	
	
	public static class QueryBase<T>
	{
		//protected List<QueryX> queryL;
		protected QueryContext<T> queryctx;

		protected void addx(char action, char type, Object obj)
		{
			addx(action, type, obj, null);
		}
		protected void addx(char action, char type, Object obj, String fieldName)
		{
			QStep x = new QStep();
			x.action = action;
			x.type = type;
			x.obj = obj;
			x.fieldName = fieldName;
			queryctx.queryL.add(x);
		}


		protected T dofindAny()
		{
			QueryParser<T> runner = new QueryParser<T>(queryctx);
			T result = runner.executeAny();
			return result;
		}
		//		public Car findAnyIfExists()
		//		{
		//			return null;
		//		}
		protected List<T> doFindMany()
		{
			QueryParser<T> runner = new QueryParser<T>(this.queryctx);
			List<T> resultL = runner.executeMany();
			return resultL;
		}
		protected T doFindUnique()
		{
			QueryParser<T> runner = new QueryParser<T>(this.queryctx);
			List<T> resultL = runner.executeMany(); //!!more efficient latet (use limit 2)
			if (resultL.size() > 1)
			{
				throw new FluentException("FindUnique found > 1 result");
			}
			
			if (resultL.size() == 0)
			{
				return null;
			}
			return resultL.get(0);
		}
		protected long doFindCount()
		{
			QueryParser<T> runner = new QueryParser<T>(this.queryctx);
			long count = runner.executeCount();
			return count;
		}
		
		protected String doDumpQuery()
		{
			String s = "";
			for(QStep x : queryctx.queryL)
			{
				if (x.fieldName == null)
				{
					s += String.format(" [%c %c]", x.action, x.type);
				}
				else
				{
					s += String.format(" [%s %c %c]", x.fieldName, x.action, x.type);
				}
			}
			return s;
		}
	}

	public static class Query3<T> extends QueryBase<T>
	{
		public Query3(QueryContext<T> queryctx)
		{
			this.queryctx = queryctx;
		}

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

		public Query2<T> and(String fieldName)
		{
			this.addx('&', ' ', null, fieldName);
			return new Query2<T>(queryctx);
		}
		public Query2<T> or(String fieldName)
		{
			this.addx('|', ' ', null, fieldName);
			return new Query2<T>(queryctx);
		}
	}

	public static class Query2<T> extends QueryBase<T>
	{
		public Query2(QueryContext<T> queryctx)
		{
			this.queryctx = queryctx;
		}


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
		public Query3<T> eq(boolean b)
		{
			this.addx(QueryParser.EQ, 'b', b);
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

	public static class Query1<T>  extends QueryBase<T>
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
			this.addx(QueryParser.ORDERBY, ' ', null, fieldName);
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

	public static class Dao
	{
		public List<Car> dataL;
//		public List<QueryX> queryL = new ArrayList<QueryX>();
		public QueryContext<Car> queryctx = new QueryContext<Car>();

		public Dao()
		{
			queryctx.queryL = new ArrayList<QStep>();
		}

		public Query1<Car> query()
		{
			queryctx.queryL = new ArrayList<QStep>();
			return new Query1<Car>(queryctx);
		}

		public void setActionProcessor(IQueryActionProcessor<Car> proc) 
		{
			queryctx.proc = proc;
		}
	}
	
	
	public static class MyProc<T> implements IQueryActionProcessor<T>
	{
		private void log(String s)
		{
			System.out.println(s);
		}
		@Override
		public void start(List<QueryAction> actionL) 
		{
			log("start");
		}

		@Override
		public T findOne() //exactly one
		{
			log("findOne");
			return null;
		}

		@Override
		public T findAny() //0 or 1
		{
			log("findAny");
			return null;
		}
		@Override
		public List<T> findMany() 
		{
			log("findMany");
			return new ArrayList<T>();
		}
		@Override
		public long findCount() 
		{
			log("findCount");
			return 0L;
		}

		@Override
		public void processAction(int index, QueryAction action) 
		{
			log(String.format(" %d. %s", index, action.action));
		}
	}

	@Test
	public void test() 
	{
		Dao dao = new Dao();
		dao.setActionProcessor(new MyProc());
		Query1 x1 = dao.query();

		assertNotNull(x1);

		Car car = dao.query().findAny();
		assertNull(car);

		List<Car> L = dao.query().findMany();
		assertNotNull(L);

		car = dao.query().where("price").eq("45").findAny();
		assertNull(car);

		car = dao.query().where("price").eq("45").and("size").eq(35.5).findAny();
		assertNull(car);

		String query = dao.query().where("price").eq("45").and("size").eq(35.5).dumpQuery();
		assertEquals(" [price W  ] [e s] [size &  ] [e f]", query);

		log("here's any");
		car = dao.query().orderBy("price").fetch("users").limit(1).findAny();
		assertNull(car);
		log("end");
		
		log("here's count");
		long count = dao.query().where("price").eq("45").findCount();
		assertEquals(0L, count);
	}

	@Test
	public void testProcessor() 
	{
		Dao dao = new Dao();
		dao.setActionProcessor(new MyProc());

		Car car = dao.query().orderBy("price").fetch("users").limit(1).findAny();
		assertNull(car);
	}
	
//	@Test 
	public void testRouteEdit()
	{
		String path = this.getTestFile("routes1.txt");
		
		SfxTextReader r = new SfxTextReader();
		ArrayList<String> L = r.readFile(path);
		assertEquals(24, L.size());
		
		boolean haveSeenGET = false;
		boolean haveSeenErrorC = false;
		int i = 0;
		for(String line : L)
		{
			String s = line.trim();
			if (line.contains("ErrorController"))
			{
				haveSeenErrorC = true;
				break;
			}
			
			if (s.startsWith("GET"))
			{
				haveSeenGET = true;
				break;
			}
			i++;
		}
		
		if (! haveSeenErrorC && haveSeenGET)
		{
			//insert at i
			String s = " ";
			L.add(i + 1, s);
			s = "GET xxx";
			L.add(i + 2, s);
			s = "GET yyy";
			L.add(i + 3, s);
		}
		
		log("result:");
		for(String line : L)
		{
			log(line);
		}
	}
}
