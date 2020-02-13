package tree.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.io.AttributeType;

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
//	double optimizedTs;
	int numResource;
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
	
//	@Override
//	public String getValue() {
//		return this.toString();
//	}
//	@Override
//	public AttributeType getType() {
//		return AttributeType.STRING;
//	}
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
		return optServiceTime=Util.getOptimalServiceTime(this);
	}
	@Override
	public void setOptServiceTime(double ts) {
		this.optServiceTime=ts;
		
	}
	@Override
	public void setOptParallelismDegree(int p) {
		this.optParDegree=p;
	}
	
	@Override
	public double calculateOptimalServiceTime() {
		return this.optServiceTime = Util.getOptimalServiceTime(this);
	}
	@Override
	public int getNumberOfResources() {
		 return this.numResource=this.children.stream().mapToInt(c -> c.getNumberOfResources()).sum();
	}
	@Override
	public void setNumberOfResources(int r) {
		this.numResource=r;
	}
	@Override
	public void addConstraint(SolverModel model) throws IloException {
		IloCP cplex = model.getCplex();
		Map<SkeletonPatt, List<IloNumVar>> variables = model.getVariables();
		
		List<IloNumVar> vars = variables.get(this);	
		IloIntVar n_i = (IloIntVar) vars.get(1);
		IloNumExpr pStages = cplex.constant(0);
		cplex.addLe(n_i, model.getNumAvailableProcessors());		
		IloIntExpr res= model.getResourcesVars().get(this);
		cplex.addEq(res, n_i);
		for (SkeletonPatt v : children) {
			
			List<IloNumVar> vv = variables.get(v);	
//			if (v instanceof FarmPatt || v instanceof MapPatt) { 
//				pStages = cplex.sum(pStages,cplex.sum(vv.get(1),2));
//			}else {
				pStages = cplex.sum(pStages,((IloIntExpr) model.getResourcesVars().get(v)));
//			}
		}

		cplex.addEq(vars.get(1), pStages);
		cplex.addEq(res, pStages);
		cplex.addLe(vars.get(1), model.getNumAvailableProcessors());
		cplex.addLe(res, model.getNumAvailableProcessors());
		for (SkeletonPatt stage : children) {
			model.setCplex(cplex);
			stage.addConstraint(model);
		}
		
		}

	@Override
	public SolverModel addObjective(SolverModel model) throws IloException {
		IloNumExpr obj = model.getObj();
		IloNumExpr pd_obj = model.getPd_obj();
		IloCP cplex = model.getCplex();
		Map<SkeletonPatt, List<IloNumVar>> variables = model.getVariables();
		
		List<IloNumVar> vars = variables.get(this);
		IloIntVar pd = (IloIntVar) vars.get(1);
		
		obj =  cplex.sum(obj, vars.get(0));
		pd_obj = cplex.sum(pd_obj,pd);
		model.setCplex(cplex);
		model.setObj(obj);
		model.setPd_obj(pd_obj);
		if(children != null) {
		for (SkeletonPatt c : children) {
			model = c.addObjective(model);
		}
	}
		return model;
	}
}
