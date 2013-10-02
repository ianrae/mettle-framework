package org.mef.framework.entitydb;
import java.util.ArrayList;
import java.util.List;


public class Query<T>
{
	private List<T> resultL = new ArrayList<T>();
	EntityDB<T> db = new EntityDB<T>();
	
	public void add(List<T> L)
	{
		resultL.addAll(L);
	}
	
	public int size()
	{
		return resultL.size();
	}
	
	public void union(List<T> L)
	{
		resultL = db.union(resultL, L);
	}

	public void intersect(List<T> L)
	{
		resultL = db.intersection(resultL, L);
	}
}