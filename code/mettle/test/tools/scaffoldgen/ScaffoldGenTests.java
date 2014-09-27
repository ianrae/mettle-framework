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
		String appDir = "c:\\tmp\\dd\\99";
		deleteFile(appDir, "\\app\\boundaries\\binders\\CollectInputFormBinder.java");
		deleteFile(appDir, "\\app\\boundaries\\ImportBoundary.java");

		ScaffoldGenBase gen = new ScaffoldGenBase();
//		String appDir = this.getCurrentDirectory();
		
		gen.appDir = appDir; //force it
		
		gen.presenterToUse("PatientImportPresenter");
		gen.inputToUse("CollectInput");
		gen.replyToUse("PatientImportReply");
		gen.runCodeGeneration("ImportController", "ImportBoundary", "CollectInputFormBinder");

	}

	//--helpers--
	private void deleteFile(String appDir, String relPath) throws Exception
	{
		String path = appDir + relPath;
		SfxFileUtils utils = new SfxFileUtils();
		utils._fileActionsEnabled = true;
		utils.deleteFile(new File(path));
	}
}
