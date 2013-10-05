package org.mef.dalgen.codegen.generators;


	import org.mef.dalgen.parser.EntityDef;
	import org.mef.dalgen.parser.FieldDef;
	import org.stringtemplate.v4.ST;

	import sfx.SfxContext;

	public class BoundaryCodeGen extends CodeGenBase
		{
			public BoundaryCodeGen(SfxContext ctx, String path, String packageName)
			{
				super(ctx, path, packageName);
			}
			
			@Override
			public String generate(EntityDef def)
			{
				String result = genHeader(def.name); 
				
				ST st = _group.getInstanceOf("classdecl");
				st.add("type", def.name);
				st.add("name", getClassName(def));
				result += st.render(); 
				
				result += genFields(def);
				
				st = _group.getInstanceOf("endclassdecl");
				result += st.render(); 
				
				return result;
			}
			
			@Override
			public String getClassName(EntityDef def)
			{
				return  def.name + "Boundary";
			}
			
			
			@Override
			protected String buildField(EntityDef def, FieldDef fdef)
			{
				return "";
			}
		}