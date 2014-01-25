package org.mef.tools.mgen.codegen;

public interface ICodeGenPhase
{
	String name();
	void add(ICodeGenerator gen);
	boolean run(String appDir) throws Exception ;
}