package graph;

import tree.model.SkeletonPatt;
import util.ReWritingRules;

public  class Edge {
	private SkeletonPatt vertex;
	private ReWritingRules rule;
	private SkeletonPatt to;
	public Edge(SkeletonPatt from, SkeletonPatt to, ReWritingRules rule) {
		this.vertex = from;
		this.rule = rule;
		this.to=to;
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
	public SkeletonPatt getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "Edge [V=" + vertex + ", rule=" + vertex.getRule() + "]";
	}

}
