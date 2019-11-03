package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import rewriter.RW;
import tree.model.SkeletonPatt;
import util.ReWritingRules;
import util.Util;

public class DiGraphGen4 {
	private Logger log = LoggerFactory.getLogger(getClass());
	RW rw = new RW();
	public static Graph<SkeletonPatt, Edge> g = new DefaultDirectedGraph<>(Edge.class);
	
	private Map<SkeletonPatt, List<Edge>> neighbors = new LinkedHashMap<SkeletonPatt, List<Edge>>();

	private Queue<SkeletonPatt> queue = new LinkedList<SkeletonPatt>();
	AtomicInteger intId = new AtomicInteger();
	public static class Edge {
		private SkeletonPatt vertex;
		private ReWritingRules rule;

		public Edge(SkeletonPatt from, SkeletonPatt to, ReWritingRules rule) {
			this.vertex = from;
			this.rule = rule;
		}

		public Edge(SkeletonPatt v, ReWritingRules c) {
			vertex = v;
			rule = c;
		}

		public SkeletonPatt getVertex() {
			return vertex;
		}

		public ReWritingRules getRule() {
			return rule;
		}

		@Override
		public String toString() {
			return "Edge [V=" + vertex + ", rule=" + vertex.getRule() + "]";
		}

	}

	public String toString() {
		StringBuffer s = new StringBuffer();
		for (SkeletonPatt v : neighbors.keySet())
			try {
				s.append("\n    " + v + " -> " + neighbors.get(v));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return s.toString();
	}

	public Map<SkeletonPatt, List<Edge>> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(Map<SkeletonPatt, List<Edge>> neighbors) {
		this.neighbors = neighbors;
	}

	/**
	 * Add a vertex to the graph if vertex is not in graph.
	 */
	public void add(SkeletonPatt vertex) {
		if (neighbors.containsKey(vertex))
			return;
		neighbors.put(vertex, new ArrayList<Edge>());
	}

	public boolean contains(SkeletonPatt vertex) {
		return neighbors.containsKey(vertex);
	}

	/**
	 * Add an edge to the graph
	 */
	public void add(SkeletonPatt from, SkeletonPatt to, ReWritingRules rule) {
		this.add(from);
		if(!g.containsVertex(from)) {
			from.setId(intId.getAndIncrement());
			g.addVertex(from);}
		if(!g.containsVertex(to)) {
			to.setId(intId.getAndIncrement());
			g.addVertex(to);}
		g.addEdge(from, to, new Edge(from, to, rule));
		this.add(to);
		neighbors.get(from).add(new Edge(to, rule));
	}

	public void bfs(SkeletonPatt s, int depth) {
		s.setDepth(0); // set depth of the root to zero and increments as it goes deep, this is used to
						// terminate the process after certain depth
		s.getChildren().forEach(c -> c.setDepth(1));
		queue.add(s);
		s.setId(intId.getAndIncrement());
		g.addVertex(s);
		Map<SkeletonPatt, Set<SkeletonPatt>> patterns = new HashMap<>();
		while (!queue.isEmpty()) {
			SkeletonPatt curNode = queue.remove();
			curNode.calculateIdealServiceTime();
//			if(curNode.getServiceTime() > 1) {//Ts=1 is optimal time so no need to refactor
			curNode.refactor(rw);
			this.add(curNode);
			patterns.put(curNode, curNode.getPatterns());
			for (SkeletonPatt sk : curNode.getPatterns()) {
				if (!this.contains(sk) && !queue.contains(sk) && Util.getHeight(sk) < depth) {
					queue.add(sk);
				}
			}

			List<SkeletonPatt> children = curNode.getChildren();
			if (children != null) {
				for (SkeletonPatt node : children) {
					node.setParent(curNode);
					node.refactor(rw);
					node.setDepth(curNode.getDepth()+1);
					if (node.getPatterns() != null) {
						for (SkeletonPatt sk : node.getPatterns()) {
							if (!this.contains(sk) && !queue.contains(sk) && Util.getHeight(sk) < depth) {
								queue.add(sk);
							}
						}
					}
					patterns.get(curNode).addAll(node.getPatterns());
				}
			}
		}

		if (patterns != null) {
			patterns.entrySet().forEach(e -> {
				SkeletonPatt k = e.getKey();
				e.getValue().forEach(v -> {
					v.calculateIdealServiceTime();
					this.add(k, v, v.getRule());
				});
			});

		}
	}

}