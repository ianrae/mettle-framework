package org.mef.tools.mgen.codegen;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxTextWriter;
import org.mef.tools.mgen.codegen.generators.BoundaryCodeGen;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.codegen.generators.FormBinderCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterUnitTestCodeGen;
import org.mef.tools.mgen.codegen.generators.ReplyCodeGen;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;


public class PresenterScaffoldCodeGenerator extends CodeGenerator
{
	private DalGenXmlParser parser;
	
	public PresenterScaffoldCodeGenerator(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public int init(String appDir) throws Exception
	{
		super.init(appDir);
		parser = readEntityDef(appDir);
		return parser._entityL.size();
	}
	
	@Override
	public boolean generate() throws Exception
	{
		return false;
	}
	
	@Override
	public boolean generate(String name) throws Exception
	{
		int i = 0;
		for(EntityDef def : parser._entityL)
		{
			if (def.name.equals(name))
			{
				return generate(i);
			}
			i++;
		}
		return false;
	}
	
	public boolean generate(int index) throws Exception
	{
		EntityDef def = parser._entityL.get(index);
		
		
		String baseDir = "/mgen/resources/presenter/";
		String path = getResourceOrFilePath(baseDir, "presenter.stg");
		boolean b = generateOneFile(def, new PresenterCodeGen(_ctx), path, "mef.presenters", "app\\mef\\presenters");
		if (!b )
		{
			return false; //!!
		}
		
//		path = this.pathCombine(stDir, "reply.stg");
//		b = generateOneFile(def, new ReplyCodeGen(_ctx), path, "mef.presenters.replies", "app\\mef\\presenters\\replies");
//		if (!b )
//		{
//			return false; //!!
//		}
//		
//		path = this.pathCombine(stDir, "boundary.stg");
//		b = generateOneFile(def, new BoundaryCodeGen(_ctx), path, "boundaries", "app\\boundaries");
//		if (!b )
//		{
//			return false; //!!
//		}
//		
//		path = this.pathCombine(stDir, "formBinder.stg");
//		b = generateOneFile(def, new FormBinderCodeGen(_ctx), path, "boundaries.binders", "app\\boundaries\\binders");
//		if (!b )
//		{
//			return false; //!!
//		}
//		
//		path = this.pathCombine(stDir, "presenter-unit-test.stg");
//		b = generateOneFile(def, new PresenterUnitTestCodeGen(_ctx), path, "mef", "test\\mef");
//		if (!b )
//		{
//			return false; //!!
//		}
		
		return b;
	}
	private boolean generateOneFile(EntityDef def, CodeGenBase gen, String path, String packageName, String relPath) throws Exception
	{
		gen.init(path, packageName);
		if (! def.enabled)
		{
			log(def.name + " disabled -- no files generated.");
			return true; //do nothing
		}
		
		String code = gen.generate(def);	
		String className = gen.getClassName(def);	

		path = this.pathCombine(appDir, relPath);
		path = this.pathCombine(path, className + ".java");
		File f = new File(path);
		if (f.exists())
		{
			log(path);
			this.log(def.name + ": skipping - already exists");
			return true;
		}
		
		boolean b = writeFile(appDir, relPath, className, code);
		if (!b)
		{
			return false;
		}
		
		return true;
	}
	private DalGenXmlParser readEntityDef(String appDir) throws Exception
	{
		String path = this.pathCombine(appDir, "mef.xml");
		DalGenXmlParser parser = new DalGenXmlParser(_ctx);
		boolean b = parser.parse(path);

//		return parser._entityL.get(0);
		return parser;
	}

}