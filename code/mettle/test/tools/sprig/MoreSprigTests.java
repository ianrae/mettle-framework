package tools.sprig;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mef.framework.entities.Entity;
import org.mef.framework.loaders.sprig.Sprig;
import org.mef.framework.loaders.sprig.SprigLoader;

import tools.BaseTest;
import tools.sprig.SprigTests.Color;
import tools.sprig.SprigTests.Shirt;
import tools.sprig.SprigTests.Size;


public class MoreSprigTests extends BaseTest
{
	public static class SizeSprig extends SprigLoader<Size>
	{
		public List<Size> finalL = new ArrayList<Size>();
		
		public SizeSprig()
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
		public void resolve(Entity sourceObj, String fieldName, Entity obj) 
		{
		}

		@Override
		public void saveEntity(Size entity) 
		{
			finalL.add(entity);
		}
	}

	public static class ColorSprig extends SprigLoader<Color>
	{
		public List<Color> finalL = new ArrayList<Color>();
		
		public ColorSprig()
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
		public void saveEntity(Color entity) 
		{
			finalL.add(entity);
		}

		@Override
		public void resolve(Entity sourceObj, String fieldName, Entity obj) 
		{
		}
	}

    public static class ShirtSprig extends SprigLoader<Shirt>
    {
    	public List<Shirt> finalL = new ArrayList<Shirt>();
        public ShirtSprig()
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
        }

		@Override
		public void saveEntity(Shirt entity) 
		{
			finalL.add(entity);
		}
    }
	

	@Test
	public void test()
	{
		String dir = this.getTestFile("sprig\\");
		log(dir);
		Sprig.setDir(dir);
		SizeSprig sizeSprig = new SizeSprig();
		ColorSprig colorSprig = new ColorSprig();
		ShirtSprig shirtSprig = new ShirtSprig();
		
		int n = Sprig.load(sizeSprig, shirtSprig, colorSprig);
		assertEquals(5, n);
		log("done");
		
		assertEquals(2, sizeSprig.finalL.size());
		chkSize(sizeSprig.finalL.get(0), "small", 45);
		chkSize(sizeSprig.finalL.get(1), "medium", 65);

		assertEquals(2, colorSprig.finalL.size());
		assertEquals("red", colorSprig.finalL.get(0).colName);
		assertEquals("blue", colorSprig.finalL.get(1).colName);
		
		assertEquals(1, shirtSprig.finalL.size());
		assertEquals(1, shirtSprig.finalL.get(0).id);
		assertEquals("blue", shirtSprig.finalL.get(0).color.colName);
	}

	private void chkSize(Size size, String expected, int i) 
	{
		assertEquals(expected, size.name);
		assertEquals(i, size.num);
	}


}
