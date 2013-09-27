package mef;

import static org.junit.Assert.*;

import mef.mocks.MockFormBinder;

import org.junit.Test;
import org.mef.framework.entities.Entity;

public class BinderTests 
{
	public static class MyEntity extends Entity
	{
		public int age;
		
		public String validate()
		{
			return (age >= 19) ? null : "too young to drink";
		}
	}
	
	@Test
	public void test() 
	{
		MyEntity entity = new MyEntity();
		entity.age = 14;
		MockFormBinder binder = new MockFormBinder(entity);
		assertEquals(false, binder.bind());
		
		entity.age = 19;
		assertEquals(true, binder.bind());
	}

}
