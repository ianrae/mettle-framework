package tools.scaffoldgen;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.mef.framework.Version;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.framework.sfx.SfxFileUtils;
import org.mef.tools.mgen.codegen.generators.BoundaryCodeGen;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.codegen.generators.PresenterCodeGen;
import org.mef.tools.mgen.codegen.phase.PresenterGenerator;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.scaffoldgen.FormBinderCodeGen;
import org.mef.tools.scaffoldgen.ScaffoldGenerator;

import tools.BaseTest;

public class ScaffoldGenTests extends BaseTest
{
	public static class ControllerGen
	{
		protected SfxContext _ctx;
		private String controllerName;
		private String boundaryName;
		private String replyName;
		private String binderName;		

		public void runCodeGeneration(String controllerName, String boundaryName, String replyName, String binderName) throws Exception
		{
			createContext();
			log("SCAFFOLD-GEN v " + Version.version);
			this.controllerName = controllerName;
			this.boundaryName = boundaryName;
			this.replyName = replyName;
			this.binderName = binderName;

			if (! isNullOrEmpty(controllerName))
			{
				genController();
			}

			if (! isNullOrEmpty(boundaryName))
			{
				genBoundary();
			}

			if (! isNullOrEmpty(binderName))
			{
				genBinder();
			}
		}
		private void genBoundary() throws Exception 
		{
			String baseDir = "/mgen/resources/scaffoldgen/";
			String filename = "formbinder.stg";
			CodeGenBase inner = new FormBinderCodeGen(_ctx);
			EntityDef def = createDef();
			String packageName = "boundaries.binders";
			String relpath = "app\\boundaries\\binders";

			String appDir = "c:\\tmp\\dd\\99";

			ScaffoldGenerator gen = new ScaffoldGenerator(_ctx, inner, baseDir, filename, def, packageName, relpath);
			gen.init(appDir);
			boolean b = gen.run();
		}



		private EntityDef createDef() 
		{
			EntityDef def = new EntityDef();
			def.enabled = true;
			def.name = "CollectInput";
			// TODO Auto-generated method stub
			return def;
		}
		private void genController()
		{
		}
		private void genBinder()
		{

		}

		//--helpers--
		protected void createContext()
		{
			_ctx = new SfxContext();
			_ctx.getServiceLocator().registerSingleton(SfxErrorTracker.class, new SfxErrorTracker(_ctx));
		}

		protected void log(String s)
		{
			System.out.println(s);
		}
		private boolean isNullOrEmpty(String address) 
		{
			if (address == null)
			{
				return true;
			}
			return address.isEmpty();
		}		
	}

	@Test
	public void test() throws Exception 
	{

		String path = "C:\\tmp\\dd\\99\\app\\boundaries\\binders\\CollectInputFormBinder.java";
		SfxFileUtils utils = new SfxFileUtils();
		utils._fileActionsEnabled = true;
		utils.deleteFile(new File(path));

		ControllerGen gen = new ControllerGen();

		gen.runCodeGeneration("ImportController", "ImportBoundary", "PatientImportReply", "CollectInputBinder");

	}


}
