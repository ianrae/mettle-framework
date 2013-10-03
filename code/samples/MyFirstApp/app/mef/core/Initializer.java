package mef.core;

import java.util.List;

import org.mef.framework.sfx.SfxContext;
import org.mef.framework.utils.ResourceReader;

import mef.gen.AllKnownDALs_GEN;

public class Initializer 
{
	public static SfxContext createContext(boolean createMocks)
	{
		SfxContext ctx = new SfxContext();
		AllKnownDALs_GEN knownDALs = new AllKnownDALs_GEN();
		knownDALs.registerDALs(ctx, createMocks);
		return ctx;
	}
	
	public static void loadSeedData(SfxContext ctx)
	{
		String json = ResourceReader.readSeedFile("json-user1.txt");
		EntityLoader loader = new EntityLoader(ctx);
		try {
			loader.loadUser(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
