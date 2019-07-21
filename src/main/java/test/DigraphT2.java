package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import rewriter.RW;
import rewriter.RW2;
import tree.model.FarmPatt;
import tree.model.SkeletonPatt;
import util.ReWritingRules;
import util.Util;

public class DigraphT2 {
	RW2 rw = new RW2();
	private Queue<SkeletonPatt> queue = new LinkedList<SkeletonPatt>();

	public static class Edge {
		private SkeletonPatt vertex;
		private ReWritingRules rule;

		public Edge(SkeletonPatt v, ReWritingRules c) {
			vertex = v;
			rule = c;
		}

		public SkeletonPatt getSkeletonPattertex() {
			return vertex;
		}

		public ReWritingRules getRule() {
			return rule;
		}

		@Override
		public String toString() {
			return "Edge [vertex=" + vertex + ", rule=" + vertex.getRule() + "]";
		}

	}

	/**
	 * A Map is used to map each vertex to its list of adjacent vertices.
	 */

	private Map<SkeletonPatt, List<Edge>> neighbors = new HashMap<SkeletonPatt, List<Edge>>();

	private int nr_edges;

	/**
	 * String representation of graph.
	 */
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (SkeletonPatt v : neighbors.keySet())
			s.append("\n    " + v + " -> " + neighbors.get(v));
		return s.toString();
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
		s.refactor(rw);
		queue.add(s);
//		s.visited = true;
		List<SkeletonPatt> patterns = new ArrayList<SkeletonPatt>();
		patterns.addAll(s.getPatterns());
		while (!queue.isEmpty()) {
			SkeletonPatt curNode = queue.remove();
			this.add(curNode);
			List<SkeletonPatt> children = curNode.getChildren();	
			if (children != null) {
				for (SkeletonPatt node : children) {
					node.refactor(rw);
					queue.add(node);
					if (node.getPatterns() != null) {
						
						for(SkeletonPatt p: node.getPatterns()) {
							ArrayList<SkeletonPatt> sc = new ArrayList<SkeletonPatt>();
							SkeletonPatt newP = s;
							sc.addAll(s.getChildren());
							sc.add(sc.indexOf(node), p);
							newP.setChildren(sc);
							patterns.add(newP);
						}
					}
				}

			}
		}
		if (patterns != null) {
			for (SkeletonPatt pat : patterns) {
//				System.out.println("before : " + neighbors);
				this.add(s, pat, pat.getRule());
//				System.out.println("after : " + neighbors);
			}

		}
	}
}