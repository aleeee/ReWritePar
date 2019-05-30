package tree.model;

import java.util.ArrayList;

import rewriter.SkelReWriter;
import visitor.NodeVisitor;

public class PipePatt implements SkeletonPatt {

	ArrayList<SkeletonPatt> children;
	SkeletonPatt parent;
	String lable;
	SkeletonPatt child;
	long serviceTime;
	ArrayList<SkeletonPatt> patterns;
	public PipePatt(String lable, long serviceTime) {
		super();
		this.lable = lable;
		this.serviceTime = serviceTime;
	}
	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);

	}
	@Override
	public void refactor(SkelReWriter reWriter) {
		reWriter.reWrite(this);
	}
	@Override
	public int parallelismDegree() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long serviceTime() {
		// TODO Auto-generated method stub
		return serviceTime;
	}

	@Override
	public long completionTime() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void setServiceTime(long ts) {
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

	public long getServiceTime() {
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
		return "PipePatt [children=" + children + ", parent=" + parent + ", lable=" + lable + ", child=" + child
				+ ", serviceTime=" + serviceTime + "]";
	}

	
}
