package tools;

import java.util.ArrayList;

import org.apache.commons.lang.NotImplementedException;
import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.CodeGenerator;
import org.mef.tools.mgen.codegen.ICodeGenPhase;
import org.mef.tools.mgen.codegen.ICodeGenerator;
import org.mef.tools.mgen.parser.DalGenXmlParser;

public class CodeGeneratorPhase extends CodeGenerator implements ICodeGenPhase
{
	private ArrayList<ICodeGenerator> genL = new ArrayList<ICodeGenerator>();
	private String name;
	public CodeGeneratorPhase(SfxContext ctx, String name) 
	{
		super(ctx);
		this.name = name;
	}

	@Override
	public String name() 
	{
		return name;
	}

	@Override
	public void add(ICodeGenerator gen) 
	{
		genL.add(gen);
	}
	
	@Override
	public void initialize(String appDir) throws Exception 
	{
		init(appDir);
		for(ICodeGenerator gen : genL)
		{
			gen.initialize(appDir);
		}
	}
	

	@Override
	public boolean run() throws Exception
	{
		for(ICodeGenerator gen : genL)
		{
			if (! gen.run())
			{
				this.addError(String.format("Generator %s failed", gen.name()));
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean generateAll() throws Exception 
	{
		throw new NotImplementedException();
	}

	@Override
	public boolean generate(String name) throws Exception 
	{
		throw new NotImplementedException();
	}

	
	protected DalGenXmlParser readEntityDef(String appDir) throws Exception
	{
		String path = this.pathCombine(appDir, "mef.xml");
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);
		return parser;
	}
	
}