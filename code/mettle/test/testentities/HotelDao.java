package testentities;
import java.util.ArrayList;
import java.util.List;

import org.mef.framework.dao.IDAO;
import org.mef.framework.fluent.IQueryActionProcessor;
import org.mef.framework.fluent.QStep;
import org.mef.framework.fluent.Query1;
import org.mef.framework.fluent.QueryContext;
import org.mef.framework.sfx.SfxContext;


public class HotelDao implements IDAO
{
	//		public List<Hotel> dataL;
	public QueryContext<Hotel> queryctx; // = new QueryContext<Hotel>();
	List<Hotel> dataL;

	public HotelDao(List<Hotel> dataL, QueryContext<Hotel> queryctx)
	{
		this.queryctx = queryctx;
		queryctx.queryL = new ArrayList<QStep>();
		this.dataL = dataL;
	}

	public Query1<Hotel> query()
	{
		queryctx.queryL = new ArrayList<QStep>();
		return new Query1<Hotel>(queryctx);
	}

//	public void setActionProcessor(IQueryActionProcessor<Hotel> proc) 
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
	public void init(SfxContext ctx) 
	{
		// TODO Auto-generated method stub
		
	}
}