package tree.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import cpo.SolverModel;
import ilog.concert.IloException;
import rewriter.ReWriter;
import util.ReWritingRules;

public interface SkeletonPatt extends Serializable{	
	
	public ArrayList<SkeletonPatt> getChildren();
	public String getLable() ;
	public void setLable(String l);

	public void refactor(ReWriter reWriter);
	
	public Set<SkeletonPatt> getPatterns();
	
	public void setPatterns(Set<SkeletonPatt> patterns);
	
	public void setParent(SkeletonPatt parent);
	
	public SkeletonPatt getParent();
	
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
	int getNumberOfResources();
	void setNumberOfResources(int r);
	void setLatency(double latency);
	double getLatency();
	void setCompletionTime(double completionTime);
	double getComletionTime();
	void addConstraint(SolverModel model) throws IloException;
	SolverModel addObjective(SolverModel model) throws IloException;
//	SkeletonPatt reWrite();
	default String print(){
		return getLable() +" "+(this.getChildren() != null? " ( " +this.getChildren().toString() +" ) ":null) 
				+ ((this instanceof FarmPatt || this instanceof MapPatt)? " nw: "+(getOptParallelismDegree() > 0? getOptParallelismDegree(): 1): "")
				+ " ts::  ["+String.format("%.2f",getOptServiceTime())+"] "								
				;

	}
	
}
