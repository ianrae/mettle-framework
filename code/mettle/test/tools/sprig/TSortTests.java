package tools.sprig;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import tools.BaseTest;


public class TSortTests extends BaseTest
{
	public static class TSortNode
	{
		public Object obj;
		private List<TSortNode> deps = new ArrayList<TSortNode>();

		public TSortNode(Object obj)
		{
			this.obj = obj;
		}

		public List<TSortNode> getDependents()
		{
			return deps;
		}

		public void addDep(TSortNode node)
		{
			deps.add(node);
		}
	}

	//http://chianti.ucsd.edu/svn/core3/model-impl/tags/model-impl-parent-3.0.0-alpha3/impl/src/main/java/org/cytoscape/model/internal/tsort/TopologicalSort.java
	/**
	 *  Implements topological sorting of nodes in a graph.
	 *  See for example http://en.wikipedia.org/wiki/Topological_sorting (the Tarjan algorithm)
	 */
	public static class TopologicalSort {
		/**
		 *  @param nodes the list of all nodes
		 *  @param edges the edges that connect the nodes that need to be sorted.
		 *  @return the topological order
		 *  @throws IllegalStateException if a cycle has been detected
		 *  N.B. it might be a good idea to make sure that whatever the concrete type of the nodes in
		 *  "nodes" are has a toString() method that returns the name of a node since this method
		 *  will be used if a cycle has been detected to report one of the nodes in the cycle.
		 */
		public static List<TSortNode> sort(final Collection<TSortNode> nodes)
				throws IllegalStateException
				{
			final List<TSortNode> order = new ArrayList<TSortNode>();
			final Set<TSortNode> visited = new HashSet<TSortNode>();

			final Set<TSortNode> alreadySeen = new HashSet<TSortNode>();
			for (final TSortNode n : nodes) {
				alreadySeen.clear();
				visit(n, alreadySeen, visited, order);
			}

			return order;
				}

		private static void visit(final TSortNode n, final Set<TSortNode> alreadySeen,
				final Set<TSortNode> visited, final List<TSortNode> order)
		{
			if (alreadySeen.contains(n))
				throw new IllegalStateException("cycle containing " + n + " found!");
			alreadySeen.add(n);

			if (!visited.contains(n)) {
				visited.add(n);
				for (final TSortNode m : n.getDependents())
					visit(m, alreadySeen, visited, order);
				order.add(n);
			}

			alreadySeen.remove(n);
		}
	}   


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

