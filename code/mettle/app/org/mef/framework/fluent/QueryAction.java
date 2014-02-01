package org.mef.framework.fluent;

public class QueryAction
{
	public static final String WHERE = "WHERE";
	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String ALL = "ALL";
	public static final String ORDERBY = "ORDERBY";
	public static final String FETCH = "FETCH";
	public static final String LIMIT = "LIMIT";
	public static final String OFFSET = "OFFSET"; 
	
	public String action;
	public String op;
	public String fieldName;
	public String subFieldName;
	public Object obj;
}