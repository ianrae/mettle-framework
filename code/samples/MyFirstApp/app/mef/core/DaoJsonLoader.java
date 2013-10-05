package mef.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mef.entities.Phone;
import mef.entities.User;
import mef.gen.DaoJsonLoader_GEN;

import org.codehaus.jackson.JsonNode;

public class DaoJsonLoader extends DaoJsonLoader_GEN
{
	@Override
	public User readUser(JsonNode node)
	{
		User obj = super.readUser(node);
		JsonNode jj = node.get("phone");
		jj = jj.get("id");
		obj.phone = new Phone();
		obj.phone.id = jj.asLong();

		return obj;
	}

	public void resolveIds(List<User> userL, List<Phone> phoneL) 
	{
		for(User u : userL)
		{
			Phone ph = findPhoneWithId(u.phone.id, phoneL);
			if (ph == null)
			{
				//err!!
			}
			else
			{
				u.phone = ph;
			}
		}
	}
}