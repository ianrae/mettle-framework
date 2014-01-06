package org.mef.tools.mgen.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityDef
{
	public boolean enabled; //whether we will generate files 
	public String name;
	public List<FieldDef> fieldL = new ArrayList<FieldDef>();
	public List<String> queryL = new ArrayList<String>();
	public List<String> methodL = new ArrayList<String>();
	
//	public boolean extendInterface;
//	public boolean extendMock;
//	public boolean extendReal;
//	public boolean extendEntity;
//	public boolean extendModel;
//	public boolean genPresenter;
	public String useExistingPackage;
	
	public static final String ENTITY = "entity";
	public static final String MODEL = "model";
	public static final String DAO_INTERFACE = "dao_interface";
	public static final String DAO_MOCK =  "dao_mock";
	public static final String DAO_REAL = "dao_real";
	public static final String PRESENTER = "presenter";
	
	public HashMap<String, GeneratorOptions> optionsMap = new HashMap<String, GeneratorOptions>();
	public GeneratorOptions getOptions(String name)
	{
		return optionsMap.get(name);
	}
	public boolean shouldExtend(String name)
	{
		return optionsMap.get(name).extend;
	}
	public boolean shouldGenerate(String name)
	{
		return optionsMap.get(name).generate;
	}
	public void setShouldExtend(String name, boolean b)
	{
		GeneratorOptions options = optionsMap.get(name);
		options.extend = b;
	}
	
	
	public List<EntityDef> allEntityTypes = new ArrayList<EntityDef>();
}