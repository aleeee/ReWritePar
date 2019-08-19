package tree.model;

import java.util.ArrayList;

import rewriter.ReWriter;
import util.ReWritingRules;
import util.Util;
import visitor.NodeVisitor;

public class PipePatt implements SkeletonPatt {

	ArrayList<SkeletonPatt> children;
	SkeletonPatt parent;
	String lable;
	SkeletonPatt child;
	double serviceTime;
	ArrayList<SkeletonPatt> patterns;
	boolean reWriteNodes;
	ReWritingRules rule;

	public PipePatt() {
		this.lable= "pipe";
	}
	public PipePatt(String lable, double serviceTime) {
		super();
		this.lable = lable;
		this.serviceTime = serviceTime;
	}
	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);

	}
	@Override
	public void refactor(ReWriter reWriter) {
		reWriter.reWrite(this);
	}
	@Override
	public int parallelismDegree() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void calculateServiceTime() {
		this.serviceTime=Util.getServiceTime(this);

	}

	@Override
	public double completionTime() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public ReWritingRules getRule() {
		return rule;
	}

	@Override
	public void setReWritingRule(ReWritingRules rule) {
		this.rule = rule;
		
	}


	@Override
	public void setServiceTime(double ts) {
		this.serviceTime=ts;
		
	}

	@Override
	public ArrayList<SkeletonPatt> getChildren() {
		return children;
	}

	@Override
	public String getLable() {
		return lable;
	}

	@Override
	public SkeletonPatt getChild() {
		return child;
	}

	public SkeletonPatt getParent() {
		return parent;
	}

	public void setParent(SkeletonPatt parent) {
		this.parent = parent;
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void setChildren(ArrayList<SkeletonPatt> children) {
		this.children = children;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public void setChild(SkeletonPatt child) {
		this.child = child;
	}
	@Override
	public ArrayList<SkeletonPatt> getPatterns() {
		return patterns;
	}
	public void setPatterns(ArrayList<SkeletonPatt> patterns) {
		this.patterns = patterns;
	}
	public boolean reWriteNodes() {
		return reWriteNodes;
	}
	public void setReWriteNodes(boolean reWriteNodes) {
		this.reWriteNodes = reWriteNodes;
	}
	@Override
	public String toString() {
//		return "PipePatt [children=" + children + ", parent=" + parent + ", lable=" + lable + ", child=" + child
//				+ ", serviceTime=" + serviceTime + "]";
		return getLable() +" ( "+ (this.getChildren() != null? this.getChildren().toString().replace("[", "").replace("]", "") + " ) ":null);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((lable == null) ? 0 : lable.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PipePatt other = (PipePatt) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (lable == null) {
			if (other.lable != null)
				return false;
		} else if (!lable.equals(other.lable))
			return false;
		return true;
	}

	
}
