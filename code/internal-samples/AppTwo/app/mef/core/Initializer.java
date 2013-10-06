package mef.core;

import java.util.List;

import mef.gen.AllKnownDAOs_GEN;

import org.mef.framework.dao.IDAO;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.utils.ResourceReader;

import play.Logger;

//import mef.gen.AllKnownDAOs_GEN;

public class Initializer 
{
	public static SfxContext theCtx;

	public static void init()
	{
		if (theCtx == null)
		{
			theCtx = createContext(false); //fix later!! not thread-safe
			Logger.info("==seed==");
			Initializer.loadSeedData(theCtx);
		}
	}
	
	public static SfxContext createContext(boolean createMocks)
	{
		SfxContext ctx = new SfxContext();
		AllKnownDAOs_GEN knownDAOs = new AllKnownDAOs_GEN();
		knownDAOs.registerDAOs(ctx, createMocks);
		return ctx;
	}
	
	public static void loadSeedData(SfxContext ctx)
	{
		String json = ResourceReader.readSeedFile("json1.txt");
		EntityLoader loader = new EntityLoader(ctx);
		try {
			loader.loadAll(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
