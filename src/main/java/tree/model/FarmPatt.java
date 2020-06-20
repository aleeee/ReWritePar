package tree.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import cpo.SolverModel;
import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cp.IloCP;
import rewriter.ReWriter;
import util.ReWritingRules;
import util.Util;

public class FarmPatt implements SkeletonPatt {
	
	private static final long serialVersionUID = 1L;
	ArrayList<SkeletonPatt> children;
	SkeletonPatt parent;
	String lable;
	SkeletonPatt child;
	Set<SkeletonPatt> patterns;
	boolean reWriteNodes;
	ReWritingRules rule;
	int depth;
	int optParallelismDegree;
	int id;
	int idealParDegree;
	double idealServiceTime;
	double optServiceTime;
	int numResources;
	private Util util;
	private double latency;
	private double completionTime;
	public FarmPatt() {
		this.lable= "farm";
		this.idealParDegree=1;
		this.optParallelismDegree=1;
		this.util=new Util();
	}

	public FarmPatt(String lable, int serviceTime) {
		super();
		this.util= new Util();
		this.lable = lable;
		this.idealServiceTime = serviceTime;
	}


	@Override
	public void refactor(ReWriter reWriter) {
		reWriter.reWrite(this);	
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
	public ReWritingRules getRule() {
		return rule;
	}

	@Override
	public void setReWritingRule(ReWritingRules rule) {
		this.rule = rule;
		
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
		return getLable() +" "+(this.getChildren() != null? " ( " +this.getChildren().toString() +" ) ":null)
				+ " nw: " +getOptParallelismDegree() ;
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
		FarmPatt other = (FarmPatt) obj;
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
		this.idealServiceTime = ts;		
	}

	@Override
	public void setIdealParDegree(int n) {
		this.idealParDegree = n;
	}

	@Override
	public double getIdealServiceTime() {
		idealServiceTime=util.getServiceTime(this);		
		return idealServiceTime;
	}

	@Override
	public int getIdealParDegree() {
		return idealParDegree;
	}

	@Override
	public int getOptParallelismDegree() {
		return optParallelismDegree;
	}

	@Override
	public double getOptServiceTime() {
		return optServiceTime=util.getOptimizedTs(this);
	}

	@Override
	public void setOptServiceTime(double ts) {
		this.optServiceTime = ts;
	}
	@Override
	public void setOptParallelismDegree(int p) {
		this.optParallelismDegree = p;
	}

	@Override
	public void calculateIdealServiceTime() {
		this.idealServiceTime=util.getServiceTime(this);		
	}

	@Override
	public double calculateOptimalServiceTime() {
		return this.optServiceTime=util.getOptimizedTs(this);
		
	}

	@Override
	public int getNumberOfResources() {
		 return this.numResources=(this.optParallelismDegree *this.children.get(0).getNumberOfResources()) +2;
	}

	@Override
	public void setNumberOfResources(int r) {
		this.numResources=r;
	}



	@Override
	public void addConstraint(SolverModel model)
			throws IloException {
		IloCP cplex = model.getCplex();
		Map<SkeletonPatt, List<IloNumVar>> variables = model.getVariables();
		List<IloNumVar> fVars = variables.get(this);
		IloIntVar pd = (IloIntVar) fVars.get(1);
		cplex.addLe(pd, model.getNumAvailableProcessors());
		cplex.addLe(pd, this.idealParDegree);
		cplex.addGe(pd,1);
		IloIntExpr res= model.getResourcesVars().get(this);
		IloIntExpr childRes= model.getResourcesVars().get(this.children.get(0));
		cplex.addEq(res, cplex.sum(cplex.prod(childRes, pd),2));
		cplex.addLe(cplex.sum(cplex.prod((pd) ,childRes),2), model.getNumAvailableProcessors());
		cplex.addLe(res,model.getNumAvailableProcessors());
		this.children.get(0).addConstraint(model);
		
	
	}
	@Override
	public SolverModel addObjective(SolverModel model) throws IloException {
		IloNumExpr obj = model.getObj();
		IloNumExpr pd_obj = model.getPd_obj();
		IloCP cplex = model.getCplex();
		Map<SkeletonPatt, List<IloNumVar>> variables = model.getVariables();
		
		List<IloNumVar> vars = variables.get(this);
		IloIntVar pd = (IloIntVar) vars.get(1);
		double ts = this.children.get(0).getIdealServiceTime();			
		cplex.addEq(vars.get(0) , cplex.div((int)ts,pd));
		obj =  cplex.sum(obj, vars.get(0));
		pd_obj = cplex.sum(pd_obj,pd);
		model.setCplex(cplex);
		model.setObj(obj);
		model.setPd_obj(pd_obj);
		
		model = this.children.get(0).addObjective(model);	
		
		
		return model;
	}

	@Override
	public void setLatency(double latency) {
		this.latency=latency;
		
	}

	@Override
	public double getLatency() {
		return latency;
	}

	@Override
	public void setCompletionTime(double completionTime) {
		this.completionTime=completionTime;
		
	}

	@Override
	public double getComletionTime() {
		return completionTime;
	}



}
