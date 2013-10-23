package org.mef.framework.sfx;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.FileUtils;

public class SfxFileUtils 
{
	public int _errorCount = 0;
	public boolean _fileActionsEnabled = false;

	public void deleteFile(File f) throws Exception
	{
		log("DEL: " + f.getAbsolutePath());
		
		if (_fileActionsEnabled)
		{
			boolean b = f.delete();
			if (! b)
			{
				_errorCount++;
			}
		}
	}
	public void renameFile(String oldPath, String newPath) throws Exception 
	{
		File f = new File(oldPath);
		File dest = new File(newPath);
		
		log("RENAME: " + oldPath + " to " + newPath);
		if (_fileActionsEnabled)
		{
			f.renameTo(dest);
		}
	}

	public void writeFile(String path, SfxTextBuilder writer) throws Exception
	{
		log("WRITE: " + path);
		
		if (_fileActionsEnabled)
		{
		    //use buffering
		    Writer output = null;
		    try 
		    {
			    output = new BufferedWriter(new FileWriter(path));
		    	output.write(writer.getText());
		    }
		    finally 
		    {
				output.close();
		    }
		}
	}
	
	public boolean fileExists(String path) throws Exception
	{
			File theFile = new File(path);
	
			return theFile.exists();
	}
	
	public boolean safeFileExists(String path)
	{
		boolean result = false;
		try
		{
			File theFile = new File(path);
			result = theFile.exists();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return result;
	}
	
	
	public String PathCombine(String path1, String path2)
	{
		String result = new File(path1, path2).toString();
		return result;
	}	
	public String PathCombine3(String path1, String path2, String path3)
	{
		String result = PathCombine(path1, path2);
		result = PathCombine(result, path3);
		return result;
	}	
	
	private void log(String s)
	{
		System.out.println(s);
	}
	
	public String getCurrentDir()
	{
		String dir = "";
		try {
			File f2 = new File(".");
			dir = f2.getCanonicalPath();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return dir;
	}
	
	//uses apache commons-io lib
	public boolean fileCopy(String srcPath, String destPath)
	{
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		boolean result = false;
		try {
			FileUtils.copyFile(srcFile, destFile, true);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
