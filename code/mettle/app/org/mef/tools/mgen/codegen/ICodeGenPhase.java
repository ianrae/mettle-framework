package org.mef.tools.mgen.codegen;

public interface ICodeGenPhase
{
	void initx(String appDir) throws Exception ;
	String name();
	void add(ICodeGenerator gen);
	boolean run() throws Exception ;
}