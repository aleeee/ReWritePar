package cpo;

import java.util.List;
import java.util.Map;

import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cp.IloCP;
import tree.model.SkeletonPatt;

public class SolverModel {
	private Map<SkeletonPatt, List<IloNumVar>> variables;
	private IloCP cplex;
	private SkeletonPatt g;
	private int numAvailableProcessors;
	private IloNumExpr obj;
	private IloNumExpr pd_obj;
	
	
	
	public SolverModel(IloCP cplex, Map<SkeletonPatt, List<IloNumVar>> variables, int numAvailableProcessors,
			IloNumExpr obj, IloNumExpr pd_obj) {
		this.variables = variables;
		this.cplex = cplex;
		this.numAvailableProcessors = numAvailableProcessors;
		this.obj = obj;
		this.pd_obj = pd_obj;
	}
	public Map<SkeletonPatt, List<IloNumVar>> getVariables() {
		return variables;
	}
	public void setVariables(Map<SkeletonPatt, List<IloNumVar>> variables) {
		this.variables = variables;
	}
	public IloCP getCplex() {
		return cplex;
	}
	public void setCplex(IloCP cplex) {
		this.cplex = cplex;
	}
	public SkeletonPatt getG() {
		return g;
	}
	public void setG(SkeletonPatt g) {
		this.g = g;
	}
	public int getNumAvailableProcessors() {
		return numAvailableProcessors;
	}
	public void setNumAvailableProcessors(int numAvailableProcessors) {
		this.numAvailableProcessors = numAvailableProcessors;
	}
	public IloNumExpr getObj() {
		return obj;
	}
	public void setObj(IloNumExpr obj) {
		this.obj = obj;
	}
	public IloNumExpr getPd_obj() {
		return pd_obj;
	}
	public void setPd_obj(IloNumExpr pd_obj) {
		this.pd_obj = pd_obj;
	}
	
	
}
