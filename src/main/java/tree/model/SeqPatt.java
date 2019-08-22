package tree.model;

import java.util.ArrayList;

import rewriter.ReWriter;
import util.ReWritingRules;
import util.Util;
import visitor.NodeVisitor;

public class SeqPatt implements SkeletonPatt {
	ArrayList<SkeletonPatt> children;
	SkeletonPatt parent;
	String lable;
	SkeletonPatt child;
	double serviceTime;
	ArrayList<SkeletonPatt> patterns;
	ReWritingRules rule;
	int depth;
	
	public SeqPatt() {
		this.lable="seq";
	}
	public SeqPatt(double serviceTime) {
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
//		this.serviceTime=this.getServiceTime();

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
	public double completionTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setServiceTime(double ts) {
		this.serviceTime = ts;

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

	@Override
	public String toString() {
		return lable;
//		return lable +" , " +getServiceTime();
	}

	@Override
	public void setReWriteNodes(boolean flag) {
		// TODO Auto-generated method stub
		
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
		SeqPatt other = (SeqPatt) obj;
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
	@Override
	public void setDepth(int depth) {
		this.depth=depth;
	}
	@Override
	public int getDepth() {
		return depth;
	}

}
