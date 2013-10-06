package mef.presenters;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.Page;

public class MyPage<Computer> implements Page<Computer>
{
	List<Computer> L;
	int pageSize;
	int pageNum;
	
	public MyPage(List<Computer> L, int pageSize, int pageNum)
	{
		this.L = L;
		this.pageSize = pageSize;
		this.pageNum = pageNum;
	}
	
	@Override
	public String getDisplayXtoYofZ(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Computer> getList() 
	{
		int start = (pageNum - 1) * pageSize;
		int end = (start + pageSize <= L.size()) ? start + pageSize : L.size();
		
		if (start < 0 || start > (L.size() - 1) || end > L.size())
		{
			return new ArrayList<Computer>(); //empty
		}
		List<Computer> someL = L.subList(start, end);
		return someL;
	}

	@Override
	public int getPageIndex() 
	{
		return this.pageNum - 1; //0-based
	}

	@Override
	public int getTotalPageCount() 
	{
		int n = L.size() / this.pageSize;
		int rem = L.size() % this.pageSize;
		
		return (rem > 0) ? n + 1 : n;
	}

	@Override
	public int getTotalRowCount() 
	{
		return L.size();
	}

	@Override
	public boolean hasNext() 
	{
		return (this.pageNum < this.getTotalPageCount());
	}

	@Override
	public boolean hasPrev() 
	{
		return (this.pageNum > 1);
	}

	@Override
	public Page<Computer> next() 
	{
		Page<Computer> pg = new MyPage<Computer>(L, pageSize, pageNum + 1);
		return pg;
	}

	@Override
	public Page<Computer> prev() 
	{
		Page<Computer> pg = new MyPage<Computer>(L, pageSize, pageNum - 1);
		return pg;
	}

}
