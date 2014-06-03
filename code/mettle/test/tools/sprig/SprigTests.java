package tools.sprig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mef.framework.entities.Entity;
import org.mef.framework.loaders.sprig.SprigDataLoader;
import org.mef.framework.loaders.sprig.SprigLoader;

import tools.BaseTest;


public class SprigTests extends BaseTest
{
	public static class Size extends Entity
	{
		public String name;
		public int num;
		public boolean flag;
		public Date createDate;
		public Long amount;
		public char action;
	}

	public static class Color extends Entity
	{
		public int id;
		public String colName;
	}

	public static class Shirt extends Entity
	{
		public int id;
		public Size size;
		public Color color;
		public int colorId;
	}
	
	
	public static class SizeJLoader extends SprigLoader<Size>
    {
        public SizeJLoader()
        {
            super(Size.class);
        }
       
        @Override
        public void parse(Size obj, Map<String,Object> map)
        {
            if (map.containsKey("name"))
            {
                obj.name = (String)map.get("name");
            }
            if (map.containsKey("num"))
            {
                obj.num = getInt(map, "num");
            }
            if (map.containsKey("flag"))
            {
                obj.flag = getBoolean(map, "flag");
            }
            if (map.containsKey("createDate"))
            {
            	obj.createDate = this.getDate(map, "createDate");
            }
            if (map.containsKey("amount"))
            {
            	obj.amount = this.getLong(map, "amount");
            }
            if (map.containsKey("action"))
            {
            	obj.action = getChar(map, "action");
            }
        }

		@Override
		public Size findRecord(Size target) 
		{
			return null;
		}

		@Override
		public void copyAllButId(Size src, Size dest) 
		{
		}

		@Override
		public void updateUdate(Size existing) 
		{
		}

		@Override
		public void saveEntity(Size entity) 
		{
		}

		@Override
		public void resolve(Entity sourceObj, String fieldName, Entity obj) {
			// TODO Auto-generated method stub
			
		}
    }
    public static class ColorJLoader extends SprigLoader<Color>
    {
        public ColorJLoader()
        {
            super(Color.class);
        }
       
        @Override
        public void parse(Color obj, Map<String,Object> map)
        {
            if (map.containsKey("colName"))
            {
                obj.colName = (String)map.get("colName");
            }
        }

		@Override
		public void saveEntity(Color entity) {
		}

		@Override
		public void resolve(Entity sourceObj, String fieldName, Entity obj) 
		{
		}
    }
    
    public static class ShirtJLoader extends SprigLoader<Shirt>
    {
        public ShirtJLoader()
        {
            super(Shirt.class);
        }
       
        @Override
        public void parse(Shirt obj, Map<String,Object> map)
        {
            if (map.containsKey("id"))
            {
            	Integer n = (Integer) map.get("id");
                obj.id = n;
            }
            //can't load size directly
        }
        
        @Override
        public void resolve(Entity sourceObj, String fieldName, Entity obj)
        {
            //gen one for each field that is not string,int,bool, etc
            if (fieldName.equals("color"))
            {
                Shirt shirt = (Shirt) sourceObj;
                shirt.color = (Color) obj;
            }
            else if (fieldName.equals("colorId"))
            {
                Shirt shirt = (Shirt) sourceObj;
                Color other = (Color)obj;
                shirt.colorId  = other.id;
            }
           
            //if was saltId then
//            if (fieldName.equals("salt"))
//            {
//                Pizza pizza = (Pizza) sourceObj;
//                pizza.saltId = (Long) obj; //or (Integer) !!must be an object (i.e. Integer not int)
//            }
        }

		@Override
		public void saveEntity(Shirt entity) {
		}
    }
    
    public static class MyDataLoader extends SprigDataLoader
    {

		@Override
		protected SprigLoader doGetLoader(String className) 
		{
            if (className.equals("Size"))
            {
                return new SizeJLoader();
            }
            else if (className.equals("Color"))
            {
                return new ColorJLoader();
            }
            else if (className.equals("Shirt"))
            {
            	return new ShirtJLoader();
            }
            else
            {
            	return null;
            }
		}
    	
    }
    
    @Test
	public void test() throws Exception
	{
        String root = "{'types': [%s] }";

		String data = "{'type':'Size', 'items':[{'name':'small','num':45,'flag':true,'createDate':'1375675200000','action':'t'},{'name':'medium'}]}";
		data = String.format(root, data);
		data = fix(data);

		log(data);
		SprigDataLoader loader = new MyDataLoader();
		loader.parseTypes(data);
		
		List<Entity> L = loader.resultMap.get(Size.class);
		assertEquals(2, L.size());
		Size size = (Size) L.get(0);
		assertEquals("small", size.name);
		assertEquals(45, size.num);
		assertEquals(true, size.flag);
		assertEquals(1375675200000L, size.createDate.getTime());   
		assertEquals('t', size.action);
		
		size = (Size) L.get(1);
		assertEquals("medium", size.name);
	}

//	@Test
//	public void test2() throws Exception
//	{
//        String root = "{'types': [%s] }";
//
//		String data1 = "{'type':'Size', 'items':[{'name':'small'},{'name':'medium'}]}";
//		String data2 = "{'type':'Shirt', 'items':[{'id':1,'size via Size.name':'medium'},{'id':2,'size via Size.name':'small'}]}";
//		String data = String.format(root, data1 + "," + data2);
//		data = fix(data);
//
//		log(data);
//		MyDataLoader loader = new MyDataLoader();
//		loader.parseTypes(data);
//		
//		List<Entity> L = loader.resultMap.get(Size.class);
//		assertEquals(2, L.size());
//		Size size = (Size) L.get(0);
//		assertEquals("small", size.name);
//		size = (Size) L.get(1);
//		assertEquals("medium", size.name);
//		
//		assertEquals(2, loader.viaL.size());
//		L = loader.resultMap.get(Shirt.class);
//		assertEquals(2, L.size());
//	}

	@Test
	public void test3() throws Exception
	{
        String root = "{'types': [%s] }";

		String data = "{'type':'Color', 'items':[{'sprig_id':1, 'colName':'red'},{'sprig_id':2, 'colName':'blue'}]}";
		data = String.format(root, data);
		data = fix(data);

		log(data);
		MyDataLoader loader = new MyDataLoader();
		loader.parseTypes(data);
		
		List<Entity> L = loader.resultMap.get(Color.class);
		assertEquals(2, L.size());
		Color color = (Color) L.get(0);
		assertEquals("red", color.colName);
		color = (Color) L.get(1);
		assertEquals("blue", color.colName);
	}

	@Test
	public void test4() throws Exception
	{
        String root = "{'types': [%s] }";

		String data1 = "{'type':'Color', 'items':[{'sprig_id':1, 'colName':'red'},{'sprig_id':2, 'colName':'blue'}]}";
//		String data2 = "{'type':'Shirt', 'items':[{'id':1,'color via Color.sprig_id':'2'}]}";
		String data2 = "{'type':'Shirt', 'items':[{'id':1,'color':'<% sprig_record(Color,2)%>'}]}";
		String data = String.format(root, data1 + "," + data2);
		data = fix(data);

		log(data);
		MyDataLoader loader = new MyDataLoader();
		loader.parseTypes(data);
		List<Entity> L = loader.resultMap.get(Color.class);
		assertEquals(2, L.size());
		Color color = (Color) L.get(0);
		assertEquals("red", color.colName);
		color = (Color) L.get(1);
		assertEquals("blue", color.colName);
		
		boolean b = loader.resolveDeferred();
		assertTrue(b);
		L = loader.resultMap.get(Shirt.class);
		assertEquals(1, L.size());
		Shirt shirt = (Shirt) L.get(0);
		assertNotNull(shirt.color);
	}

	@Test
	public void test5() throws Exception
	{
        String root = "{'types': [%s] }";

		String data1 = "{'type':'Color', 'items':[{'sprig_id':1, 'colName':'red'},{'sprig_id':2, 'colName':'blue'}]}";
		String data2 = "{'type':'Shirt', 'items':[{'id':1,'colorId':'<% sprig_record(Color,2)%>'}]}";
		String data = String.format(root, data1 + "," + data2);
		data = fix(data);

		log(data);
		MyDataLoader loader = new MyDataLoader();
		loader.parseTypes(data);
		List<Entity> L = loader.resultMap.get(Color.class);
		assertEquals(2, L.size());
		Color color = (Color) L.get(0);
		assertEquals("red", color.colName);
		color = (Color) L.get(1);
		assertEquals("blue", color.colName);
		
		//simulate dao load
		color.id = 55;
		
		boolean b = loader.resolveDeferred();
		assertTrue(b);
		
		L = loader.resultMap.get(Shirt.class);
		assertEquals(1, L.size());
		Shirt shirt = (Shirt) L.get(0);
		assertNull(shirt.color);
		assertEquals(55, shirt.colorId);
	}
	//helpers
	protected String fix(String s)
	{
		s = s.replace('\'', '"');
		return s;
	}

}
