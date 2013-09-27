package mef;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//idea is our DAL would support
//
//  User user = DAL.findById(7);
//  User user2 = DAL.find().all().where("size > 10").orderBy("price").asc().get();
//see below
// Part 1 - simple fluid api for calculator
// Part 2 - builds list and executes at end
// Part 3 - mock DAL 


import mef.dals.MockUserDAL;
import mef.entities.User;
import mef.mocks.MockTaskDAL;

import org.junit.Test;

public class FluidDALTests 
{
	
	//======================== PART 3 =================
	//--DAL --------
	public static class ChairWeightComparator implements Comparator<User> 
	{
		private boolean ascending;
		public ChairWeightComparator(boolean ascending)
		{
			this.ascending = ascending;
		}
	    public int compare(User chair1, User chair2) 
	    {
	    	if (ascending)
	    	{
	    		return chair1.name.compareTo(chair2.name);
	    	}
	    	else
	    	{
	    		return chair2.name.compareTo(chair1.name);
	    	}
	    }
	}	
	
	public static class DalExpression
	{
		int type; //1=+,2=-,3=*,4=/
		String colName;
		String val;

		public DalExpression(int type, String colName, String val)
		{
			this.type = type;
			this.colName = colName;
			this.val = val;
		}
		
		public List<User> apply(MockUserDAL dal, List<User> L) 
		{
			List<User> outL = new ArrayList<User>(); 
					
			switch(type)
			{
			case 1:
				for(User u : L)
				{
					if (u.name.indexOf(val) >= 0) //later use reflection to find colName!!
					{
						outL.add(u);
					}
				}
				break;
			case 2:
				for(User u : L)
				{
					outL.add(u);
				}
				Collections.sort(outL, new ChairWeightComparator(val.equals("asc")));
				break;
			case 3:
				for(User u : L)
				{
					if (evalExpr(u, val))
					{
						outL.add(u);
					}
				}
				break;
			}
			return outL;
		}

		private boolean evalExpr(User u, String val2) 
		{
			return true; //!!
		}
	}
	
	public static class DalRez
	{
		private ArrayList<DalExpression> opL = new ArrayList<DalExpression>();
		MockUserDAL dal;
		
		public DalRez(MockUserDAL dal)
		{
			this.dal = dal;
		}
		
		public List<User> get()
		{
			List<User> L = dal.all();
			for(DalExpression op : opL)
			{
				L = op.apply(dal, L);
			}
			return L;
		}
		
		
		//fluid API
		public DalRez like(String colName, String val)
		{
			opL.add(new DalExpression(1, colName, val));
			return this;
		}
		public DalRez orderBy(boolean asc)
		{
			opL.add(new DalExpression(2, "", (asc) ? "asc" : "desc"));
			return this;
		}
		public DalRez where(String expr)
		{
			opL.add(new DalExpression(3, "", expr));
			return this;
		}
	}
	public static class DalCalc
	{
		MockUserDAL dal;
		
		public DalCalc(MockUserDAL dal)
		{
			this.dal = dal;
		}

		public DalRez getAll() 
		{
			return new DalRez(dal);
		}
	}
	
	@Test
	public void testDalCalc0() 
	{
		MockUserDAL dal = new MockUserDAL();
		initUser(dal, 44, "bob");
		assertEquals(1, dal.size());
		
		DalCalc calc = new DalCalc(dal);
		List<User>L = calc.getAll().get();
		assertEquals(1, L.size());
	}
	
	private User initUser(MockUserDAL dal, long id, String name)
	{
		User t = new User();
		t.id = id;
		t.name = name;
		dal.save(t);
		return t;
	}
	
	@Test
	public void testDalCalc1() 
	{
		MockUserDAL dal = new MockUserDAL();
		initUser(dal, 44, "bob");
		initUser(dal, 45, "sob");
		assertEquals(2, dal.size());
		
		DalCalc calc = new DalCalc(dal);
		List<User>L = calc.getAll().like("name", "bob").get();
		assertEquals(1, L.size());
	}

	@Test
	public void testDalCalc2() 
	{
		MockUserDAL dal = new MockUserDAL();
		initUser(dal, 44, "bob");
		initUser(dal, 45, "sob");
		initUser(dal, 46, "bobby");
		assertEquals(3, dal.size());
		
		DalCalc calc = new DalCalc(dal);
		List<User>L = calc.getAll().like("name", "bob").get();
		assertEquals(2, L.size());
	}
	
	@Test
	public void testDalCalc3() 
	{
		MockUserDAL dal = new MockUserDAL();
		initUser(dal, 45, "sob");
		initUser(dal, 44, "bob");
		initUser(dal, 46, "bobby");
		assertEquals(3, dal.size());
		
		DalCalc calc = new DalCalc(dal);
		List<User>L = calc.getAll().orderBy(true).get();
		assertEquals(3, L.size());
		chkL(L, "bob", "bobby", "sob");
		
		calc = new DalCalc(dal);
		L = calc.getAll().orderBy(false).get();
		assertEquals(3, L.size());
		chkL(L, "sob", "bobby", "bob");
	}

	@Test
	public void testDalCalc4() 
	{
		MockUserDAL dal = new MockUserDAL();
		initUser(dal, 45, "sob");
		initUser(dal, 44, "bob");
		initUser(dal, 46, "bobby");
		assertEquals(3, dal.size());
		
		DalCalc calc = new DalCalc(dal);
		List<User>L = calc.getAll().where("id > 0").get();
		assertEquals(3, L.size());
		chkL(L, "sob", "bob", "bobby");
		
	}
	
	//-------------- helper fns -------------------
	private void chkL(List<User> L, String val0, String val1, String val2)
	{
		assertEquals(val0, L.get(0).name);
		assertEquals(val1, L.get(1).name);
		assertEquals(val2, L.get(2).name);
	}
}

