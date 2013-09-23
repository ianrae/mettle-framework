package mef;

import static org.junit.Assert.*;

import java.util.List;


import org.junit.Test;

import mef.dals.MockTaskDAL;
import mef.entities.TaskEO;

public class TaskTests {
	
	@Test
	public void test() 
	{
		MockTaskDAL dal = new MockTaskDAL();
		List<TaskEO> L = dal.findAll();
		assertEquals(0, L.size());
		
		TaskEO t = new TaskEO();
		long id = 45;
		t.id = id;
		t.label = "abc";

		dal.save(t);
		L = dal.findAll();
		assertEquals(1, L.size());
		
		TaskEO t2 = dal.findById(id);
		assertEquals("abc", t2.label);
		
		//update record
		t2.label = "def";
		dal.save(t2);
		TaskEO t3 = dal.findById(id);
		assertEquals("def", t3.label);
		assertEquals(1, dal.size());
		
		dal.delete(t2.id);
		TaskEO t4 = dal.findById(id);
		assertEquals(null, t4);
		assertEquals(0, dal.size());
	}

	
}
