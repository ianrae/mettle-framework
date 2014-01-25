package org.mef.tools.mgen.codegen.phase;

import java.util.ArrayList;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.ICodeGenPhase;

public class MasterCodeGenerator extends SfxBaseObj
{
	private ArrayList<ICodeGenPhase> phaseL = new ArrayList<ICodeGenPhase>();
	private String appDir;

	public MasterCodeGenerator(SfxContext ctx) 
	{
		super(ctx);
	}
	public void addPhase(ICodeGenPhase phase)
	{
		phaseL.add(phase);
	}

	public void initialize(String appDirParam) throws Exception
	{
		this.appDir = appDirParam;

		for(ICodeGenPhase phase : phaseL)
		{
			phase.initialize(appDir);
		}
	}

	public boolean run() throws Exception 
	{
		for(ICodeGenPhase phase : phaseL)
		{
			if (! phase.run())
			{
				this.addError(String.format("Phase %s failed", phase.name()));
				return false;
			}
		}

		return true;
	}
}