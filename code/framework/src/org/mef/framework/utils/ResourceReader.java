package org.mef.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public class ResourceReader 
{
	public static String readSeedFile(String filename)
	{
		String currentDir = new File(".").getAbsolutePath();
		String path = FilenameUtils.concat(currentDir, "conf\\mef\\seed");
		path = FilenameUtils.concat(path, filename);
		return readFile(path);
	}
	
	public static String readFile(String path) 
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
	    try {
	    	br = new BufferedReader(new FileReader(path));
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append('\n');
	            line = br.readLine();
	        }
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	    	if (br != null)
	    	{
	    		try {
					br.close();
				} catch (IOException e) {
//					e.printStackTrace();
				}
	    	}
	    }		
	    String everything = sb.toString();
	    return everything;
	}
}
