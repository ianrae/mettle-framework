package org.mef.framework.loaders.sprig;

import java.util.ArrayList;
import java.util.Date;
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

		for(Map<String,Object> tmp : inputList)
		{
			T obj = (T)this.parse(tmp);
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
		String s = key.replace('\t', ' ');
		return s.contains(" via ");
	}

	public abstract Entity parse(Map<String,Object> map);

	public void resolve(Entity sourceObj, String fieldName, Entity obj)
	{}

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
	public abstract T findRecord(T target);

	public abstract void copyAllButId(T src, T dest);

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
	public abstract void updateUdate(T existing);
	public abstract void saveEntity(T entity);

}