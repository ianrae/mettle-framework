package mef.core;

import org.codehaus.jackson.JsonNode;

import mef.entities.AuthRole;
import mef.entities.AuthRule;
import mef.entities.AuthSubject;
import mef.gen.DaoJsonLoader_GEN;

public class DaoJsonLoader extends DaoJsonLoader_GEN
{
	@Override
	public AuthRule readAuthRule(JsonNode node)
	{
		AuthRule obj = super.readAuthRule(node);
		JsonNode jj = node.get("role");
		if (jj != null)
		{
			jj = jj.get("id");
			if (jj != null)
			{
				obj.role = new AuthRole();
				obj.role.id = jj.asLong();
			}
		}

		jj = node.get("subject");
		if (jj != null)
		{
			jj = jj.get("id");
			if (jj != null)
			{
				obj.subject = new AuthSubject();
				obj.subject.id = jj.asLong();
			}
		}
		return obj;
	}
	
}