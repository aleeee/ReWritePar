package tree.model;

import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.io.AttributeType;

import rewriter.ReWriter;
import util.ReWritingRules;
import util.Util;
import visitor.NodeVisitor;

public class PipePatt implements SkeletonPatt {

	ArrayList<SkeletonPatt> children;
	SkeletonPatt parent;
	String lable;
	SkeletonPatt child;
	Set<SkeletonPatt> patterns;
	boolean reWriteNodes;
	ReWritingRules rule;
	int depth ;
	int id;
	int idealParDegree;
	double idealServiceTime;
	int optParDegree;
	double optServiceTime;
	double optimizedTs;
	
	public PipePatt() {
		this.lable= "pipe";
	}
	public PipePatt(String lable, int serviceTime) {
		super();
		this.lable = lable;
		this.idealServiceTime = serviceTime;
		this.idealParDegree=1;
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
	public void calculateIdealServiceTime() {
		this.idealServiceTime=Util.getServiceTime(this);

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
	public Set<SkeletonPatt> getPatterns() {
		return patterns;
	}
	public void setPatterns(Set<SkeletonPatt> patterns) {
		this.patterns = patterns;
	}
	public boolean reWriteNodes() {
		return reWriteNodes;
	}
	public void setReWriteNodes(boolean reWriteNodes) {
		this.reWriteNodes = reWriteNodes;
	}
	@Override
	public String toString() {
//		return " P ( "+ (this.getChildren() != null? this.getChildren().toString().replace("[", "").replace("]", "") + " ) ":null);
		return getLable() +" ( "+ (this.getChildren() != null? this.getChildren().toString().replace("[", "").replace("]", "") + " ) ":null) ;
				//+ "ts:: ["+getIdealServiceTime()+" ] n> "+ getIdealParDegree() ;
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
		PipePatt other = (PipePatt) obj;
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
//		if(rule == null) {
//			if(other.rule != null)
//				return false;
//		}else if(!rule.equals(other.rule))
//			return false;
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
	
	@Override
	public String getValue() {
		return this.toString();
	}
	@Override
	public AttributeType getType() {
		return AttributeType.STRING;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public void setIdealServiceTime(double ts) {
		this.idealServiceTime = ts;		
	}

	@Override
	public void setIdealParDegree(int n) {
		this.idealParDegree = n;
	}

	@Override
	public double getIdealServiceTime() {
		idealServiceTime=Util.getServiceTime(this);		
		return idealServiceTime;
	}

	@Override
	public int getIdealParDegree() {
		return idealParDegree;
	}
	@Override
	public int getOptParallelismDegree() {
		return optParDegree;
	}
	@Override
	public double getOptServiceTime() {
		return optimizedTs=Util.getOptimalServiceTime(this);
	}
	@Override
	public void setOptServiceTime(double ts) {
		this.optServiceTime=ts;
		
	}
	@Override
	public void setOptParallelismDegree(int p) {
		this.optParDegree=p;
	}
	public double getOptimizedTs() {
		return optimizedTs=Util.getOptimalServiceTime(this);
	}
	public void setOptimizedTs(double optimizedTs) {
		this.optimizedTs = optimizedTs;
	}
	@Override
	public double calculateOptimalServiceTime() {
		return this.optimizedTs = Util.getOptimalServiceTime(this);
	}
	
}
