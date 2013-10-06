package mef.presenters;

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
		int start = 0;
		int end = (start + pageSize <= L.size()) ? start + pageSize : L.size();
		List<Computer> someL = L.subList(start, end);
		return someL;
	}

	@Override
	public int getPageIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalPageCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPrev() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Page<Computer> next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Computer> prev() {
		// TODO Auto-generated method stub
		return null;
	}

}
