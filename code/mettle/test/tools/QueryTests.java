package tools;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class QueryTests extends BaseTest
{
	public static class QueryRenderer
	{
		public QueryRenderer()
		{}
		
		public String render(List<QueryParser.Clause> resultsL)
		{
			String s = "";
			int i = 0;
			boolean findUnique = false;
			boolean whereAdded = false;
			QueryParser.Clause prev = null;
			
			for(QueryParser.Clause clause : resultsL)
			{
				boolean skip = false;
				if (i == 0)
				{
					if (clause.is("findUnique"))
					{
						findUnique = true;
					}
				}
				else
				{
					//  .eq("status", Order.Status.SHIPPED)  
					if (! whereAdded)
					{
						whereAdded = true;
						s += ".where";
					}
					else if (clause.is("or"))
					{
						s += ".or";
						skip = true;
					}
					else if (clause.is("and"))
					{
						s += ".and";
						skip = true;
					}
					else if (clause.is("orderBy"))
					{
						s += ".orderBy(orderBy)"; //what if multiple orderBy?? need param orderBy2
					}
					else if (clause.is("maxRows"))
					{
						s += ".maxRows(maxRows)"; //what if multiple orderBy?? need param orderBy2
					}
					else if (prev.is("whereEq"))
					{
						s += ".and";
					}
					
					if (clause.is("whereEq"))
					{
						s += renderExpr("eq", clause);
					}
					else if (clause.is("whereGt"))
					{
						s += renderExpr("gt", clause);
					}
					else if (clause.is("whereLt"))
					{
						s += renderExpr("lt", clause);
					}
					else if (clause.is("whereGe"))
					{
						s += renderExpr("ge", clause);
					}
					else if (clause.is("whereLe"))
					{
						s += renderExpr("le", clause);
					}
					else if (clause.is("whereNe"))
					{
						s += renderExpr("ne", clause);
					}
				}
				i++;
				prev = clause;
			}
			
			if (findUnique)
			{
				s += ".findUnique()";
			}
			
			return s;
		}
		
		private String renderExpr(String op, QueryParser.Clause clause)
		{
			String param = clause.value;
			param = param.replace(".", "_");
			return String.format(".%s(\"%s\", %s)", op, clause.value, param);
			
		}
	}
	public static class QueryParser
	{
		public static class Clause
		{
			public String clause;
			public String value;
			
			public Clause(String clause)
			{
				this.clause = clause;
			}
			
			public boolean is(String clauseName)
			{
				return (clause.equals(clauseName));
			}
		}

		private List<Clause> clauseL;
		private List<String> fieldNamesL;
		
		public QueryParser(List<String> fieldNamesL)
		{
			this.fieldNamesL = fieldNamesL;
		}
		
		boolean parse(String s)
		{
			clauseL = new ArrayList<Clause>();
			
			String target = "find_by_";
			if (s.startsWith(target))
			{
				Clause c = new Clause("findUnique");
				clauseL.add(c);
				
				s = s.substring(target.length());
			}
			
			for(String field : fieldNamesL)
			{
				if (s.startsWith(field))
				{
					Clause c = new Clause("whereEq");
					c.value = field;
					this.clauseL.add(c);
					
					s = s.substring(field.length());
					
					if (s.startsWith("_dot_"))
					{
						s = s.substring(5);
						int pos = s.indexOf('_');
						if (pos <= 0)
						{
							String name = s;
							c.value += "." + name;
						}
						else
						{
							String name = s.substring(0, pos);
							c.value += "." + name;
							s = s.substring(pos + 1);
						}
					}
					
					if (s.startsWith("_eq"))
					{}
					else if (s.startsWith("_gt"))
					{
						c.clause = "whereGt";
					}
					else if (s.startsWith("_ge"))
					{
						c.clause = "whereGe";
					}
					else if (s.startsWith("_lt"))
					{
						c.clause = "whereLt";
					}
					else if (s.startsWith("_le"))
					{
						c.clause = "whereLe";
					}
					else if (s.startsWith("_ne"))
					{
						c.clause = "whereNe";
					}
					else if (s.startsWith("_like"))
					{
						c.clause = "whereLike";
					}
					else if (s.startsWith("_ilike"))
					{
						c.clause = "whereILike";
					}
					
					if (s.startsWith("_and_"))
					{
						s = s.substring(5);
					}
					if (s.startsWith("_or_"))
					{
						c = new Clause("or");
						clauseL.add(c);
						s = s.substring(4);
					}
				}
			}
			
			target = "_orderBy";
			if (s.startsWith(target))
			{
				Clause c = new Clause("orderBy");
				clauseL.add(c);
				
				s = s.substring(target.length());
			}
			
			target = "_maxRows";
			if (s.startsWith(target))
			{
				Clause c = new Clause("maxRows");
				clauseL.add(c);
				
				s = s.substring(target.length());
			}
			
			return true;
		}
		
		List<Clause> getResults()
		{
			
			return clauseL;
		}
	}
	@Test
	public void test() 
	{
		String query = "find_by_name";
		QueryParser parser = startParse(query);
		chkClause(parser, 1, "whereEq", "name");
		
		chkRender(".where.eq(\"name\", name).findUnique()", parser);
	}
	
	private void chkRender(String expected, QueryParser parser)
	{
		QueryRenderer r = new QueryRenderer();
		assertEquals(expected, r.render(parser.getResults()));
	}

	@Test
	public void test2() 
	{
		String query = "find_by_name_and_birthDate";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(3, results.size());
		chkClause(parser, 1, "whereEq", "name");
		chkClause(parser, 2, "whereEq", "birthDate");
		
		chkRender(".where.eq(\"name\", name).and.eq(\"birthDate\", birthDate).findUnique()", parser);
	}
	
	@Test
	public void test3() 
	{
		String query = "find_by_name_or_birthDate";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(4, results.size());
		chkClause(parser, 1, "whereEq", "name");
		chkClause(parser, 2, "or", null);
		chkClause(parser, 3, "whereEq", "birthDate");
		
		chkRender(".where.eq(\"name\", name).or.eq(\"birthDate\", birthDate).findUnique()", parser);
	}
	
	@Test
	public void test4() 
	{
		String query = "find_by_name_or_birthDate_orderBy";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(5, results.size());
		chkClause(parser, 1, "whereEq", "name");
		chkClause(parser, 2, "or", null);
		chkClause(parser, 3, "whereEq", "birthDate");
		chkClause(parser, 4, "orderBy", null);
		
		chkRender(".where.eq(\"name\", name).or.eq(\"birthDate\", birthDate).orderBy(orderBy).findUnique()", parser);
	}
	
	@Test
	public void test5() 
	{
		String query = "find_by_name_or_birthDate_maxRows";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(5, results.size());
		chkClause(parser, 1, "whereEq", "name");
		chkClause(parser, 2, "or", null);
		chkClause(parser, 3, "whereEq", "birthDate");
		chkClause(parser, 4, "maxRows", null);
		
		chkRender(".where.eq(\"name\", name).or.eq(\"birthDate\", birthDate).maxRows(maxRows).findUnique()", parser);
	}
	
	@Test
	public void test6() 
	{
		String query = "find_by_name_gt";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(2, results.size());
		chkClause(parser, 1, "whereGt", "name");
		chkRender(".where.gt(\"name\", name).findUnique()", parser);
	}
	@Test
	public void test6a() 
	{
		String query = "find_by_name_lt";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(2, results.size());
		chkClause(parser, 1, "whereLt", "name");
		chkRender(".where.lt(\"name\", name).findUnique()", parser);
	}
	@Test
	public void test6b() 
	{
		String query = "find_by_name_ge";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(2, results.size());
		chkClause(parser, 1, "whereGe", "name");
		chkRender(".where.ge(\"name\", name).findUnique()", parser);
	}
	@Test
	public void test6c() 
	{
		String query = "find_by_name_le";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(2, results.size());
		chkClause(parser, 1, "whereLe", "name");
		chkRender(".where.le(\"name\", name).findUnique()", parser);
	}
	@Test
	public void test6d() 
	{
		String query = "find_by_name_ne";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(2, results.size());
		chkClause(parser, 1, "whereNe", "name");
		chkRender(".where.ne(\"name\", name).findUnique()", parser);
	}
	
	@Test
	public void test7() 
	{
		String query = "find_by_customer_dot_name";
		QueryParser parser = startParse(query);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertEquals(2, results.size());
		chkClause(parser, 1, "whereEq", "customer.name");
		chkRender(".where.eq(\"customer.name\", customer_name).findUnique()", parser);
	}
	
	//--helper--
	private QueryParser startParse(String query)
	{
		List<String> fieldNames = Arrays.asList("name", "birthDate", "customer");
		
		QueryParser parser = new QueryParser(fieldNames);
		Boolean b = parser.parse(query);
		assertEquals(true, b);
		return parser;
	}
	
	private void chkClause(QueryParser parser, int index, String expect, String value)
	{
		List<QueryParser.Clause> results = parser.getResults();
		QueryParser.Clause clause = results.get(index);
		assertEquals(expect, clause.clause);
		assertEquals(value, clause.value);
		
	}
}
