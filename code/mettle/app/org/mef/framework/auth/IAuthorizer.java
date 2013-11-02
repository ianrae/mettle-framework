package org.mef.framework.auth;


public interface IAuthorizer
{
//	void init(IAuthSubjectDAO userDao, IAuthRoleDAO roleDao, IAuthTicketDAO ticketDao, IAuthRuleDAO ruleDao);
	boolean isAuthEx(IAuthSubject subj, AuthRole role, AuthTicket ticket);
	
//	AuthSubject findSubject(String identityId);
	boolean isAuth(IAuthSubject subj, String roleName, AuthTicket ticket);
}