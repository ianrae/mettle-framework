package tools;

import java.io.InputStream;

import org.apache.commons.lang.NotImplementedException;
import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.CodeGenerator;
import org.mef.tools.mgen.codegen.ICodeGenerator;

public class CopyFileGenerator extends CodeGenerator implements ICodeGenerator
{
	String filename;  //no path
	String baseDir;   //location of source (StringTemplate file)
	String newExt; //can be null
	String destDir; //can be same as appDir or a directory underneath it

	public CopyFileGenerator(SfxContext ctx, String baseDir, String filename, String newExt, String destDir) 
	{
		super(ctx);
		this.filename = filename;
		this.baseDir = baseDir;
		this.newExt = newExt;
		this.destDir = destDir;
	}

	@Override
	public String name() 
	{
		return "copyFile";
	}

	@Override
	public void initialize(String appDir) throws Exception
	{
		init(appDir);
	}
	@Override
	public boolean run() throws Exception 
	{
		InputStream stream = getSourceFile(baseDir, filename);
		boolean b = copyFile(stream, filename, newExt, destDir);
		return b;
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
}