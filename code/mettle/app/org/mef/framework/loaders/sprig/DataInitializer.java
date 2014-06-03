package org.mef.framework.loaders.sprig;

import java.util.List;

import org.mef.framework.entities.Entity;
import org.mef.framework.loaders.sprig.SprigDataLoader;
import org.mef.framework.loaders.sprig.SprigLoader;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.utils.ResourceReader;

public class DataInitializer extends SfxBaseObj
{
	private SprigDataLoader loader;

	public DataInitializer(SfxContext ctx, SprigDataLoader loader) 
	{
		super(ctx);
		this.loader = loader;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void load(String path, String dir) 
	{
		String json = ResourceReader.readSeedFile(path, dir);
		if (json == null || json.isEmpty()) //fix later!!
		{
			log(String.format("SEED LOAD failed: %s", path));
			return;
		}

		_ctx.log(String.format("SEED %s loading..", path));
		try 
		{
			loader.parseTypes(json);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return;
		}
		
		int totalObjCount = 0;
		List<SprigLoader> loaderL = loader.getAllLoaders();
		for(SprigLoader classDataLoader : loaderL)
		{
			List<Entity> L = loader.resultMap.get(classDataLoader.getClassBeingLoaded());
			classDataLoader.saveOrUpdate(L);
			totalObjCount += L.size();
		}
		
		int n1 = loader.viaL.size();
		boolean b = loader.resolveDeferred();
		if (! b)
		{
			this.addError("resolveDeferred FAILED!");
			return;
		}
		int n2 = loader.viaL.size();
		_ctx.log(String.format("DERERRED resolve: %d of %d", (n1 - n2), n1));
		_ctx.log(String.format("TOTAL objects loaded into db: %d", totalObjCount));
	}


	public List<Entity> getObjectsForClass(Class clazz) 
	{
		return loader.resultMap.get(clazz);
	}
	
}