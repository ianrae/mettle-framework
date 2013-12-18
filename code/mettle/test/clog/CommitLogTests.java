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
	
	public static class BugLoader extends SfxBaseObj
	{
		private List<Bug> L;
		
		public BugLoader(SfxContext ctx)
		{
			super(ctx);
		}
		
		public List<Bug> load(int commitId)
		{
			return L;
		}
		
		public void force(List<Bug> L)
		{
			this.L = L;
		}

	}
	
	
	public static class BugManager extends SfxBaseObj
	{
		BugCache cache;
		BugLoader loader;
		private List<Bug> L;
		
		public BugManager(SfxContext ctx, BugCache cache, BugLoader loader)
		{
			super(ctx);
			this.cache = cache;
			this.loader = loader;
		}
		private void logDetails(String title)
		{
			if (L == null)
			{
				log(String.format("%s: miss", title));
			}
			else
			{
				log(String.format("%s: load %d", title, L.size()));
			}
			
		}
		private boolean loadIfNeeded()
		{
			if (L == null)
			{
				L = cache.getFromCache();
				logDetails("cache");
			}
			
			if (L == null)
			{
				L = this.loader.load(0); //!!
				logDetails("cache");
			}
			
			return (L != null);
		}
		
		
		public Bug findById(int id) 
		{
			if (! loadIfNeeded())
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
		init();
		Bug bug = _mgr.findById(1);
		assertNull(bug);
	}

	@Test
	public void testCache() 
	{
		init();
		ArrayList<Bug> L = new ArrayList<CommitLogTests.Bug>();
		Bug bug1 = new Bug(1, "abc");
		L.add(bug1);
		_cache.saveToCache(L);
		
		Bug bug = _mgr.findById(1);
		assertNotNull(bug);
		assertEquals(1, bug.id);

		bug = _mgr.findById(2);
		assertNull(bug);
	}
	
	@Test
	public void testLoad() 
	{
		init();
		ArrayList<Bug> L = new ArrayList<CommitLogTests.Bug>();
		Bug bug1 = new Bug(1, "abc");
		L.add(bug1);
		_loader.force(L);
		
		Bug bug = _mgr.findById(1);
		assertNotNull(bug);
		assertEquals(1, bug.id);

		bug = _mgr.findById(2);
		assertNull(bug);
	}
	
	//------------- helpers ------------
	//vars
	BugCache _cache;
	BugLoader _loader;
	BugManager _mgr;

	private void init()
	{
		this.createContext();
		_cache = new BugCache(_ctx);
		_loader = new BugLoader(_ctx);
		_mgr = new BugManager(_ctx, _cache, _loader);
	}
}

