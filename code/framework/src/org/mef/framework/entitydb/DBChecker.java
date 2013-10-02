package org.mef.framework.entitydb;
import java.util.HashMap;
import java.util.List;


public class DBChecker<T>
{
	public boolean ensureUnique(List<T> L)
	{
		HashMap<T, String> map = new HashMap<T, String>();
		for(T f : L)
		{
			map.put(f, "1");
		}
		
		return (L.size() == map.size());
	}
	
}