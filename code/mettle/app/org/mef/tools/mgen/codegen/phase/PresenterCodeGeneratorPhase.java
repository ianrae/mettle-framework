package org.mef.tools.mgen.codegen.phase;

import org.mef.framework.sfx.SfxContext;
import org.mef.tools.mgen.codegen.generators.BoundaryCodeGen;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.codegen.generators.ControllerCodeGen;
import org.mef.tools.mgen.codegen.generators.FormBinderCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterCodeGen;
import org.mef.tools.mgen.codegen.generators.PresenterUnitTestCodeGen;
import org.mef.tools.mgen.codegen.generators.ReplyCodeGen;
import org.mef.tools.mgen.codegen.generators.ViewCodeGen;
import org.mef.tools.mgen.parser.DalGenXmlParser;
import org.mef.tools.mgen.parser.EntityDef;

public class PresenterCodeGeneratorPhase extends CodeGeneratorPhase
{
	private DalGenXmlParser parser;
	
	public PresenterCodeGeneratorPhase(SfxContext ctx) 
	{
		super(ctx, "presenter");
	}

	@Override
	public void initialize(String appDir) throws Exception 
	{
		init(appDir); //done twice. fix later!! but need it initialized here for addGenerators
		parser = readEntityDef(appDir);

		for(EntityDef def : parser._entityL)
		{
			if (! def.shouldGenerate(EntityDef.PRESENTER))
			{
				continue;
			}
			
			String baseDir = "/mgen/resources/presenter/";
			String filename = "presenter.stg";
			CodeGenBase inner = new PresenterCodeGen(_ctx);
			PresenterGenerator gen = new PresenterGenerator(_ctx, inner, baseDir, filename, def,  "mef.presenters", "app\\mef\\presenters");
			this.add(gen);
			
			filename = "reply.stg";
			inner = new ReplyCodeGen(_ctx);
			gen = new PresenterGenerator(_ctx, inner, baseDir, filename, def,  "mef.presenters.replies", "app\\mef\\presenters\\replies");
			add(gen);
			
			filename = "boundary.stg";
			inner = new BoundaryCodeGen(_ctx);
			gen = new PresenterGenerator(_ctx, inner, baseDir, filename, def,  "boundaries", "app\\boundaries");
			add(gen);
			
			filename = "formbinder.stg";
			inner = new FormBinderCodeGen(_ctx);
			gen = new PresenterGenerator(_ctx, inner, baseDir, filename, def, "boundaries.binders", "app\\boundaries\\binders");
			add(gen);
			
			filename = "presenter-unit-test.stg";
			inner = new PresenterUnitTestCodeGen(_ctx);
			gen = new PresenterGenerator(_ctx, inner, baseDir, filename, def,  "mef.presenter", "test\\mef\\presenter");
			add(gen);
			
			filename = "controller.stg";
			inner = new ControllerCodeGen(_ctx);
			gen = new PresenterGenerator(_ctx, inner, baseDir, filename, def,  "controllers", "app\\controllers");
			add(gen);
			
			filename = "index-view.stg";
			inner = new ViewCodeGen(_ctx);
			gen = new PresenterGenerator(_ctx, inner, baseDir, filename, def, "", "app\\views\\" + def.name);
			add(gen);
			
		}
		
		super.initialize(appDir); //it will call initalize of each generator
	}

}