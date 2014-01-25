package org.mef.tools.mgen.codegen;

public interface ICodeGenerator
{
	String name();

	boolean run() throws Exception ;
}