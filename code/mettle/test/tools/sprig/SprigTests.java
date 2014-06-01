package tools.sprig;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import tools.BaseTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SprigTests extends BaseTest
{
	public static class Size
	{
		public String name;
	}

	public static class Color
	{
		public int id;
		public String colName;
	}

	public static class Shirt
	{
		public int id;
		public Size size;
		public Color color;
		public int colorId;
	}
	
	
	public static class ViaRef
	{
		public Class sourceClazz;
		public String sourceField;
		public Object sourceObj;
		public String targetClassName;
		public String targetField;
		public String targetVal;
		public Object targetObj;
	}
	public interface LoaderObserver
	{
		void addViaRef(ViaRef ref);
	}
	
	public static class SprigIdMap
	{
		public Map<Integer,Object> objMap = new HashMap<Integer, Object>();
	}

    public static abstract class BaseJLoader 
    {
        protected Class classBeingLoaded;
//        protected HashMap<Class<?>,SprigIdMap> sprigIdMap;// = new HashMap<Class,SprigId>();
        SprigIdMap sprigIdMap;
        
        public BaseJLoader(Class clazz)
        {
            this.classBeingLoaded = clazz;
            sprigIdMap = new SprigIdMap();
        }
        public List<Object> parseItems(Map<String,Object> map, LoaderObserver observer)
        {
            List<Object> resultL = new ArrayList<Object>();
            List<Map<String,Object>> inputList = (List<Map<String, Object>>) map.get("items");
            
            
            for(Map<String,Object> tmp : inputList)
            {
            	Object obj = this.parse(tmp);
            	resultL.add(obj);
            	parseSprigId(tmp, obj);
            	
            	for(String key : tmp.keySet())
            	{
            		if (containsVia(key))
            		{
            			System.out.println(key);
            			String[] ar = key.split(" via ");
            			System.out.println(key);
            			String[] arTarget = ar[1].split("\\.");
            			//sourceclass,field,obj,target class,field,val,obj
            			ViaRef ref = new ViaRef();
            			ref.sourceClazz = this.classBeingLoaded;
            			ref.sourceField = ar[0];
            			ref.sourceObj = obj;
            			ref.targetClassName = arTarget[0];
            			ref.targetField = arTarget[1];
            			ref.targetVal = (String) tmp.get(key);
            			observer.addViaRef(ref);
            		}
            	}
            }

            return resultL;
        }
        
        
        public void parseSprigId(Map<String,Object> map, Object obj)
        {
        	String idName = "sprig_id";
            if (map.containsKey(idName))
            {
            	Integer id = (Integer)map.get(idName);
            	
            	SprigIdMap idMap = this.sprigIdMap;
            	idMap.objMap.put(id, obj);
            }
        }
       
       
        private boolean containsVia(String key) 
        {
        	String s = key.replace('\t', ' ');
        	return s.contains(" via ");
		}
        
		public abstract Object parse(Map<String,Object> map);
		
        public void resolve(Object sourceObj, String fieldName, Object obj)
        {}

        public Class getClassBeingLoaded()
        {
            return this.classBeingLoaded;
        }
    }
    public static class SizeJLoader extends BaseJLoader
    {
        public SizeJLoader()
        {
            super(Size.class);
        }
       
        @Override
        public Object parse(Map<String,Object> map)
        {
            Size obj = new Size();
            if (map.containsKey("name"))
            {
                obj.name = (String)map.get("name");
            }

            return obj;
        }
    }
    public static class ColorJLoader extends BaseJLoader
    {
        public ColorJLoader()
        {
            super(Color.class);
        }
       
        @Override
        public Object parse(Map<String,Object> map)
        {
        	Color obj = new Color();
            if (map.containsKey("colName"))
            {
                obj.colName = (String)map.get("colName");
            }

            return obj;
        }
    }
    
    public static class ShirtJLoader extends BaseJLoader
    {
        public ShirtJLoader()
        {
            super(Shirt.class);
        }
       
        @Override
        public Object parse(Map<String,Object> map)
        {
        	Shirt obj = new Shirt();
            if (map.containsKey("id"))
            {
            	Integer n = (Integer) map.get("id");
                obj.id = n;
            }
            //can't load size directly

            return obj;
        }
        
        @Override
        public void resolve(Object sourceObj, String fieldName, Object obj)
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
    }
    
    public static class XLoader implements LoaderObserver
    {
        public Map<Class, List<Object>> resultMap = new HashMap<Class, List<Object>>();
        private Map<String, BaseJLoader> loaderMap = new HashMap<String, BaseJLoader>();
        public List<ViaRef> viaL = new ArrayList<ViaRef>();
		private boolean doingImmediate;
       
        private BaseJLoader getLoader(String className)
        {
        	BaseJLoader loader = null;
           
            if (className.equals("Size"))
            {
                loader = new SizeJLoader();
            }
            else if (className.equals("Color"))
            {
                loader = new ColorJLoader();
            }
            else if (className.equals("Shirt"))
            {
            	loader = new ShirtJLoader();
            }
            else
            {
            }
           
            this.loaderMap.put(className, loader); //only works if each class only loaded once. fix later!!
            return loader;
        }

        @SuppressWarnings("unchecked")
        public void parseTypes(String inputJson) throws Exception
        {
            Map<String,Object> myMap = new HashMap<String, Object>();

            ObjectMapper objectMapper = new ObjectMapper();
            String mapData = inputJson;
            myMap = objectMapper.readValue(mapData, new TypeReference<HashMap<String,Object>>() {});
            System.out.println("Map using TypeReference: "+myMap);

            List<Map<String,Object>> myList = (List<Map<String, Object>>) myMap.get("types");

            for(Map<String,Object> inner : myList)
            {
                String typeName = (String)inner.get("type");
                BaseJLoader loader = this.getLoader(typeName);
//                List<Object> L = (List<Flour>)(List<?>) loader.parseItems(inner, this);
                List<Object> L = loader.parseItems(inner, this);

                List<Object> storedL = resultMap.get(loader.getClassBeingLoaded());
                if (storedL != null)
                {
                    storedL.addAll(L);
                }
                else
                {
                    List<Object> objL = (List<Object>)(List<?>)L;
                    resultMap.put(loader.getClassBeingLoaded(), objL);
                }
            }
        }

		@Override
		public void addViaRef(ViaRef ref) 
		{
			viaL.add(ref);
		}

		
		public boolean resolveImmediate() 
		{
			doingImmediate = true;
			return doResolve();
		}
		
		public boolean resolveDeferred() 
		{
			doingImmediate = false;
			return doResolve();
		}
		
        private boolean doResolve()
        {
            int maxRounds = 100;
            int index = 0;
            while(doOneRound())
            {
                int currentSize = this.viaL.size();
                index++;
                if (index >= maxRounds)
                {
                    return false;
                }
               
            }
            return (this.viaL.size() == 0);
        }

        private boolean doOneRound()
        {
            for(ViaRef vid : viaL)
            {
            	if (doingImmediate)
            	{
	                if (resolveAsImmediateId(vid))
	                {
	                    viaL.remove(vid);
	                    return true;
	                }
            	}
            	else
            	{
	                if (resolveAsDeferredId(vid))
	                {
	                    viaL.remove(vid);
	                    return true;
	                }
            	}
            }
           
            return false;
        }

        private boolean resolveAsImmediateId(ViaRef ref)
        {
        	if (ref.sourceField.startsWith("$")) //deferred (needs DAO)?
        	{
        		return false;
        	}
        	
        	if (ref.targetField.equals("sprig_id"))
        	{
        		System.out.println("####");
        		BaseJLoader loader = this.loaderMap.get(ref.targetClassName);
        		Integer sprigId = Integer.parseInt(ref.targetVal);
        		Object obj = loader.sprigIdMap.objMap.get(sprigId);
        		
        		BaseJLoader sourceLoader = this.loaderMap.get(ref.sourceClazz.getSimpleName());
        		sourceLoader.resolve(ref.sourceObj, ref.sourceField, obj);
        		return true;
        	}
        	
//        	for(ViaRef tmp : this.viaL)
//            {
//                if (tmp.className.equals(vid.targetClassName))
//                {
//                    System.out.println("ho");
//                    if (tmp.fieldName.equals(vid.targetFieldName))
//                    {
//                        if (tmp.fieldValue.equals(vid.targetFieldValue))
//                        {
//                            IJsonLoader loader = this.loaderMap.get(vid.sourceObj.getClass().getSimpleName());
//                           
//                            //!!to handle saltId must detect that fieldName is Integer,int,Long,String, then
//                            //get targetFieldName tmp.obj.targetfieldName as an object (i.e. Integer not int)
//                            loader.resolveVid(vid.sourceObj, vid.fieldName, tmp.obj);
//                            return true;
//                        }
//                    }
//                }
//            }
            return false;
        }


        private boolean resolveAsDeferredId(ViaRef ref)
        {
        	if (! ref.sourceField.startsWith("$")) //immediate?
        	{
        		return false;
        	}
        	
        	if (ref.targetField.equals("sprig_id"))
        	{
        		System.out.println("####D");
        		BaseJLoader loader = this.loaderMap.get(ref.targetClassName);
        		Integer sprigId = Integer.parseInt(ref.targetVal);
        		Object obj = loader.sprigIdMap.objMap.get(sprigId);
        		
        		BaseJLoader sourceLoader = this.loaderMap.get(ref.sourceClazz.getSimpleName());
        		String fieldName = ref.sourceField.substring(1); //remove $
        		sourceLoader.resolve(ref.sourceObj, fieldName, obj);
        		return true;
        	}
        	return false;
        }
    }


	@Test
	public void test() throws Exception
	{
        String root = "{'types': [%s] }";

		String data = "{'type':'Size', 'items':[{'name':'small'},{'name':'medium'}]}";
		data = String.format(root, data);
		data = fix(data);

		log(data);
		XLoader loader = new XLoader();
		loader.parseTypes(data);
		
		List<Object> L = loader.resultMap.get(Size.class);
		assertEquals(2, L.size());
		Size size = (Size) L.get(0);
		assertEquals("small", size.name);
		size = (Size) L.get(1);
		assertEquals("medium", size.name);
	}

	@Test
	public void test2() throws Exception
	{
        String root = "{'types': [%s] }";

		String data1 = "{'type':'Size', 'items':[{'name':'small'},{'name':'medium'}]}";
		String data2 = "{'type':'Shirt', 'items':[{'id':1,'size via Size.name':'medium'},{'id':2,'size via Size.name':'small'}]}";
		String data = String.format(root, data1 + "," + data2);
		data = fix(data);

		log(data);
		XLoader loader = new XLoader();
		loader.parseTypes(data);
		
		List<Object> L = loader.resultMap.get(Size.class);
		assertEquals(2, L.size());
		Size size = (Size) L.get(0);
		assertEquals("small", size.name);
		size = (Size) L.get(1);
		assertEquals("medium", size.name);
		
		assertEquals(2, loader.viaL.size());
		L = loader.resultMap.get(Shirt.class);
		assertEquals(2, L.size());
	}

	@Test
	public void test3() throws Exception
	{
        String root = "{'types': [%s] }";

		String data = "{'type':'Color', 'items':[{'sprig_id':1, 'colName':'red'},{'sprig_id':2, 'colName':'blue'}]}";
		data = String.format(root, data);
		data = fix(data);

		log(data);
		XLoader loader = new XLoader();
		loader.parseTypes(data);
		
		List<Object> L = loader.resultMap.get(Color.class);
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
		String data2 = "{'type':'Shirt', 'items':[{'id':1,'color via Color.sprig_id':'2'}]}";
		String data = String.format(root, data1 + "," + data2);
		data = fix(data);

		log(data);
		XLoader loader = new XLoader();
		loader.parseTypes(data);
		List<Object> L = loader.resultMap.get(Color.class);
		assertEquals(2, L.size());
		Color color = (Color) L.get(0);
		assertEquals("red", color.colName);
		color = (Color) L.get(1);
		assertEquals("blue", color.colName);
		
		boolean b = loader.resolveImmediate();
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
		String data2 = "{'type':'Shirt', 'items':[{'id':1,'$colorId via Color.sprig_id':'2'}]}";
		String data = String.format(root, data1 + "," + data2);
		data = fix(data);

		log(data);
		XLoader loader = new XLoader();
		loader.parseTypes(data);
		List<Object> L = loader.resultMap.get(Color.class);
		assertEquals(2, L.size());
		Color color = (Color) L.get(0);
		assertEquals("red", color.colName);
		color = (Color) L.get(1);
		assertEquals("blue", color.colName);
		
		boolean b = loader.resolveImmediate();
		assertFalse(b);
		
		//simulate dao load
		color.id = 55;
		
		b = loader.resolveDeferred();
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
