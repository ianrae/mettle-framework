package tools.cgen;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.mef.framework.Version;
import org.mef.framework.sfx.SfxContext;
import org.mef.framework.sfx.SfxErrorTracker;
import org.mef.framework.sfx.SfxFileUtils;
import org.mef.tools.mgen.codegen.CodeGenerator;
import org.mef.tools.mgen.codegen.ICodeGenerator;
import org.mef.tools.mgen.codegen.generators.BoundaryCodeGen;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.codegen.generators.PresenterCodeGen;
import org.mef.tools.mgen.codegen.phase.PresenterGenerator;
import org.mef.tools.mgen.parser.EntityDef;
import org.mef.tools.mgen.parser.FieldDef;
import org.stringtemplate.v4.ST;

import tools.BaseTest;

public class CGenTests extends BaseTest
{
	public static class xPresenterGenerator extends CodeGenerator implements ICodeGenerator
	{
		String filename;  //no path
		String baseDir;   //location of source (StringTemplate file)
		String packageName;
		String relPath;
		EntityDef def;
		CodeGenBase gen;

		public xPresenterGenerator(SfxContext ctx, CodeGenBase gen, String baseDir, String filename, EntityDef def, String packageName, String relPath) 
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
	public static class xFormBinderCodeGen extends CodeGenBase
	{
		public static String newline = System.getProperty("line.separator");

		public xFormBinderCodeGen(SfxContext ctx)
		{
			super(ctx);
		}

		@Override
		public String generate(EntityDef def)
		{
			String result = genHeader(def.name); 

			ST st = _group.getInstanceOf("classdecl");
			st.add("type", def.name);
			st.add("name", getClassName(def));
			result += st.render(); 
			result += newline;
			st = _group.getInstanceOf("endclassdecl");
			result += st.render(); 

			return result;
		}

		@Override
		public String getClassName(EntityDef def)
		{
			return  def.name + "FormBinder";
		}


		@Override
		protected String buildField(EntityDef def, FieldDef fdef)
		{
			return "";
		}
	}	


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
			log("CONTROLLERGEN v " + Version.version);
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
			String baseDir = "/mgen/resources/presenter/controllergen/";
			String filename = "formbinder.stg";
			CodeGenBase inner = new xFormBinderCodeGen(_ctx);
			EntityDef def = createDef();
			String packageName = "boundaries.binders";
			String relpath = "app\\boundaries\\binders";

			String appDir = "c:\\tmp\\dd\\99";

			xPresenterGenerator gen = new xPresenterGenerator(_ctx, inner, baseDir, filename, def, packageName, relpath);
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
