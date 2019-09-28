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
	int parallelismDegree();
	void calculateServiceTime();
	double completionTime();
	double getServiceTime();
	void setServiceTime(double ts);
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
//	void setLable(String lable);
	
}
