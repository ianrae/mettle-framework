package org.mef.framework.fluent;
import java.util.List;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;



public class QueryContext<T> extends SfxBaseObj
{
	public List<QStep> queryL;
	public IQueryActionProcessor<T> proc;
	
	public QueryContext(SfxContext ctx)
	{
		super(ctx);
	}
	
	public SfxContext getSfxContext()
	{
		return _ctx;
	}
	
}