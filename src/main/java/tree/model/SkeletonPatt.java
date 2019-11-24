package tree.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.io.Attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;

import rewriter.ReWriter;
import util.ReWritingRules;
import visitor.NodeVisitor;



public interface SkeletonPatt extends Serializable, Attribute{
	
	@JsonIgnore
	public ArrayList<SkeletonPatt> getChildren();
	public String getLable() ;
	public void setLable(String l);
	@JsonIgnore
	public SkeletonPatt getChild() ;
	@JsonIgnore
	public void accept(NodeVisitor visitor);
	@JsonIgnore
	public void refactor(ReWriter reWriter);
	@JsonIgnore
	public Set<SkeletonPatt> getPatterns();
	@JsonIgnore
	public void setPatterns(Set<SkeletonPatt> patterns);
	@JsonIgnore
	public void setParent(SkeletonPatt parent);
	@JsonIgnore
	public SkeletonPatt getParent();
	@JsonIgnore
	public void setChildren(ArrayList<SkeletonPatt> children);
	void setReWriteNodes(boolean flag);
	public ReWritingRules getRule();
	public void setReWritingRule(ReWritingRules rule);
	void setDepth(int depth);
	int getDepth();
	boolean reWriteNodes();
	int getId();
	void setId(int id);
	int getOptParallelismDegree();
	double getOptServiceTime();
	void setOptServiceTime(double ts);
	void setOptParallelismDegree(int p);
	void setIdealServiceTime(double ts);
	void setIdealParDegree(int n);
	double getIdealServiceTime();
	int getIdealParDegree();
	void calculateIdealServiceTime();
	double calculateOptimalServiceTime();
	default String print(){
		return getLable() +" "+(this.getChildren() != null? " ( " +this.getChildren().toString() +" ) ":null) 
//				+ " I_PD: " +getIdealParDegree() + " I_TS::  ["+getIdealServiceTime()+"] "
				+ (this instanceof FarmPatt? " nw: "+getOptParallelismDegree(): "")
				+ " ts::  ["+String.format("%.2f",getOptServiceTime())+"] "								
				;

	};
	
}
