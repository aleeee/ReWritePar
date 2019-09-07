package tree.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import rewriter.ReWriter;
import util.ReWritingRules;
import visitor.NodeVisitor;



public interface SkeletonPatt extends Serializable{
	int parallelismDegree();
	void calculateServiceTime();
	int completionTime();
	int getServiceTime();
	void setServiceTime(int ts);
	public ArrayList<SkeletonPatt> getChildren();
	public String getLable() ;
	public SkeletonPatt getChild() ;
	public void accept(NodeVisitor visitor);
	public void refactor(ReWriter reWriter);
	public Set<SkeletonPatt> getPatterns();
	public void setParent(SkeletonPatt parent);
	public void setChildren(ArrayList<SkeletonPatt> children);
	void setReWriteNodes(boolean flag);
	public ReWritingRules getRule();
	public void setReWritingRule(ReWritingRules rule);
	void setDepth(int depth);
	int getDepth();
	
}
