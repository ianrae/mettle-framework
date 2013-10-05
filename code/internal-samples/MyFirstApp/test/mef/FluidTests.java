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


import mef.daos.mocks.MockTaskDAO;
import mef.entities.User;

import org.junit.Test;

public class FluidTests 
{
	//======================== PART 1 =================
	//--simple approach where we calc as we go--------
	public static class Rez
	{
		public int _val;
		
		public int get()
		{
			return _val;
		}
		
		
		//fluid API
		public Rez add(int n)
		{
			_val += n;
			return this;
		}
		public Rez times(int n)
		{
			_val *= n;
			return this;
		}
		public Rez minus(int n)
		{
			_val -= n;
			return this;
		}
	}
	public static class Calc
	{

		public Rez calculate() 
		{
			return new Rez();
		}
		
	}
	
	@Test
	public void test0() 
	{
		Calc calc = new Calc();
		int result = calc.calculate().get();
		
		assertEquals(0, result);
	}

	@Test
	public void test1() 
	{
		Calc calc = new Calc();
		int result = calc.calculate().add(5).get();
		
		assertEquals(5, result);
	}

	@Test
	public void test2() 
	{
		Calc calc = new Calc();
		int result = calc.calculate().add(5).times(2).minus(3).get();
		
		assertEquals(7, result);
	}
	
	
	//======================== PART 2 =================
	//--simple approach where we calc as we go--------
	public static class Operation
	{
		int type; //1=+,2=-,3=*,4=/
		int val;

		public Operation(int type, int val)
		{
			this.type = type;
			this.val = val;
		}
		
		public int apply(int val2) 
		{
			int n = 0;
			switch(type)
			{
			case 1:
				n = val + val2;
				break;
			case 2:
				n = val2 - val;
				break;
			case 3:
				n = val * val2;
				break;
			}
			return n;
		}
	}
	
	public static class FullRez
	{
		private ArrayList<Operation> opL = new ArrayList<FluidTests.Operation>();
		
		public int get()
		{
			int val = 0;
			for(Operation op : opL)
			{
				val = op.apply(val);
			}
			return val;
		}
		
		
		//fluid API
		public FullRez add(int n)
		{
			opL.add(new Operation(1, n));
			return this;
		}
		public FullRez times(int n)
		{
			opL.add(new Operation(3, n));
			return this;
		}
		public FullRez minus(int n)
		{
			opL.add(new Operation(2, n));
			return this;
		}
	}
	public static class FullCalc
	{

		public FullRez calculate() 
		{
			return new FullRez();
		}
		
	}
	
	@Test
	public void testFull0() 
	{
		FullCalc calc = new FullCalc();
		int result = calc.calculate().get();
		
		assertEquals(0, result);
	}

	@Test
	public void testFull1() 
	{
		FullCalc calc = new FullCalc();
		int result = calc.calculate().add(5).get();
		
		assertEquals(5, result);
	}

	@Test
	public void testFull2() 
	{
		FullCalc calc = new FullCalc();
		int result = calc.calculate().add(5).times(2).minus(3).get();
		
		assertEquals(7, result);
	}
	
	
}
