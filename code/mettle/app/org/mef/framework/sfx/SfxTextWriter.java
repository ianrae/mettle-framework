package org.mef.framework.sfx;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class SfxTextWriter 
{
	private String _path;
	private List<String> _linesL;
	
	public SfxTextWriter()
	{}
	public SfxTextWriter(String path, List<String> linesL)
	{
		_path = path;
		_linesL = linesL;
	}
	public void addLine(String line)
	{
		if (_linesL == null)
		{
			_linesL = new ArrayList<String>();
		}
		_linesL.add(line);
	}
	
	public boolean writeFile()
	{
		boolean result = false;
		File outFile = new File(_path);
		String newline = System.getProperty("line.separator");
		
        Writer output = null;
        try
        {
            output = new BufferedWriter(new FileWriter(outFile));
            for(String line : _linesL)
            {
            	output.write(line);
            	output.write(newline);
            }
            output.close();
            result = true;
        }
        catch(Exception ex)
        {
        	System.out.println(ex);
        }
        return result;
	}
	
	
	//or write one line at a time
	Writer _output = null;
	public boolean open()
	{
		boolean result = false;
		File outFile = new File(_path);
		
		Writer output = null;
        try
        {
            output = new BufferedWriter(new FileWriter(outFile));
            _output = output;
            result = true;
        }
        catch(Exception ex)
        {
        	System.out.println(ex);
        }
        return result;
	}
	public boolean writeLine(String line)
	{
		boolean result = false;
		String newline = System.getProperty("line.separator");
		
        Writer output = _output;
        if (output == null)
        {
        	return false;
        }
        try
        {
           	output.write(line);
           	output.write(newline);
            result = true;
        }
        catch(Exception ex)
        {
        	System.out.println(ex);
        }
        return result;
	}
	public boolean close()
	{
		boolean result = false;
		
        Writer output = _output;
        if (output == null)
        {
        	return false;
        }
        
        try
        {
            output.close();
            result = true;
        }
        catch(Exception ex)
        {
        	System.out.println(ex);
        }
        return result;
	}
	
}
