package test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import rewriter.RW2;
import tree.model.SkeletonPatt;
import util.ReWritingRules;

public class DigraphT4 {
	private  Logger log = LoggerFactory.getLogger(getClass());
	RW2 rw = new RW2();
	public Graph<SkeletonPatt, Edge> g = new  DefaultDirectedGraph<>(Edge.class);
	private Queue<SkeletonPatt> queue = new LinkedList<SkeletonPatt>();
	public static class Edge {
		private SkeletonPatt vertex;
		private ReWritingRules rule;
		private SkeletonPatt to;
		public Edge(SkeletonPatt from, SkeletonPatt to, ReWritingRules rule) {
			this.vertex=from;
			this.to=to;
			this.rule=rule;
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

	/**
	 * A Map is used to map each vertex to its list of adjacent vertices.
	 */

	private Map<SkeletonPatt, List<Edge>> neighbors = new LinkedHashMap<SkeletonPatt, List<Edge>>();

	/**
	 * String representation of graph.
	 */
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (SkeletonPatt v : neighbors.keySet())
			try {
				s.append("\n    " + v +" -> " + neighbors.get(v));
				
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
	 * Add a vertex to the graph. Nothing happens if vertex is already in graph.
	 */
	public void add(SkeletonPatt vertex) {
		if (neighbors.containsKey(vertex))
			return;
		neighbors.put(vertex, new ArrayList<Edge>());
	}

	public int getNumberOfEdges() {
		int sum = 0;
		for (List<Edge> outBounds : neighbors.values()) {
			sum += outBounds.size();
		}
		return sum;
	}

	/**
	 * True iff graph contains vertex.
	 */
	public boolean contains(SkeletonPatt vertex) {
		return neighbors.containsKey(vertex);
	}

	/**
	 * Add an edge to the graph; if either vertex does not exist, it's added. This
	 * implementation allows the creation of multi-edges and self-loops.
	 */
	public void add(SkeletonPatt from, SkeletonPatt to, ReWritingRules rule) {
		this.add(from);
		g.addVertex(from);
		g.addVertex(to);
		g.addEdge(from,to,new Edge(from, to, rule));
		this.add(to);
		neighbors.get(from).add(new Edge(to, rule));
	}

	public int outDegree(int vertex) {
		return neighbors.get(vertex).size();
	}

	public int inDegree(SkeletonPatt vertex) {
		return inboundNeighbors(vertex).size();
	}

	public List<SkeletonPatt> outboundNeighbors(SkeletonPatt vertex) {
		List<SkeletonPatt> list = new ArrayList<SkeletonPatt>();
		for (Edge e : neighbors.get(vertex))
			list.add(e.vertex);
		return list;
	}

	public List<SkeletonPatt> inboundNeighbors(SkeletonPatt inboundSkeletonPattertex) {
		List<SkeletonPatt> inList = new ArrayList<SkeletonPatt>();
		for (SkeletonPatt to : neighbors.keySet()) {
			for (Edge e : neighbors.get(to))
				if (e.vertex.equals(inboundSkeletonPattertex))
					inList.add(to);
		}
		return inList;
	}

	public boolean isEdge(SkeletonPatt from, SkeletonPatt to) {
		for (Edge e : neighbors.get(from)) {
			if (e.vertex.equals(to))
				return true;
		}
		return false;
	}

	public ReWritingRules getCost(SkeletonPatt from, SkeletonPatt to) {
		for (Edge e : neighbors.get(from)) {
			if (e.vertex.equals(to))
				return e.rule;
		}
		return null;
	}

	public void bfs(SkeletonPatt s) {
		Gson gson = new Gson();
		s.setDepth(0);
		s.getChildren().forEach(c -> c.setDepth(1));
		queue.add(s);
	    JsonObject root = new  JsonObject();
	    JsonElement el = gson.toJsonTree(s);
	    root.add("root", el);
	    JsonObject tmp ;
		Map<SkeletonPatt, List<SkeletonPatt>> patterns = new HashMap<>();
		while (!queue.isEmpty()) {
			SkeletonPatt curNode = queue.remove();
			curNode.refactor(rw);
			this.add(curNode);
//			log.debug("curNode: "+ curNode );
			patterns.put(curNode, curNode.getPatterns());
			for (SkeletonPatt sk : curNode.getPatterns()) {
				if (!this.contains(sk) && !queue.contains(sk) && sk.getDepth() < 2) {
					queue.add(sk);
//					log.debug("sk1: " + sk);
					
				}else {
					
				}
			}

			List<SkeletonPatt> children = curNode.getChildren();
			if (children != null) {
				for (SkeletonPatt node : children) {
					node.setParent(curNode);
					node.refactor(rw);
//					queue.add(node);
					if (node.getPatterns() != null) {
						for (SkeletonPatt sk : node.getPatterns()) {
							if (!this.contains(sk) && !queue.contains(sk) && sk.getDepth() < 2) {
								queue.add(sk);
//								log.debug("sk2: " + sk);
							}
						}
					}
					patterns.get(curNode).addAll(node.getPatterns());
//						patterns.put(curNode,node.getPatterns());
				}
			}

		}

		if (patterns != null) {
			patterns.entrySet().forEach(e -> {
				SkeletonPatt k = e.getKey();
				e.getValue().forEach(v -> {
					this.add(k, v, v.getRule());
//					if(!this.contains(k))
//						temp.add(k);
				});
			});
//		System.out.println(gson.toJsonTree(patterns));
		Map<SkeletonPatt, Long> count =  (Map<SkeletonPatt, Long>) patterns.values().stream()
					.flatMap(Collection::stream)
					.collect(Collectors.collectingAndThen(
							Collectors.groupingBy(Function.identity(), Collectors.counting()), 
							m -> {m.values().removeIf(v -> v < 2L); 
//							System.out.println(m);
//							System.out.println("of ");
							m.entrySet().forEach(mm -> {
//								System.out.println(mm);
//								System.out.println("of ");
//								System.out.println(patterns.get(mm));
							});
						return m;})
							);
//		System.out.println(patterns);
		}
	}
    
	
}