package tools.sprig;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mef.framework.loaders.sprig.EntityDataLoader;
import org.mef.sprig.Sprig;

import tools.BaseTest;
import tools.sprig.MoreSprigTests.ColorSprig;
import tools.sprig.SprigTests.Shirt;


public class SprigFailTests extends BaseTest
{
    public static class BadShirtSprig extends EntityDataLoader<Shirt>
    {
    	public List<Shirt> finalL = new ArrayList<Shirt>();
        public BadShirtSprig()
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
        	throw new IllegalStateException("die!");
        }

		@Override
		public void save(Shirt entity) 
		{
			finalL.add(entity);
		}
    }
	

	@Test(expected=IllegalStateException.class)
	public void test() throws Exception
	{
		String dir = this.getTestFile("sprig\\");
		log(dir);
		Sprig.setDir(dir);
		ColorSprig colorSprig = new ColorSprig();
		BadShirtSprig shirtSprig = new BadShirtSprig();
		
		int n = Sprig.load(shirtSprig, colorSprig);
		assertEquals(5, n);
		log("done");
		
	}


}
