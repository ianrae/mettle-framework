package clog;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.mef.framework.sfx.SfxContext;

import clog.JsonTests.AirportParser;
import clog.JsonTests.BigAirport;
import clog.JsonTests.BigAirportParser;
import clog.JsonTests.Gate;
import clog.JsonTests.GateParser;
import clog.JsonTests.MultiAirportParser;
import clog.TMTests.AirportThingManager;

import persistence.BaseParser;
import persistence.ICLOGDAO;
import persistence.Thing;
import persistence.ThingManager;
import persistence.WorldParser;

public class MDashTests extends BaseJsonTest
{
	public static class DashItem extends Thing
	{
		public String title;
		public Date createDate;
	}
	public static class DashState extends Thing
	{
		public DashItem item;
	}
	
	public static class DashItemParser extends BaseParser
	{
		public DashItemParser(SfxContext ctx)
		{
			super(ctx);
		}
		protected Thing createObj()
		{
			return new DashItem();
		}
		
		protected void onParse(Thing targetParam) throws Exception
		{
			DashItem target = (DashItem) targetParam;
			parseId(target);
			target.title = helper.getString("title");
			target.createDate = helper.getDate("createDate");
		}
		
		@Override
		protected void onRender(Thing targetParam) 
		{
			DashItem target = (DashItem) targetParam;
			obj.put("id", target.id);
			obj.put("title", target.title);
			obj.put("createDate", Thing.dateToString(target.createDate));
		}
	}
	public static class DashStateParser extends BaseParser
	{
		public DashStateParser(SfxContext ctx)
		{
			super(ctx);
		}
		protected Thing createObj()
		{
			return new DashState();
		}
		
		protected void onParse(Thing targetParam) throws Exception
		{
			DashState target = (DashState) targetParam;
			parseId(target);
			this.addRef(targetParam, "item", DashItem.class);
		}
		@Override
		protected void resolve(String refName, Thing refObj, Object targetParam) throws Exception
		{
			if (refName.equals("item"))
			{
				DashState target = (DashState) targetParam;
				target.item = (DashItem) refObj;
			}
		}
		
		@Override
		protected void onRender(Thing targetParam) 
		{
			DashState target = (DashState) targetParam;
			obj.put("id", target.id);
			this.renderRef("item", target.item);
		}
	}
	
	
	public static class MDashThingManager extends ThingManager<DashState>
	{
		public MDashThingManager(SfxContext ctx)
		{
			super(ctx);
		}

		@Override
		protected WorldParser createWorldParser()
		{
			WorldParser parser = new WorldParser(_ctx);
			parser.addParser("DashState", new DashStateParser(_ctx));
			parser.addParser("DashItem", new DashItemParser(_ctx));
			return parser;
		}
	}
	
	@Test
	public void test() throws Exception
	{
		init();
		_ctx.getServiceLocator().registerSingleton(ICLOGDAO.class, mockDao);
		
		MDashThingManager tm = new MDashThingManager(_ctx);
		
		Long clogId = 1L;
		DashState state = tm.load(clogId);
		assertEquals(1, state.id);
		assertEquals(2, state.item.id);
		
		tm.setDirty();
		tm.saveIfNeeded();
		assertEquals(1, tm.saveCounter); 
		chkErrors(0);
	}
	
	
	@Override 
	protected void init()
	{
		super.init();
		mockDao = new MockClogDAO(getJson());
	}
	private String getJson() 
	{
		String s = "{'rootType':'DashState','root': {'id':1,'item':{'id':2}}, 'refs': [ EEE ] }";
		s = s.replace("EEE", "{ 'type': 'DashItem', 'things': [{'id':2, 'title':'bob','createDate':'22 Dec 2013 20:56:05 GMT'}] }");
//		String s = "{'rootType':'DashState','root': {'id':1,'title':'bob','createDate':'22 Dec 2013 20:56:05 GMT'}, 'refs': [ ] }";
		return fix(s);
	}


	//------ implement ICLOGDAO ----
	MockClogDAO mockDao;


}
