package tree.model;

import java.util.ArrayList;

import rewriter.ReWriter;
import util.Util;
import visitor.NodeVisitor;

public class SeqPatt implements SkeletonPatt {
	ArrayList<SkeletonPatt> children;
	SkeletonPatt parent;
	String lable;
	SkeletonPatt child;
	double serviceTime;
	ArrayList<SkeletonPatt> patterns;

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
//		return "SeqPatt [children=" + children + ", parent=" + parent + ", lable=" + lable + ", child=" + child
//				+ ", serviceTime=" + serviceTime + "]";
		
		return lable +" , " +getServiceTime();
	}

	@Override
	public void setReWriteNodes(boolean flag) {
		// TODO Auto-generated method stub
		
	}

}
