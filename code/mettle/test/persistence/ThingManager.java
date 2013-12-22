package persistence;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import clog.TMTests;
import clog.TMTests.ClogEntity;

public class ThingManager<T extends Thing> extends SfxBaseObj
{
	private T loadedObj;
	private boolean loaded; //have attempted a load
	ICLOGDAO dao;
	private boolean dirtyFlag;
	private ClogEntity entity;
	public int saveCounter;
	
	public ThingManager(SfxContext ctx)
	{
		super(ctx);
		dao = (ICLOGDAO) ctx.getServiceLocator().getInstance(ICLOGDAO.class);
	}

	private boolean load(String json) throws Exception 
	{
		WorldParser parser = createWorldParser();
		loadedObj = (T) parser.parse(json);
		
		loaded =  (loadedObj != null);
		return loaded;
	}
	
	public T getLoadedObj()
	{
		return loadedObj;
	}
	
	public T load(Long id) throws Exception
	{
		if (! loaded)
		{
			entity = dao.findById(id);
			load(entity.blob);
		}
		return loadedObj;
	}
	
	public void setDirty()
	{
		dirtyFlag = true;
	}
	public boolean isDirty()
	{
		return dirtyFlag;
	}
	public void saveIfNeeded()
	{
		if (dirtyFlag)
		{
			WorldParser parser = createWorldParser();
			String json = parser.render(loadedObj);
			log("save: " + json);
			entity.blob = json;
			dao.save(entity);
			saveCounter++;
		}
	}
	
	
	protected WorldParser createWorldParser()
	{
		return null;
	}
}