//package org.mef.framework.metadata;
//import java.util.Arrays;
//import java.util.List;
//
//
//
//public class ValueHandlerRegistry
//{
//	private List<ValueHandler> reg;
//	
//	public ValueHandlerRegistry()
//	{
//		//INDEX MUST MATCH Value.TYPE_xxx values. eg TYPE_INT at index 1
////		public static final int TYPE_INT=1;
////		public static final int TYPE_STRING=2;
////		public static final int TYPE_TUPLE=3;
////		public static final int TYPE_LIST=4;
////		public static final int TYPE_BOOLEAN=5;
////		public static final int TYPE_DOUBLE=6;
////		public static final int TYPE_LONG=7;
//		
//		ValueHandler[] arregistry = {
//				null,
//				new DefaultValueHandlers.IntHandler(),
//				new DefaultValueHandlers.StringHandler(),
//				new DefaultValueHandlers.TupleValueHandler(),
//				new DefaultValueHandlers.ListValueHandler(),
//				new DefaultValueHandlers.BooleanHandler(),
//				new DefaultValueHandlers.DoubleHandler(),
//				new DefaultValueHandlers.LongHandler(),
//				new DefaultValueHandlers.DateHandler()
//		};
//		
//		reg = Arrays.asList(arregistry);
//	}
//	
//	
//	//keep all type values sequential, or at least close together!
//	public void register(int type, ValueHandler handler)
//	{
//		int desiredLen = type + 1;
//		for(int i = reg.size(); i <= desiredLen; i++)
//		{
//			reg.add(null);
//		}
//		
//		reg.set(type, handler);
//	}
//	
//	public ValueHandler get(int type)
//	{
//		return reg.get(type);
//	}
//	
//}