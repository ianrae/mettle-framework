package clog;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mef.framework.entities.Entity;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

import tools.BaseTest;

public class CommitLogTests extends BaseTest
{
	public static class Bug extends Entity
	{
		public int id;
		public String title;
		
		public Bug(int id, String title)
		{
			this.id = id;
			this.title = title;
		}
	}
	
	public static class BugCache extends SfxBaseObj
	{
		private List<Bug> L;
		
		public BugCache(SfxContext ctx)
		{
			super(ctx);
		}
		
		public List<Bug> getFromCache()
		{
			return L;
		}
		
		public void saveToCache(List<Bug> L)
		{
			this.L = L;
		}

	}
	
	
	public static class BugManager extends SfxBaseObj
	{
		BugCache cache;
		private List<Bug> L;
		
		public BugManager(SfxContext ctx, BugCache cache)
		{
			super(ctx);
			this.cache = cache;
		}

		private boolean loadCacheIfNeeded()
		{
			if (L == null)
			{
				L = cache.getFromCache();
			}
			
			return (L != null);
		}
		public Bug findById(int id) 
		{
			if (! loadCacheIfNeeded())
			{
				return null;
			}
			
			for(Bug bug : L)
			{
				if (bug.id == id)
				{
					return bug;
				}
			}
			return null;
		}
	}
	
	
	@Test
	public void testEmptyCache() 
	{
		this.createContext();
		BugCache cache = new BugCache(_ctx);
		BugManager mgr = new BugManager(_ctx, cache);
		
		Bug bug = mgr.findById(1);
		assertNull(bug);
	}

	@Test
	public void testCache() 
	{
		this.createContext();
		ArrayList<Bug> L = new ArrayList<CommitLogTests.Bug>();
		Bug bug1 = new Bug(1, "abc");
		L.add(bug1);
		
		BugCache cache = new BugCache(_ctx);
		cache.saveToCache(L);
		
		BugManager mgr = new BugManager(_ctx, cache);
		
		Bug bug = mgr.findById(1);
		assertNotNull(bug);
		assertEquals(1, bug.id);

		bug = mgr.findById(2);
		assertNull(bug);
	}
}
