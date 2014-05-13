package org.mef.tools.mgen.codegen.phase;

import java.io.File;

import org.mef.framework.sfx.SfxFileUtils;
import org.mef.tools.mgen.codegen.generators.CodeGenBase;
import org.mef.tools.mgen.parser.EntityDef;

class AddParams
{
	public String baseDir;
	public String filename;
	public EntityDef def;
	public CodeGenBase inner;
	public boolean isExtended;
	public boolean isParentOfExtended;
	public boolean forceUserCanModifyFlag;

	public AddParams(String baseDir, String filename, EntityDef def, CodeGenBase inner, boolean isExtended)
	{
		this.baseDir = baseDir;
		this.filename = filename;
		this.def = def;
		this.inner = inner;
		this.isExtended = isExtended;
		//			inner.setExtended(def.shouldExtend(EntityDef.ENTITY));
	}

	public boolean needParentClass(String appDir, String relPath)
	{
		//			boolean isExtended = def.shouldExtend(EntityDef.ENTITY);

		if (isExtended)
		{
			String className = inner.getClassName(def);	
			className = className.replace("_GEN", "");
			SfxFileUtils utils = new SfxFileUtils();
			String path = utils.PathCombine(appDir, relPath);
			path = utils.PathCombine(path, className + ".java");
			File f = new File(path);
			if (! f.exists())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean canUserModify()
	{
//		isExtended mean is a _GEN class
//		isParentOfExtended means is parent of extended class
//		userCanModify
//
//		file        isEx  isPar  UCMod
//		entity_gen  true   false  false
//		entity      false  true   true
//		boundary    false  false  true
//		binder       ""    ""     true
//		real-dao    false  true   true
//		qryproc     false  false  false
//		idao        false   true  true
//		mockdao     false   true  true
//		real-dao-gen true   false false
//		idao-gen     true   false false
		
		if (this.isExtended)
		{
			return false;
		}
		else if (this.isParentOfExtended)
		{
			return true;
		}
		else
		{
			return this.forceUserCanModifyFlag;
		}
	}
}