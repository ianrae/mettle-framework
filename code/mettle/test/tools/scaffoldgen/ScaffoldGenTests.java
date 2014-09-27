package tools.scaffoldgen;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.mef.framework.sfx.SfxFileUtils;
import org.mef.tools.mgen.codegen.generators.BoundaryCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterCodeGen;
import org.mef.tools.mgen.codegen.phase.PresenterGenerator;
import org.mef.tools.scaffoldgen.ScaffoldGenBase;

import tools.BaseTest;

public class ScaffoldGenTests extends BaseTest
{
	@Test
	public void test() throws Exception 
	{

		String path = "C:\\tmp\\dd\\99\\app\\boundaries\\binders\\CollectInputFormBinder.java";
		SfxFileUtils utils = new SfxFileUtils();
		utils._fileActionsEnabled = true;
		utils.deleteFile(new File(path));

		ScaffoldGenBase gen = new ScaffoldGenBase();
//		String appDir = this.getCurrentDirectory();
		String appDir = "c:\\tmp\\dd\\99";
		
		gen.init(appDir);
		

		gen.runCodeGeneration("ImportController", "ImportBoundary", "PatientImportReply", "CollectInputBinder");

	}


}
