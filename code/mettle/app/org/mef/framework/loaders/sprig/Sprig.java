package org.mef.framework.loaders.sprig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.framework.entities.Entity;
import org.mef.framework.utils.ResourceReader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Sprig implements LoaderObserver
{
	private static String seedDir = "conf/mef/seed";

	public static void setDir(String dir)
	{
		seedDir = dir;
	}
	public static int load(SprigLoader...sprigs)
	{
		int n = 0;
		Sprig self = new Sprig();
		try {
			n = self.doLoad(sprigs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n;
	}

	private Map<Class, List<Entity>> resultMap = new HashMap<Class, List<Entity>>();
	private Map<String, SprigLoader> loaderMap = new HashMap<String, SprigLoader>();
	private List<ViaRef> viaL = new ArrayList<ViaRef>();

	public Sprig()
	{
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int doLoad(SprigLoader...loaders) throws Exception
	{
		int numObj = 0;
		for(SprigLoader loader : loaders)
		{
			String className = loader.getNameOfClassBeingLoaded();
			String path = className + ".json";
			String json = ResourceReader.readSeedFile(path, seedDir);
			if (json == null || json.isEmpty()) //fix later!!
			{
				log(String.format("SEED LOAD failed: %s", path));
				return 0;
			}

			log(String.format("SEED %s loading..", path));
			numObj += parseType(loader, json);
			
			this.loaderMap.put(className, loader);
		}

		List<SprigLoader> sortedL = tsort(loaders);

		log("and save..");
		List<SprigLoader> soFarL = new ArrayList<SprigLoader>();

		for(SprigLoader loader : sortedL)
		{
			log(String.format("SEED saving %s..", loader.getNameOfClassBeingLoaded()));
			List<Entity> L = this.resultMap.get(loader.getClassBeingLoaded());
			loader.saveOrUpdate(L);

			soFarL.add(loader);
			doResolve(soFarL);
		}
		
		if (viaL.size() > 0)
		{
			log(String.format("SEED FAILED with %d unresolved sprig_id references", viaL.size()));
		}

		return numObj;
	}
	private int parseType(SprigLoader loader, String inputJson) throws Exception
	{
		Map<String,Object> myMap = new HashMap<String, Object>();

		ObjectMapper objectMapper = new ObjectMapper();
		String mapData = inputJson;
		myMap = objectMapper.readValue(mapData, new TypeReference<HashMap<String,Object>>() {});
		System.out.println("Map using TypeReference: "+myMap);

		List<Map<String,Object>> myList = (List<Map<String, Object>>) myMap.get("records");

		//                List<Object> L = (List<Flour>)(List<?>) loader.parseItems(inner, this);
		List<Entity> L = loader.doparseItems(this, myList);

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
		return L.size();
	}
	
	@SuppressWarnings("rawtypes")
	private List<SprigLoader> tsort(SprigLoader[] loaders)
	{
		List<TSortNode> L = new ArrayList<TSortNode>();
		for(SprigLoader loader : loaders)
		{
			TSortNode node = new TSortNode(loader);
			L.add(node);
		}
		
		
		for(ViaRef via : viaL)
		{
			SprigLoader srcLoader = findByClass(loaders, via.sourceClazz);
			SprigLoader targetLoader = findByClassName(loaders, via.targetClassName);
			
			TSortNode node1 = null;
			TSortNode node2 = null;
			for(TSortNode tmp : L)
			{
				if (tmp.obj == srcLoader)
				{
					node1 = tmp;
				}
				else if (tmp.obj == targetLoader)
				{
					node2 = tmp;
				}
			}

			node1.addDep(node2);
		}
		
		List<TSortNode> sortL = TopologicalSort.sort(L);
		
		List<SprigLoader> sortedL = new ArrayList<SprigLoader>();
		for(TSortNode node : sortL)
		{
			sortedL.add((SprigLoader) node.obj);
		}
		return sortedL;
	}

	private SprigLoader findByClass(SprigLoader[] loaders, Class clazz) 
	{
		for(SprigLoader loader : loaders)
		{
			if (loader.getClassBeingLoaded() == clazz)
			{
				return loader;
			}
		}
		return null;
	}
	private SprigLoader findByClassName(SprigLoader[] loaders, String className) 
	{
		for(SprigLoader loader : loaders)
		{
			if (loader.getNameOfClassBeingLoaded().equals(className))
			{
				return loader;
			}
		}
		return null;
	}
	private void log(String s)
	{
		System.out.println(s);
	}
	@Override
	public void addViaRef(ViaRef ref) 
	{
		viaL.add(ref);
	}


	private boolean doResolve(List<SprigLoader> soFarL)
	{
		while(doOneRound(soFarL))
		{
		}
		return (this.viaL.size() == 0);
	}

	private boolean doOneRound(List<SprigLoader> soFarL)
	{
		for(ViaRef vid : viaL)
		{
			for(SprigLoader loader : soFarL)
			{
				//once a loaders objects have been saved to the db, we can resolve references to them
				String className = loader.getNameOfClassBeingLoaded();
				if (className.equals(vid.targetClassName))
				{
					log("a " + className);
					if (resolveAsDeferredId(vid))
					{
						viaL.remove(vid);
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean resolveAsDeferredId(ViaRef ref)
	{
		if (ref.targetField.equals("sprig_id"))
		{
			SprigLoader loader = this.loaderMap.get(ref.targetClassName);
			Integer sprigId = Integer.parseInt(ref.targetVal);
			Entity obj = loader.getSprigIdMap().objMap.get(sprigId);

			SprigLoader sourceLoader = this.loaderMap.get(ref.sourceClazz.getSimpleName());
			String fieldName = ref.sourceField; //.substring(1); //remove $
			sourceLoader.resolve(ref.sourceObj, fieldName, obj);
			return true;
		}
		return false;
	}
}