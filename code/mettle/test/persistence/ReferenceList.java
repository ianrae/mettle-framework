package persistence;

import java.util.ArrayList;
import java.util.List;

public class ReferenceList
{
	public ArrayList<ReferenceDesc> refL = new ArrayList<ReferenceDesc>();

	public List<String> getAllTypes()
	{
		ArrayList<String> nameL = new ArrayList<String>();
		for(ReferenceDesc dd : refL)
		{
			String s = dd.refClass.getSimpleName();
			if (! nameL.contains(s))
			{
				nameL.add(s);
			}
		}
		return nameL;
	}
	public List<ReferenceDesc> getListFor(String className)
	{
		ArrayList<ReferenceDesc> resultL = new ArrayList<ReferenceDesc>();
		for(ReferenceDesc desc : refL)
		{
			String s = desc.refClass.getSimpleName();
			if (s.equals(className))
			{
				resultL.add(desc);
			}
		}
		return resultL;
	}
}