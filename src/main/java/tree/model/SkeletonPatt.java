package tree.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore
	public ArrayList<SkeletonPatt> getChildren();
	public String getLable() ;
	@JsonIgnore
	public SkeletonPatt getChild() ;
	public void accept(NodeVisitor visitor);
	public void refactor(ReWriter reWriter);
	@JsonIgnore
	public ArrayList<SkeletonPatt> getPatterns();
	@JsonIgnore
	public void setParent(SkeletonPatt parent);
	@JsonIgnore
	public void setChildren(ArrayList<SkeletonPatt> children);
	void setReWriteNodes(boolean flag);
	public ReWritingRules getRule();
	public void setReWritingRule(ReWritingRules rule);
	void setDepth(int depth);
	int getDepth();
	
}
