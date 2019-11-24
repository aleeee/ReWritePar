
package tree.model;

import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.io.AttributeType;

import rewriter.ReWriter;
import util.ReWritingRules;
import visitor.NodeVisitor;

public class SeqPatt implements SkeletonPatt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<SkeletonPatt> children;
	SkeletonPatt parent;
	String lable;
	SkeletonPatt child;
	Set<SkeletonPatt> patterns;
	ReWritingRules rule;
	int depth;
	int id;
	final int idealParDegree;
	final double idealServiceTime;
	final double optimizedTs;
	
	public SeqPatt(SeqPatt s) {
		this.idealParDegree=1;
		this.idealServiceTime=s.getIdealServiceTime();
		this.optimizedTs=s.getOptimizedTs();
	}
	public SeqPatt(double serviceTime) {
		this.idealServiceTime=serviceTime;
		this.idealParDegree=1;
		this.optimizedTs=serviceTime;
	}
	public SeqPatt(double serviceTime, String lable) {
		this.idealServiceTime = serviceTime;
		this.lable=lable;
		this.idealParDegree=1;
		this.optimizedTs=serviceTime;
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

	@Override
	public String toString() {
//		return lable +" ["+getIdealServiceTime()+" ] n> "+ getIdealParDegree() ;
		return lable;
	}

	@Override
	public void setReWriteNodes(boolean flag) {
		// TODO Auto-generated method stub
		
	}
	 @Override
	public boolean reWriteNodes() {
		return false;
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
		SeqPatt other = (SeqPatt) obj;
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
	}

	@Override
	public void setIdealParDegree(int n) {		
	}

	@Override
	public double getIdealServiceTime() {
		return idealServiceTime;
	}

	@Override
	public int getIdealParDegree() {
		return idealParDegree;
	}
	@Override
	public int getOptParallelismDegree() {
		return idealParDegree;
	}
	@Override
	public double getOptServiceTime() {
		return idealServiceTime;
	}
	@Override
	public void setOptServiceTime(double ts) {
				
	}
	@Override
	public void setOptParallelismDegree(int p) {
		
	}
	public double getOptimizedTs() {
		return idealServiceTime;
	}
//	public void setOptimizedTs(double optimizedTs) {
//		this.optimizedTs = optimizedTs;
//	}
	@Override
	public double calculateOptimalServiceTime() {
		return idealServiceTime;
	}
	
	
}
