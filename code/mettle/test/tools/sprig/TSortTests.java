package tools.sprig;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mef.sprig.util.TSortNode;
import org.mef.sprig.util.TopologicalSort;

import tools.BaseTest;


public class TSortTests extends BaseTest
{
	@Test
	public void test()
	{
		List<TSortNode> L = new ArrayList<TSortNode>();
		for(int i = 0; i < 4; i++)
		{
			String s = String.format("node%d", i);
			TSortNode node = new TSortNode(s);
			L.add(node);
		}
		L.get(2).addDep(L.get(1));
		L.get(2).addDep(L.get(3));
		L.get(3).addDep(L.get(0));

		List<TSortNode> sortL = TopologicalSort.sort(L);

		for(TSortNode nod : sortL)
		{
			log(nod.obj.toString());
		}

	}

}

