package org.mef.dalgen.codegen;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mef.dalgen.codegen.generators.CodeGenBase;
import org.mef.dalgen.codegen.generators.DALIntefaceCodeGen;
import org.mef.dalgen.codegen.generators.EntityCodeGen;
import org.mef.dalgen.codegen.generators.MockDALCodeGen;
import org.mef.dalgen.codegen.generators.ModelCodeGen;
import org.mef.dalgen.codegen.generators.PresenterCodeGen;
import org.mef.dalgen.codegen.generators.ReplyCodeGen;
import org.mef.dalgen.parser.DalGenXmlParser;
import org.mef.dalgen.parser.EntityDef;

import sfx.SfxBaseObj;
import sfx.SfxContext;
import sfx.SfxTextWriter;

public class AppScaffoldCodeGenerator extends SfxBaseObj
{
	private String appDir;
	private String stDir;
	public boolean disableFileIO;
	
	public AppScaffoldCodeGenerator(SfxContext ctx)
	{
		super(ctx);
	}
	
	public void init(String appDir, String stDir) throws Exception
	{
		this.appDir = appDir;
		this.stDir = stDir;
	}
	
	public boolean generate() throws Exception
	{
		createDirStructure();
		String filename = "mef.xml";
		
		String resDir = FilenameUtils.concat(stDir, "copy");
		return copyFile(filename, resDir, appDir);
	}
	
	private boolean copyFile(String filename, String resDir, String appDir) throws Exception
	{
		String src = FilenameUtils.concat(resDir, filename);
		String dest = FilenameUtils.concat(appDir, filename);
		
		File fdest = new File(dest);
		if (fdest.exists())
		{
			log(String.format("copy: (skipping, exists) => %s", dest));
			return true;
		}
		
		log(String.format("copy: %s => %s", src, dest));
		if (disableFileIO)
		{
			return true;
		}
		FileUtils.copyFile(new File(src), new File(dest));
		return true;
	}
	private void createDirStructure()
	{
		createDir("app\\boundaries");
		createDir("app\\boundaries\\binders");
		createDir("app\\boundaries\\dals");
		createDir("mef");
		createDir("mef\\core");
		createDir("mef\\dals");
		createDir("mef\\dals\\mocks");
		createDir("mef\\entities");
		createDir("mef\\gen");
		createDir("mef\\presenters");
		createDir("mef\\presenters\\replies");
		createDir("conf\\mef\\seed");
		createDir("test\\mef");
		
	}

	private void createDir(String dirName) 
	{
		String dest = FilenameUtils.concat(appDir, dirName);
		log("creating dir: " + dest);
		File f = new File(dest);
		f.mkdirs();
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