package org.mef.framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mef.framework.auth.NotAuthorizedException;
import org.mef.framework.auth.NotLoggedInException;
import org.mef.framework.replies.Reply;
import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class MethodInvoker extends SfxBaseObj
	{
		Object _target;
		public Method _method;
		Class<?> _methodParam1;
		String _methodName;
		
		public MethodInvoker(SfxContext ctx, Object target, String methodName, Class<?> param1)
		{
			super(ctx);
			_method = null;
			_target = target;
			_methodName = methodName;
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
		
		public Object call(Object param1, Reply reply)
		{
			if (_method == null)
			{
				log("err: can't find method: " + _methodName);
				return null;
			}
			Object result = null;
			try 
			{
//				log("a0");
				result = _method.invoke(_target, param1);
//				log("a1");
			} catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) 
			{
				log("EXCEPTION in presenter!");
//				e.printStackTrace();
				Reply tmp = this.handleInnerException(e, reply);
				if (tmp != null)
				{
					result = tmp;
				}
				else
				{
					e.getCause().printStackTrace();
				}
			}	
			return result;
		}
		
		Reply handleInnerException(InvocationTargetException e, Reply reply)
		{
			Throwable ex = e.getCause();
			if (ex == null)
			{
				return null;
			}
			
			if (ex instanceof NotLoggedInException)
			{
				reply.setDestination(Reply.FOWARD_NOT_AUTHENTICATED);
				return reply;
			}
			else if (ex instanceof NotAuthorizedException)
			{
				reply.setDestination(Reply.FOWARD_NOT_AUTHORIZED);
				return reply;
			}
			else
			{
				this.addError(String.format("PRESENTER EXCEPTION: " + ex.getMessage()));
				return null;
			}
		}
	}