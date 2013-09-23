package org.mef.framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class MethodInvoker extends SfxBaseObj
	{
		Object _target;
		public Method _method;
		Class<?> _methodParam1;
		
		public MethodInvoker(SfxContext ctx, Object target, String methodName, Class<?> param1)
		{
			super(ctx);
			_method = null;
			_target = target;
			try {
			  _method = target.getClass().getMethod(methodName, param1);
			  
			} catch (SecurityException e) {
			  // ...
			} catch (NoSuchMethodException e) {
			  // ...
			}
			finally
			{
				if (_method == null)
				{
					Method[] ar = target.getClass().getDeclaredMethods();
					for(int i = 0; i < ar.length; i++)
					{
						Method method = ar[i];
//						System.out.println(method.getName());
						if (method.getName().equals(methodName))
						{
							_method = method;
							
							Class<?>[] arParams = method.getParameterTypes();
							if (arParams.length == 0)
							{
								_method = null; //err!
							}
							else
							{
								_methodParam1 = arParams[0]; //1st one. not needed actually
							}
						}
					}
				}
			}
		}
		
		public Object call(Object param1)
		{
			if (_method == null)
			{
				log("err: null _method");
			}
			Object result = null;
			try {
				result = _method.invoke(_target, param1);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}
	}