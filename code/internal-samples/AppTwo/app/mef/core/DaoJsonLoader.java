package mef.core;

import org.codehaus.jackson.JsonNode;

import mef.entities.Company;
import mef.entities.Computer;
import mef.gen.DaoJsonLoader_GEN;

public class DaoJsonLoader extends DaoJsonLoader_GEN
{
	@Override
	public Computer readComputer(JsonNode node)
	{
		Computer obj = super.readComputer(node);
		JsonNode jj = node.get("company");
		jj = jj.get("id");
		obj.company = new Company();
		obj.company.id = jj.asLong();

		return obj;
	}
}