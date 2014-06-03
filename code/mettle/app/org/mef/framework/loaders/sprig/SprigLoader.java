package org.mef.framework.loaders.sprig;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.framework.entities.Entity;

public abstract class SprigLoader<T extends Entity>
{
	protected Class classBeingLoaded;
	//        protected HashMap<Class<?>,SprigIdMap> sprigIdMap;// = new HashMap<Class,SprigId>();
	SprigIdMap sprigIdMap;

	public SprigLoader(Class clazz)
	{
		this.classBeingLoaded = clazz;
		sprigIdMap = new SprigIdMap();
	}
	public List<Entity> parseItems(Map<String,Object> map, LoaderObserver observer)
	{
		List<Entity> resultL = new ArrayList<Entity>();
		List<Map<String,Object>> inputList = (List<Map<String, Object>>) map.get("items");

		Map<String,Object> tmp = new HashMap<String,Object>();
		for(Map<String,Object> params : inputList)
		{
			T obj = null;
			try {
				obj = (T) this.classBeingLoaded.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultL.add(obj);
			parseSprigId(params, obj);

			for(String key : params.keySet())
			{
				String val = (String) params.get(key).toString();
				if (containsVia(val))
				{
//					String data2 = "{'type':'Shirt', 'items':[{'id':1,'color':'<% sprig_record(Color,2)%>'}]}";
					
					System.out.println(key);
					val = val.replace("<%", "");
					val = val.replace("%>", "");
//					String target = "sprig_record(";
					int pos = val.indexOf('(');
					int pos2 = val.indexOf(',', pos);
					int pos3 = val.indexOf(')', pos);
					String op = val.substring(0, pos).trim();
					String namex = val.substring(pos + 1, pos2).trim();
					String valx = val.substring(pos2 + 1, pos3).trim();
					
					//sourceclass,field,obj,target class,field,val,obj
					ViaRef ref = new ViaRef();
					ref.sourceClazz = this.classBeingLoaded;
					ref.sourceField = key;
					ref.sourceObj = obj;
					ref.targetClassName = namex;
					ref.targetField = "sprig_id";
					ref.targetVal = valx;
					observer.addViaRef(ref);
				}
				else
				{
					tmp.put(key, params.get(key));
				}
			}

			//parse all the non-sprig-record ones
			this.parse(obj, tmp);				
		}
		
		return resultL;
	}
	

	public void parseSprigId(Map<String,Object> map, Entity obj)
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
		return (key.startsWith("<%") && key.endsWith("%>"));
	}

	public abstract void parse(T obj, Map<String,Object> map);

	public abstract void resolve(Entity sourceObj, String fieldName, Entity obj);

	public Class getClassBeingLoaded()
	{
		return this.classBeingLoaded;
	}

	protected String getString(Map<String,Object> map, String name)
	{
		String s = (String) map.get(name);
		return s;
	}
	protected Integer getInt(Map<String,Object> map, String name)
	{
		Integer n = (Integer) map.get(name);
		return n;
	}
	protected Long getLong(Map<String,Object> map, String name)
	{
		Long n = 0L;
		Object obj = map.get(name);
		if (obj instanceof Integer)
		{
			Integer intVal = (Integer)obj;
			n = new Long(intVal);
		}
		else
		{
			n = (Long) map.get(name);
		}
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

	//extra stuff
	//can be overridden
	public T findRecord(T target)
	{
		return null;
	}

	//can be overridden
	public void copyAllButId(T src, T dest)
	{
	}

	//	private abstract boolean hasId(T user)
	//	{
	//		return !(user.getId() == null || user.getId() == 0L);
	//	}

	public void saveOrUpdate(List<Entity> L)
	{
		for(Entity obj : L)
		{
			@SuppressWarnings("unchecked")
			T entity = (T)obj;
			T existing = findRecord(entity);
			if (existing == null)
			{
				saveEntity(entity);
			}
			else
			{
				copyAllButId(entity, existing);
				updateUdate(existing);
			}
		}
	}
	//can be overridden
	public void updateUdate(T existing)
	{}
	public abstract void saveEntity(T entity);

}