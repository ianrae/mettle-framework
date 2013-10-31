package mef.core;

import mef.daos.IAuthRoleDAO;
import mef.daos.IAuthRuleDAO;
import mef.daos.IAuthSubjectDAO;
import mef.daos.IAuthTicketDAO;
import mef.entities.AuthRole;
import mef.entities.AuthRule;
import mef.entities.AuthSubject;
import mef.entities.AuthTicket;

import org.mef.framework.sfx.SfxBaseObj;
import org.mef.framework.sfx.SfxContext;

public class MyAuthorizer extends SfxBaseObj implements IAuthorizer
{
	private IAuthRoleDAO _roleDao;
	private IAuthTicketDAO _ticketDao;
	private IAuthRuleDAO _ruleDao;
	private IAuthSubjectDAO _subjectDao;
	
	public MyAuthorizer(SfxContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public boolean isAuth(AuthSubject subj, AuthRole role, AuthTicket ticket) 
	{
		AuthRule rule = _ruleDao.find_by_subject_and_role_and_ticket(subj, role, ticket);
		return (rule != null);
	}

	@Override
	public void init(IAuthSubjectDAO subjectDao, IAuthRoleDAO roleDao,
			IAuthTicketDAO ticketDao, IAuthRuleDAO ruleDao) 
	{
		this._subjectDao = subjectDao;
		this._roleDao = roleDao;
		this._ticketDao = ticketDao;
		this._ruleDao = ruleDao;
		
	}
	
}