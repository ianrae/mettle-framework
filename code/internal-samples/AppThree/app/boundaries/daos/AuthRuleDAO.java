//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.
package boundaries.daos;


import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.mef.framework.binder.IFormBinder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import play.Logger;
import java.util.Date;

import boundaries.Boundary;
import boundaries.daos.*;
import mef.core.Initializer;

import models.AuthRuleModel;
import play.db.ebean.Model.Finder;

import mef.daos.*;
import mef.entities.*;
import mef.gen.AuthRuleDAO_GEN;

import com.avaje.ebean.Page;
import org.mef.framework.auth.AuthRole;
import org.mef.framework.auth.AuthTicket;
public class AuthRuleDAO extends AuthRuleDAO_GEN
{
@Override
public AuthRule find_by_subject_and_role_and_ticket(AuthSubject s, AuthRole r, AuthTicket t)
{
	return null;
}

}
