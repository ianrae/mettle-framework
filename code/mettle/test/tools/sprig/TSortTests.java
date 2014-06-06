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
	public static class TopoGraphNode
	{
		public Object obj;
		private List<TopoGraphNode> deps = new ArrayList<TopoGraphNode>();

		public TopoGraphNode(Object obj)
		{
			this.obj = obj;
		}

		public List<TopoGraphNode> getDependents()
		{
			return deps;
		}

		public void addDep(TopoGraphNode node)
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
		public static List<TopoGraphNode> sort(final Collection<TopoGraphNode> nodes)
				throws IllegalStateException
				{
			final List<TopoGraphNode> order = new ArrayList<TopoGraphNode>();
			final Set<TopoGraphNode> visited = new HashSet<TopoGraphNode>();

			final Set<TopoGraphNode> alreadySeen = new HashSet<TopoGraphNode>();
			for (final TopoGraphNode n : nodes) {
				alreadySeen.clear();
				visit(n, alreadySeen, visited, order);
			}

			return order;
				}

		private static void visit(final TopoGraphNode n, final Set<TopoGraphNode> alreadySeen,
				final Set<TopoGraphNode> visited, final List<TopoGraphNode> order)
		{
			if (alreadySeen.contains(n))
				throw new IllegalStateException("cycle containing " + n + " found!");
			alreadySeen.add(n);

			if (!visited.contains(n)) {
				visited.add(n);
				for (final TopoGraphNode m : n.getDependents())
					visit(m, alreadySeen, visited, order);
				order.add(n);
			}

			alreadySeen.remove(n);
		}
	}   


	@Test
	public void test()
	{
		List<TopoGraphNode> L = new ArrayList<TopoGraphNode>();
		for(int i = 0; i < 4; i++)
		{
			String s = String.format("node%d", i);
			TopoGraphNode node = new TopoGraphNode(s);
			L.add(node);
		}
		L.get(2).addDep(L.get(1));
		L.get(2).addDep(L.get(3));
		L.get(3).addDep(L.get(0));

		List<TopoGraphNode> sortL = TopologicalSort.sort(L);

		for(TopoGraphNode nod : sortL)
		{
			log(nod.obj.toString());
		}

	}

}

