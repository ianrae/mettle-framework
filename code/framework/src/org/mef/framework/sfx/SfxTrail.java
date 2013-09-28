package org.mef.framework.sfx;

/**
 * A trail is a series of strings, separated by ';'.  Used to verify
 * that a series of actions took place in a unit test.
 *
 */

public class SfxTrail
{
	private String _trail = "";
	
	public SfxTrail()
	{}
	
	public void add(String s)
	{
		if (_trail.isEmpty())
		{
			_trail = s;
		}
		else
		{
			_trail += ";" + s;
		}
	}
	
	public String getTrail() 
	{
		return _trail;
	}
	public String toString() 
	{
		return _trail;
	}
}

