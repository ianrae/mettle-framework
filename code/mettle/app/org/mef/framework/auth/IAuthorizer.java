package org.mef.framework.auth;

import org.mef.framework.auth2.AuthUser;


public interface IAuthorizer
{
	//does currently logged in user have authorization
	boolean isAuth(AuthUser user, String roleName, String ticketName);
}