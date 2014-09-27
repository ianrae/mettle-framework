package org.mef.tools.scaffoldgen;

import java.io.File;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.CodeGenerator;
import org.mef.tools.mgen.codegen.ICodeGenerator;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.parser.EntityDef;

public class ScaffoldGenerator extends CodeGenerator implements ICodeGenerator
{
	String filename;  //no path
	String baseDir;   //location of source (StringTemplate file)
	String packageName;
	String relPath;
	EntityDef def;
	CodeGenBase gen;

	public ScaffoldGenerator(SfxContext ctx, CodeGenBase gen, String baseDir, String filename, EntityDef def, String packageName, String relPath) 
	{
		super(ctx);
		this.filename = filename;
		this.baseDir = baseDir;
		this.packageName = packageName;
		this.relPath = relPath;
		this.def = def;
		this.gen = gen;
		this.gen.setUserCanModifyFlag(true);
	}

	@Override
	public String name() 
	{
		return "presenter";
	}

	@Override
	public void initialize(String appDir) throws Exception
	{
		init(appDir);
		String path = getResourceOrFilePath(baseDir, filename);
	}
	@Override
	public boolean run() throws Exception 
	{
		String path = getResourceOrFilePath(baseDir, filename);

		gen.init(path, packageName);
		if (! def.enabled)
		{
			log(def.name + " disabled -- no files generated.");
			return true; //do nothing
		}

		String code = gen.generate(def);	
		String className = gen.getClassName(def);	

		path = this.pathCombine(appDir, relPath);
		this.createDir(relPath);

		String filename = className;
		if (! filename.contains(".html"))
		{
			filename += ".java";
		}
		path = this.pathCombine(path, filename);
		File f = new File(path);
		if (f.exists())
		{
			log(prettifyPath(path)  + ": skipping - already exists");
			return true;
		}

		boolean b = writeFile(appDir, relPath, filename, code);
		if (!b)
		{
			return false;
		}

		return true;
	}

	@Override
	public boolean generateAll() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean generate(String name) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}