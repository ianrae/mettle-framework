package org.mef.tools.mgen.codegen;

public interface ICodeGenerator
{
	String name();
	void initialize(String appDir) throws Exception;
	boolean run() throws Exception ;
}