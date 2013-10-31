package mef.core;

import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthRuleDAO;
import mef.daos.IAuthSubjectDAO;
import mef.daos.IAuthTicketDAO;
import mef.entities.AuthRole;
import mef.entities.AuthSubject;
import mef.entities.AuthTicket;

public interface IAuthorizer
{
	void init(IAuthSubjectDAO userDao, IAuthRoleDAO roleDao, IAuthTicketDAO ticketDao, IAuthRuleDAO ruleDao);
	boolean isAuth(AuthSubject subj, AuthRole role, AuthTicket ticket);
	
	AuthSubject findSubject(String identityId);
}