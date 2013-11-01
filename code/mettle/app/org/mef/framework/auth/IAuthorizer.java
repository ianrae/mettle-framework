package org.mef.framework.auth;


public interface IAuthorizer
{
//	void init(IAuthSubjectDAO userDao, IAuthRoleDAO roleDao, IAuthTicketDAO ticketDao, IAuthRuleDAO ruleDao);
	boolean isAuthEx(AuthSubject subj, AuthRole role, AuthTicket ticket);
	
	AuthSubject findSubject(String identityId);
	boolean isAuth(AuthSubject subj, String roleName, AuthTicket ticket);
}