package org.mef.framework.sfx;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;

public class SfxTextReader {

	public SfxTextReader()
	{}
	
	public String readFileAsSingleString(String path)
	{
		String newline = System.getProperty("line.separator");
		
		ArrayList<String> L = this.readFile(path);
		StringBuffer sb = new StringBuffer();
		for(String s: L)
		{
			sb.append(s);
			sb.append(newline);
		}
		return sb.toString();
	}
	
	public ArrayList<String> readFile(String path)
	{
		ArrayList<String> L = new ArrayList<String>();

		BufferedReader input = null;
		try
		{
			try
			{
				input =  new BufferedReader(new FileReader(path));
				String line = null; //not declared within while loop
				while (( line = input.readLine()) != null)
				{
					L.add(line);
				}
			}
			finally
			{
				if (input != null)
				{
					input.close();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

        return L;
	}
	
	public String readUTF8File(String path)
	{
		return readUTF8File(path, true);
	}
	/**
	 * Read a UTF-8 text file and return the contents as a String.
	 * @param path
	 * @return
	 */
	public String readUTF8File(String path, boolean logExceptions)
	{
		StringBuffer result = new StringBuffer();
		boolean ok = false;
		Reader reader = null;
		try
		{
			try
			{
				InputStream inputStream = new FileInputStream(path);
				
				String charsetName = "UTF-8";
				Charset charset = Charset.forName(charsetName);
				CharsetDecoder decoder = charset.newDecoder();
				decoder.onMalformedInput(CodingErrorAction.REPORT);
				decoder.onUnmappableCharacter(CodingErrorAction.REPORT);				
				
				reader      = new InputStreamReader(inputStream, decoder); //"UTF-8");
				
				
				int data = reader.read();
				while(data != -1){
				    char theChar = (char) data;
				    result.append(theChar);
				    
				    data = reader.read();
				}
				ok = true;
			}
			finally
			{
				if (reader != null)
				{
					reader.close();
				}
			}
		}
		catch(Exception e)
		{
			if (logExceptions)
			{
				e.printStackTrace();
			}
		}

        return (ok) ? result.toString() : "";
	}	
	
}
