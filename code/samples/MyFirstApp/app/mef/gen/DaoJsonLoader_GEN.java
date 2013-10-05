//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import mef.entities.Task;




public class DaoJsonLoader_GEN
{


	public Task readTask(JsonNode node)
	{
		Task obj = new Task();

		return obj;
	}
	public List<Task> loadTasks(JsonNode rootNode) 
	{
		List<Task> phoneL = new ArrayList<Task>();

    	JsonNode msgNode = rootNode.path("Task");
		Iterator<JsonNode> ite = msgNode.getElements();

		int i = 0;
		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			Task ph = readTask(temp);

			phoneL.add(ph);
			i++;
		}    	

		return phoneL;
	}
	private Task findTaskWithId(long id, List<Task> phoneL) 
	{
		for (Task ph : phoneL)
		{
			if (ph.id == id)
			{
				return ph;
			}
		}
		return null;
	}
}
