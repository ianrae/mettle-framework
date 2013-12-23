package clog;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import persistence.ParserHelper;
import persistence.Thing;
import persistence.ThingManager;
import persistence.WorldParser;

public class MDashTests extends BaseJsonTest
{
	public static class Subject extends Thing
	{
		public String name;
	}
	public static class DashItem extends Thing
	{
		public String title;
		public Date createDate;
		public Date modDate;
		public Subject subject;
	}
	public static class DashState extends Thing
	{
		public List<DashItem> items = new ArrayList<DashItem>();
		public List<Subject> subjects = new ArrayList<MDashTests.Subject>();
	}
	
	
	public static class SubjectParser extends BaseParser
	{
		public SubjectParser(SfxContext ctx)
		{
			super(ctx);
		}
		protected Thing createObj()
		{
			return new Subject();
		}
		
		protected void onParse(Thing targetParam) throws Exception
		{
			Subject target = (Subject) targetParam;
			parseId(target);
			target.name = helper.getString("name");
//			target.createDate = helper.getDate("createDate");
//			target.modDate = helper.getDate("modDate");
		}
		
		@Override
		protected void onRender(Thing targetParam) 
		{
			Subject target = (Subject) targetParam;
			obj.put("id", target.id);
			obj.put("title", target.name);
//			obj.put("createDate", ParserHelper.dateToString(target.createDate));
//			obj.put("modDate", ParserHelper.dateToString(target.modDate));
		}
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
			target.modDate = helper.getDate("modDate");
			this.addRef(target, "subject", Subject.class);
		}
		
		@Override
		protected void resolve(String refName, Thing refObj, Object targetParam) throws Exception
		{
			DashItem target = (DashItem) targetParam;
			if (refName.startsWith("subject"))
			{
				target.subject = (Subject)refObj;
			}
		}
		
		@Override
		protected void onRender(Thing targetParam) 
		{
			DashItem target = (DashItem) targetParam;
			obj.put("id", target.id);
			obj.put("title", target.title);
			obj.put("createDate", ParserHelper.dateToString(target.createDate));
			obj.put("modDate", ParserHelper.dateToString(target.modDate));
			this.renderRef("subject", target.subject);
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
			this.addListRef(targetParam, "subjects", Subject.class);
			this.addListRef(targetParam, "items", DashItem.class);
		}
		@Override
		protected void resolve(String refName, Thing refObj, Object targetParam) throws Exception
		{
			DashState target = (DashState) targetParam;
			if (refName.startsWith("items."))
			{
				target.items.add((DashItem) refObj);
			}
			else if (refName.startsWith("subjects."))
			{
				target.subjects.add((Subject)refObj);
			}
		}
		
		@Override
		protected void onRender(Thing targetParam) 
		{
			DashState target = (DashState) targetParam;
			obj.put("id", target.id);
			this.renderListRef("subjects", target.subjects);
			this.renderListRef("items", target.items);
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
			parser.addParser("Subject", new SubjectParser(_ctx));
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
		assertEquals(1, state.items.size());
		assertEquals(2, state.items.get(0).id);
		
		DashItem item2 = new DashItem();
		item2.title = "bob2";
		item2.createDate = Calendar.getInstance().getTime();
		state.items.add(item2);
		assertEquals(0, item2.id);
		
		tm.setDirty();
		tm.saveIfNeeded();
		assertEquals(1, tm.saveCounter); 
		assertEquals(3, item2.id);
		chkErrors(0);
	}
	
	@Test
	public void testSubj() throws Exception
	{
		init();
		_ctx.getServiceLocator().registerSingleton(ICLOGDAO.class, mockDao);
		
		MDashThingManager tm = new MDashThingManager(_ctx);
		
		Long clogId = 1L;
		DashState state = tm.load(clogId);
		assertEquals(1, state.id);
		
		Subject subj = new Subject();
		state.subjects.add(subj);
		subj.name = "math";
		state.items.get(0).subject = subj;
		
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
		String s = "{'rootType':'DashState','root': {'id':1,'items':[{'id':2}] }, 'refs': [ EEE ] }";
		s = s.replace("EEE", "{ 'type': 'DashItem', 'things': [{'id':2, 'title':'bob','createDate':'22 Dec 2013 20:56:05 GMT'}] }");
		return fix(s);
	}

	//------ implement ICLOGDAO ----
	MockClogDAO mockDao;


}
