package mef;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.utils.ResourceReader;
import org.mef.tools.mgen.codegen.AppScaffoldCodeGenerator;

import play.Play;

public class OtherTests extends BaseTest
{
	public static class CC
	{
		public String name;
		public String date1;
		public String date2;
		public String compId;
	}
	
	@Test
	public void testResourceRead() throws Exception
	{
		log("--testResource--");
//		Application.resource("public/books.json");
		String dir = this.getCurrentDir("");
		log(dir);
		
		//reads relative to class'es location in jar
		InputStream stream = this.getClass().getResourceAsStream("testfiles/file1.txt");
		assertNotNull(stream);
		List<String> lineL = readInputStream(stream);
		
		for(String s : lineL)
		{
			log(s);
		}
	}
	
	@Test
	public void testMGENResourceRead() throws Exception
	{
		SfxContext ctx = new SfxContext();
		AppScaffoldCodeGenerator gen = new AppScaffoldCodeGenerator(ctx);
		assertNotNull(gen);
		
		//C:\Users\ian\Documents\GitHub\dalgen\code\mettle\.target\org\mef\tools\mgen\resources\dal
		
		InputStream stream = gen.getClass().getResourceAsStream("/mgen/resources/dal/entity.stg");
		assertNotNull(stream);
		
//		URL url = gen.getClass().getClassLoader().getResource("entity.stg");
//		log(url.toString());
		
	}
	
	List<String> readInputStream(InputStream stream) throws Exception
	{
		List<String> lineL = new ArrayList<String>();
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));

        // reads each line
        String l;
        while((l = r.readLine()) != null) 
        {
        	lineL.add(l);
        } 
        stream.close();	
        
        return lineL;
	}
	
	@Test
	public void test() 
	{
		String path = "C:\\ianTools\\play\\play-2.1.2\\samples\\java\\computer-database\\conf\\evolutions\\default";
		ResourceReader r = new ResourceReader();
		String all = r.readSeedFile("2.sql", path);
//		log(all);
	
		ArrayList<CC> L = new ArrayList<OtherTests.CC>();
		String[] ar = all.split("\n");
		for(int i = 0; i < ar.length; i++)
		{
			String line = ar[i];
			
			if (line.indexOf("discontinued") > 0)
			{
//				log(line);
				int pos = line.indexOf("values");
				String s = line.substring(pos);
//				log(s);
				
				String toks = s;
				pos = toks.indexOf('\'');
				s = toks.substring(pos);
				log(s);
				pos = pos + 1;
				
				String name = null;
				int endpos = toks.indexOf('\'', pos);
				if (endpos > 0)
				{
					name = toks.substring(pos, endpos);
					log(name);
				}
				
				String date1 = null;
				String[] xx = toks.split(",");
				for(int k = 0; k < xx.length; k++)
				{
					String ss = xx[k];
					log(String.format("%d: %s", k, ss));
				}
				
				CC cc = new CC();
				cc.name = xx[1].replace('\'', ' ').trim();
				cc.date1 = xx[2].replace('\'', ' ').trim();
				cc.date2 = xx[3].replace('\'', ' ').trim();
				
				int z = xx[4].indexOf(')');
				cc.compId = xx[4].substring(0, z);
				L.add(cc);
			}
		}
		
		log("--------------------------------------");
		for(CC cc : L)
		{
//			log(cc.name);
//			log(cc.date1);
//			log(cc.date2);
//			log(cc.compId);
	
			if (! cc.compId.equals("null"))
			{
				String tmp = "";
				if (cc.date1 != null && ! cc.date1.equals("null"))
				{
					tmp = String.format(" \"introduced\": \"%s\",", cc.date1 );
				}
				
				String tmp2 = "";
				if (cc.date2 != null && ! cc.date2.equals("null"))
				{
					tmp = String.format(" \"discontinued\": \"%s\",", cc.date2 );
				}
				
			    String s = String.format(",{ \"id\": \"0\", \"name\": \"%s\", %s %s \"company\": {\"id\": \"%s\" } }", 
			    		cc.name, tmp, tmp2, cc.compId);
			    log(s);
			}
			else
			{
			    String s = String.format(",{ \"id\": \"0\", \"name\": \"%s\" }", cc.name);
			    log(s);
			}
		}
			
	}

}
