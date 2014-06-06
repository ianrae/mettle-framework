package tools.sprig;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mef.framework.entities.Entity;
import org.mef.framework.loaders.sprig.LoaderObserver;
import org.mef.framework.loaders.sprig.SprigLoader;
import org.mef.framework.loaders.sprig.ViaRef;
import org.mef.framework.utils.ResourceReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tools.BaseTest;
import tools.sprig.SprigTests.Size;

public class MoreSprigTests extends BaseTest
{
	public static class SizeSprig extends SprigLoader<Size>
	{
		public SizeSprig()
		{
			super(Size.class);
		}

		@Override
		public void parse(Size obj, Map<String,Object> map)
		{
			if (map.containsKey("name"))
			{
				obj.name = (String)map.get("name");
			}
			if (map.containsKey("num"))
			{
				obj.num = getInt(map, "num");
			}
			if (map.containsKey("flag"))
			{
				obj.flag = getBoolean(map, "flag");
			}
			if (map.containsKey("createDate"))
			{
				obj.createDate = this.getDate(map, "createDate");
			}
			if (map.containsKey("amount"))
			{
				obj.amount = this.getLong(map, "amount");
			}
			if (map.containsKey("action"))
			{
				obj.action = getChar(map, "action");
			}
		}

		@Override
		public void resolve(Entity sourceObj, String fieldName, Entity obj) 
		{
		}

		@Override
		public void saveEntity(Size entity) 
		{
		}
	}

	public static class Sprig implements LoaderObserver
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

		private int doLoad(SprigLoader...loaders) throws Exception
		{
			int numObj = 0;
			for(SprigLoader loader : loaders)
			{
				String path = "User.json";
				String json = ResourceReader.readSeedFile(path, seedDir);
				if (json == null || json.isEmpty()) //fix later!!
				{
					log(String.format("SEED LOAD failed: %s", path));
					return 0;
				}

				log(String.format("SEED %s loading..", path));
				numObj += parseType(loader, json);
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

		private void log(String s)
		{
			System.out.println(s);
		}
		@Override
		public void addViaRef(ViaRef ref) 
		{
			viaL.add(ref);
		}
	}

	@Test
	public void test()
	{
		String dir = this.getTestFile("sprig\\");
		log(dir);
		Sprig.setDir(dir);
		int n = Sprig.load(new SizeSprig());
		assertEquals(4, n);
	}

}
