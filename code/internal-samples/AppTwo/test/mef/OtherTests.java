package mef;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.mef.framework.utils.ResourceReader;

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
			    String s = String.format(",{ \"id\": \"0\", \"name\": \"%s\",  \"company\": {\"id\": \"%s\" } }", cc.name, cc.compId);
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
