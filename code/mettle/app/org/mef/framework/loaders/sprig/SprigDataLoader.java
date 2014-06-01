package org.mef.framework.loaders.sprig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SprigDataLoader implements LoaderObserver
{
	public Map<Class, List<Object>> resultMap = new HashMap<Class, List<Object>>();
	private Map<String, SprigLoader> loaderMap = new HashMap<String, SprigLoader>();
	public List<ViaRef> viaL = new ArrayList<ViaRef>();
	private boolean doingImmediate;

	private SprigLoader getLoader(String className)
	{
		SprigLoader loader = doGetLoader(className);
		if (loader != null)
		{
			this.loaderMap.put(className, loader); //only works if each class only loaded once. fix later!!
		}
		return loader;
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
			SprigLoader loader = this.getLoader(typeName);
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
			SprigLoader loader = this.loaderMap.get(ref.targetClassName);
			Integer sprigId = Integer.parseInt(ref.targetVal);
			Object obj = loader.sprigIdMap.objMap.get(sprigId);

			SprigLoader sourceLoader = this.loaderMap.get(ref.sourceClazz.getSimpleName());
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
			SprigLoader loader = this.loaderMap.get(ref.targetClassName);
			Integer sprigId = Integer.parseInt(ref.targetVal);
			Object obj = loader.sprigIdMap.objMap.get(sprigId);

			SprigLoader sourceLoader = this.loaderMap.get(ref.sourceClazz.getSimpleName());
			String fieldName = ref.sourceField.substring(1); //remove $
			sourceLoader.resolve(ref.sourceObj, fieldName, obj);
			return true;
		}
		return false;
	}
}