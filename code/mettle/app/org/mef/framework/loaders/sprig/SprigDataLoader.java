package org.mef.framework.loaders.sprig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.framework.entities.Entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SprigDataLoader implements LoaderObserver
{
	public Map<Class, List<Entity>> resultMap = new HashMap<Class, List<Entity>>();
	private Map<String, SprigLoader> loaderMap = new HashMap<String, SprigLoader>();
	public List<ViaRef> viaL = new ArrayList<ViaRef>();
	private boolean doingImmediate;

	@SuppressWarnings("rawtypes")
	public SprigLoader getLoader(String className)
	{
		SprigLoader loader = doGetLoader(className);
		if (loader != null)
		{
			this.loaderMap.put(className, loader); //only works if each class only loaded once. fix later!!
		}
		return loader;
	}

	@SuppressWarnings("rawtypes")
	public List<SprigLoader> getAllLoaders()
	{
		List<SprigLoader> L = new ArrayList<SprigLoader>();
		
		for(String className : loaderMap.keySet())
		{
			SprigLoader loader = loaderMap.get(className);
			L.add(loader);
		}
		return L;
	}

	
	protected abstract SprigLoader doGetLoader(String className);

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
			@SuppressWarnings("rawtypes")
			SprigLoader loader = this.getLoader(typeName);
			//                List<Object> L = (List<Flour>)(List<?>) loader.parseItems(inner, this);
			List<Entity> L = loader.parseItems(inner, this);

			List<Entity> storedL = resultMap.get(loader.getClassBeingLoaded());
			if (storedL != null)
			{
				storedL.addAll(L);
			}
			else
			{
				List<Entity> objL = (List<Entity>)(List<?>)L;
				resultMap.put(loader.getClassBeingLoaded(), objL);
			}
		}
	}

	@Override
	public void addViaRef(ViaRef ref) 
	{
		viaL.add(ref);
	}


//	public boolean resolveImmediate() 
//	{
////		doingImmediate = true;
////		doResolve();
//		return true;
//	}

	public boolean resolveDeferred() 
	{
//		doingImmediate = false;
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
			if (resolveAsDeferredId(vid))
			{
				viaL.remove(vid);
				return true;
			}
		}

		return false;
	}

	private boolean resolveAsDeferredId(ViaRef ref)
	{
		if (ref.targetField.equals("sprig_id"))
		{
			System.out.println("####D");
			SprigLoader loader = this.loaderMap.get(ref.targetClassName);
			Integer sprigId = Integer.parseInt(ref.targetVal);
			Entity obj = loader.sprigIdMap.objMap.get(sprigId);

			SprigLoader sourceLoader = this.loaderMap.get(ref.sourceClazz.getSimpleName());
			String fieldName = ref.sourceField; //.substring(1); //remove $
			sourceLoader.resolve(ref.sourceObj, fieldName, obj);
			return true;
		}
		return false;
	}
}