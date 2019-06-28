package tree.model;

import java.util.ArrayList;

import rewriter.ReWriter;
import rewriter.SkelReWriter;
import visitor.NodeVisitor;



public interface SkeletonPatt {
	int parallelismDegree();
	void calculateServiceTime();
	double completionTime();
	double getServiceTime();
	void setServiceTime(double ts);
	public ArrayList<SkeletonPatt> getChildren();
	public String getLable() ;
	public SkeletonPatt getChild() ;
	public void accept(NodeVisitor visitor);
	public void refactor(ReWriter reWriter);
	public ArrayList<SkeletonPatt> getPatterns();
	public void setParent(SkeletonPatt parent);
	public void setChildren(ArrayList<SkeletonPatt> children);
	void setReWriteNodes(boolean flag);
}
