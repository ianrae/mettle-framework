package tools.sprig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mef.framework.entities.Entity;
import org.mef.framework.loaders.sprig.EntityDataLoader;
import org.mef.sprig.SprigLoader;
import org.mef.sprig.ViaRef;
import org.mef.sprig.Wrapper;
import org.mef.sprig.json.JsonFileReader;

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
	
	
	public static class SizeJLoader extends EntityDataLoader<Size>
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
		public void save(Size entity) 
		{
		}

		@Override
		public void resolve(Size sourceObj, String fieldName, Object obj) 
		{
		}
    }
	
	
    public static class ColorJLoader extends EntityDataLoader<Color>
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
		public void save(Color entity) {
		}

		@Override
		public void resolve(Color sourceObj, String fieldName, Object obj) 
		{
		}
    }
    
    public static class ShirtJLoader extends EntityDataLoader<Shirt>
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
        public void resolve(Shirt sourceObj, String fieldName, Object obj)
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
        }

		@Override
		public void save(Shirt entity) {
		}
    }
    
    
    @Test
	public void test() throws Exception
	{
        String root = "{'records': [%s] }";

		String data = "{'name':'small','num':45,'flag':true,'createDate':'1375675200000','action':'t'},{'name':'medium'}";
		data = String.format(root, data);
		data = fix(data);

		log(data);
		JsonFileReader r = new JsonFileReader("", "Size");
		SizeJLoader loader = new SizeJLoader();
		List<Map<String,Object>> LL = r.parseType(data);
		assertEquals(2, LL.size());
		
		Wrapper wrapper = new Wrapper(loader, r);
		
		List<ViaRef> viaL = new ArrayList<ViaRef>();
		List<Object> L = wrapper.parseObjects(LL, viaL);
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

	@Test
	public void test3() throws Exception
	{
        String root = "{'records': [%s] }";
		String data = "{'sprig_id':1, 'colName':'red'},{'sprig_id':2, 'colName':'blue'}";
		data = String.format(root, data);
		data = fix(data);

		log(data);
		ColorJLoader loader = new ColorJLoader();
		JsonFileReader r = new JsonFileReader("", "Color");
		Wrapper wrapper = new Wrapper(loader, r);
		List<Map<String,Object>> LL = r.parseType(data);
		assertEquals(2, LL.size());
		
		List<ViaRef> viaL = new ArrayList<ViaRef>();
		List<Object> L = wrapper.parseObjects(LL, viaL);
		assertEquals(2, L.size());
		Color color = (Color) L.get(0);
		assertEquals("red", color.colName);
		color = (Color) L.get(1);
		assertEquals("blue", color.colName);
	}


	//helpers
	protected String fix(String s)
	{
		s = s.replace('\'', '"');
		return s;
	}

}
