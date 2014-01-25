package org.mef.tools.mgen.codegen;

public interface ICodeGenPhase
{
	String name();
	void add(ICodeGenerator gen);
	void initialize(String appDir) throws Exception;
	boolean run() throws Exception;
}