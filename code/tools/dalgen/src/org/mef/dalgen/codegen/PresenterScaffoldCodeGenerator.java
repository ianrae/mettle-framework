package org.mef.dalgen.codegen;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.mef.dalgen.codegen.generators.BoundaryCodeGen;
import org.mef.dalgen.codegen.generators.CodeGenBase;
import org.mef.dalgen.codegen.generators.DAOIntefaceCodeGen;
import org.mef.dalgen.codegen.generators.EntityCodeGen;
import org.mef.dalgen.codegen.generators.MockDAOCodeGen;
import org.mef.dalgen.codegen.generators.ModelCodeGen;
import org.mef.dalgen.codegen.generators.PresenterCodeGen;
import org.mef.dalgen.codegen.generators.ReplyCodeGen;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

import sfx.SfxBaseObj;
import sfx.SfxContext;
import sfx.SfxTextWriter;

public class PresenterScaffoldCodeGenerator extends SfxBaseObj
{
	private String appDir;
	private String stDir;
	private DalGenXmlParser parser;
	public boolean disableFileIO;
	
	public PresenterScaffoldCodeGenerator(SfxContext ctx)
	{
		super(ctx);
	}
	
	public int init(String appDir, String stDir) throws Exception
	{
		this.appDir = appDir;
		this.stDir = stDir;
		parser = readEntityDef(appDir);
		return parser._entityL.size();
	}
	
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
		
		String path = this.pathCombine(stDir, "presenter.stg");
		PresenterCodeGen gen = new PresenterCodeGen(_ctx, path, "mef.presenters");
		boolean b = generateOneFile(def, gen, "app\\mef\\presenters");
		if (!b )
		{
			return false; //!!
		}
		
		path = this.pathCombine(stDir, "reply.stg");
		ReplyCodeGen gen2 = new ReplyCodeGen(_ctx, path, "mef.presenters.replies");
		b = generateOneFile(def, gen2, "app\\mef\\presenters\\replies");
		if (!b )
		{
			return false; //!!
		}
		
		path = this.pathCombine(stDir, "boundary.stg");
		BoundaryCodeGen gen3 = new BoundaryCodeGen(_ctx, path, "boundaries");
		b = generateOneFile(def, gen3, "app\\boundaries");
		if (!b )
		{
			return false; //!!
		}
		
		return b;
	}
	private boolean generateOneFile(EntityDef def, CodeGenBase gen, String relPath) throws Exception
	{
		if (! def.enabled)
		{
			log(def.name + " disabled -- no files generated.");
			return true; //do nothing
		}
		
		String code = gen.generate(def);	
		String className = gen.getClassName(def);	

		String path = this.pathCombine(appDir, relPath);
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

	private boolean writeFile(String appDir, String subDir, String fileName, String code)
	{
		String outPath = this.pathCombine(appDir, subDir);
		outPath = this.pathCombine(outPath, String.format("%s.java", fileName));
		log(fileName + ": " + outPath);
		if (disableFileIO)
		{
			return true;
		}
		
		SfxTextWriter w = new SfxTextWriter(outPath, null);
		w.addLine(code);
		boolean b = w.writeFile();
		return b;
	}
	
}