package org.mef.dalgen.parser;

import java.util.ArrayList;
import java.util.List;

public class EntityDef
{
	public String name;
	public List<FieldDef> fieldL = new ArrayList<FieldDef>();
	public List<String> queryL = new ArrayList<String>();
	public boolean extendInterface;
	public boolean extendMock;
	public boolean extendReal;
	public boolean extendEntity;
	public boolean extendModel;
	
	
	public List<EntityDef> allEntityTypes = new ArrayList<EntityDef>();
}