package tools.sprig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class SprigLoader 
    {
        protected Class classBeingLoaded;
//        protected HashMap<Class<?>,SprigIdMap> sprigIdMap;// = new HashMap<Class,SprigId>();
        SprigIdMap sprigIdMap;
        
        public SprigLoader(Class clazz)
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
        
        protected Integer getInt(Map<String,Object> map, String name)
        {
        	Integer n = (Integer) map.get(name);
        	return n;
        }
        protected Long getLong(Map<String,Object> map, String name)
        {
        	Long n = (Long) map.get(name);
        	return n;
        }
        protected Boolean getBoolean(Map<String,Object> map, String name)
        {
        	Boolean b = (Boolean) map.get(name);
        	return b;
        }
        protected Date getDate(Map<String,Object> map, String name)
        {
        	Long n = Long.parseLong((String) map.get(name));
        	Date dt = new Date(n);
        	return dt;
        }
        protected Character getChar(Map<String,Object> map, String name)
        {
        	String s = (String)map.get(name);
        	Character ch = (s.isEmpty()) ? 0 : s.charAt(0);
        	return ch;
        }
    }