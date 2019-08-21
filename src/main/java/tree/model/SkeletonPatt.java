package tree.model;

import java.io.Serializable;
import java.util.ArrayList;

import rewriter.ReWriter;
import rewriter.SkelReWriter;
import util.ReWritingRules;
import visitor.NodeVisitor;



public interface SkeletonPatt extends Serializable{
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
	public ReWritingRules getRule();
	public void setReWritingRule(ReWritingRules rule);
	void setDepth(int depth);
	int getDepth();
	
}
