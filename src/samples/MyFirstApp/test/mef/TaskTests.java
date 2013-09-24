package mef;

import static org.junit.Assert.*;

import java.util.List;


import org.junit.Test;

import mef.dals.MockTaskDAL;
import mef.entities.Task;

public class TaskTests {
	
	@Test
	public void test() 
	{
		MockTaskDAL dal = new MockTaskDAL();
		List<Task> L = dal.all();
		assertEquals(0, L.size());
		
		Task t = new Task();
		long id = 45;
		t.id = id;
		t.label = "abc";

		dal.save(t);
		L = dal.all();
		assertEquals(1, L.size());
		
		Task t2 = dal.findById(id);
		assertEquals("abc", t2.label);
		
		//update record
		t2.label = "def";
		dal.save(t2);
		Task t3 = dal.findById(id);
		assertEquals("def", t3.label);
		assertEquals(1, dal.size());
		
		dal.delete(t2.id);
		Task t4 = dal.findById(id);
		assertEquals(null, t4);
		assertEquals(0, dal.size());
	}

	
}
