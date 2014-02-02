package tools.testfiles;
import java.util.ArrayList;
import java.util.List;

import org.mef.framework.binder.IFormBinder;
import org.mef.framework.dao.IDAO;
import org.mef.framework.fluent.IQueryActionProcessor;
import org.mef.framework.fluent.QStep;
import org.mef.framework.fluent.Query1;
import org.mef.framework.fluent.QueryContext;
import org.mef.framework.sfx.SfxContext;

import testentities.StreetAddress;


public class StreetAddressDao implements IDAO
{
	//		public List<Hotel> dataL;
	public QueryContext<StreetAddress> queryctx; // = new QueryContext<StreetAddress>();
	List<StreetAddress> dataL;

	public StreetAddressDao(List<StreetAddress> dataL, QueryContext<StreetAddress> queryctx)
	{
		this.queryctx = queryctx;
		queryctx.queryL = new ArrayList<QStep>();
		this.dataL = dataL;
		
		
	}

	public Query1<StreetAddress> query()
	{
		queryctx.queryL = new ArrayList<QStep>();
		return new Query1<StreetAddress>(queryctx);
	}

//	public void setActionProcessor(IQueryActionProcessor<StreetAddress> proc) 
//	{
//		queryctx.proc = proc;
//	}

	@Override
	public int size() 
	{
		return dataL.size();
	}

	@Override
	public void delete(long id) 
	{
		throw new RuntimeException("no del yet");
	}

	@Override
	public void updateFrom(IFormBinder binder) 
	{
		throw new RuntimeException("no del yet");
	}

	@Override
	public void init(SfxContext ctx) {
		// TODO Auto-generated method stub
		
	}
}