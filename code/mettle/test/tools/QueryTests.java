package tools;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class QueryTests extends BaseTest
{
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
					if (s.startsWith("_and_"))
					{
						s = s.substring(5);
					}
				}
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
		List<String> fieldNames = Arrays.asList("name", "birthDate");
		
		QueryParser parser = new QueryParser(fieldNames);
		Boolean b = parser.parse(query);
		assertEquals(true, b);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertNotNull(results);
		assertEquals(2, results.size());
		
		QueryParser.Clause clause = results.get(1);
		assertEquals("whereEq", clause.clause);
		
	}

	@Test
	public void test2() 
	{
		String query = "find_by_name_and_birthDate";
		List<String> fieldNames = Arrays.asList("name", "birthDate");
		
		QueryParser parser = new QueryParser(fieldNames);
		Boolean b = parser.parse(query);
		assertEquals(true, b);
		
		List<QueryParser.Clause> results = parser.getResults();
		assertNotNull(results);
		assertEquals(3, results.size());
		
		QueryParser.Clause clause = results.get(1);
		assertEquals("whereEq", clause.clause);
		assertEquals("name", clause.value);
		clause = results.get(2);
		assertEquals("whereEq", clause.clause);
		assertEquals("birthDate", clause.value);
		
	}
}
