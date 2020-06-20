
package tree.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import cpo.SolverModel;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cp.IloCP;
import rewriter.ReWriter;
import util.ReWritingRules;

public class SeqPatt implements SkeletonPatt {
	
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
     int numResource=1;
	
	public SeqPatt(SeqPatt s) {
		this.idealParDegree=1;
		this.idealServiceTime=s.getIdealServiceTime();
		this.optimizedTs=s.getIdealServiceTime();
		this.lable=s.getLable();
		
	}
	public SeqPatt(double serviceTime) {
		this.idealServiceTime=serviceTime;
		this.idealParDegree=1;
		this.optimizedTs=serviceTime;
		this.lable = "seq";
	}
	public SeqPatt(double serviceTime, String lable) {
		this.idealServiceTime = serviceTime;
		this.lable=lable;
		this.idealParDegree=1;
		this.optimizedTs=serviceTime;
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
	
	@Override
	public double calculateOptimalServiceTime() {
		return idealServiceTime;
	}
	@Override
	public int getNumberOfResources() {
		return  this.numResource=1;
		
	}
	@Override
	public void setNumberOfResources(int r) {
		this.numResource=r;
	}
	@Override
	public void addConstraint(SolverModel model)
			throws IloException {
		model.getCplex().addEq(model.getVariables().get(this).get(1), 1);
		model.getCplex().addEq(model.getResourcesVars().get(this), 1);
		
	}
	@Override
	public SolverModel addObjective(SolverModel model) throws IloException {
		IloNumExpr obj = model.getObj();
		IloNumExpr pd_obj = model.getPd_obj();
		IloCP cplex = model.getCplex();
		List<IloNumVar> vars = model.getVariables().get(this);
		IloIntVar pd = (IloIntVar) vars.get(1);
		obj =  cplex.sum(obj, vars.get(0));
		pd_obj = cplex.sum(pd_obj,pd);
		
		model.setCplex(cplex);
		model.setObj(obj);
		model.setPd_obj(pd_obj);
		return model;
	}
	@Override
	public void setLatency(double latency) {
				
	}
	@Override
	public double getLatency() {
		return this.idealServiceTime;
	}
	@Override
	public void setCompletionTime(double completionTime) {
				
	}
	@Override
	public double getComletionTime() {
		return  this.idealServiceTime;
	}
	

}
