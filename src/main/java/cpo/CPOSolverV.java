package cpo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cp.IloCP;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class CPOSolverV {
	Logger log = LoggerFactory.getLogger(getClass());

	public Map<SkeletonPatt, List<IloNumVar>> variables;
	public IloCP cplex;
	private SkeletonPatt g;
	private int numAvailableProcessors;
	IloNumExpr expr;
	public IloNumExpr obj;
	public IloNumExpr pd_obj;
	IloNumExpr objTs;
	List<SkeletonPatt> result = new ArrayList<>();
	private SolverModel model;
	public CPOSolverV(SkeletonPatt skeletonPatt, int maxParDegree) throws IloException {

		this.cplex = new IloCP();
		expr = cplex.constant(0);
		this.obj = cplex.constant(0);
		this.pd_obj= cplex.constant(0);
		this.variables = new HashMap<SkeletonPatt, List<IloNumVar>>();
		this.g = skeletonPatt;
		this.numAvailableProcessors = maxParDegree;
		this.model = new SolverModel(cplex, variables, numAvailableProcessors, obj, pd_obj);
		
		addVars(g);
		addConstraints(g);
		addObjective(g);
		String modelName = "C:\\Users\\me\\Desktop\\out\\cpo\\cpoModelV" + skeletonPatt.hashCode()
				+ skeletonPatt.getLable() + skeletonPatt.getIdealServiceTime() + ".cpo";
//		System.out.println(g + "modelName " + modelName);
//		cplex.exportModel(modelName);

		cplex.setOut(null);
	}

	private void addConstraints(SkeletonPatt p) throws IloException {
//		cplex.addLe(variables.get(p).get(1), numAvailableProcessors);
		p.addConstraint(this.model);
//		cplex.addLe(expr, numAvailableProcessors);

	}
	
	private void addVars(SkeletonPatt v) {
		varCreator.accept(v);
		addVariables(v);
	}
	// create variables for all nodes
	private void addVariables(SkeletonPatt p) {
		if(p.getChildren() != null) {
		for (SkeletonPatt v : p.getChildren()) {
			varCreator.accept(v);
			
				addVariables(v);
		}
		
		}
	}

	
	private void addObjective(SkeletonPatt p) throws IloException {
		model = p.addObjective(this.model);
		cplex.addMinimize(cplex.diff(model.getObj(), model.getPd_obj()));
	}


	public List<SkeletonPatt> getSolutions() throws IloException {
		getSolutions(g);
		for (SkeletonPatt v : g.getChildren()) {
			getSolutions(v);
		}
		return result;
	}

	public void getSolutions(SkeletonPatt p) throws IloException {
//		if (p instanceof FarmPatt || p instanceof MapPatt) {
		try {
			List<IloNumVar> vars = variables.get(p);
			double value = cplex.getValue(vars.get(0));
			int parDegree = (int) cplex.getValue(vars.get(1));
			p.setOptServiceTime(value);
			p.setOptParallelismDegree((int) parDegree);
			if (value >= 0.5) {
				result.add(p);
			}else {
				System.out.println("not resolved");
			}
//		}
		if (p.getChildren() == null)
			return;
		for (SkeletonPatt d : p.getChildren()) {
			getSolutions(d);
		}
		}catch(Exception e) {
			log.error("Error inside getSolution "+e.getMessage());
		}

	}

	public void solveIt() throws IloException {
		cplex.solve();
	}

	Consumer<SkeletonPatt> varCreator = s -> {
		IloNumVar tsi = null;
		IloIntVar n = null;
		try {
			int ts_i = (int) s.getIdealServiceTime();
			int n_i = s.getIdealParDegree() ==0? 1: s.getIdealParDegree();
			tsi = cplex.intVar(1 ,ts_i*n_i, "tsi");
			if(s instanceof SeqPatt) {
				n = cplex.intVar(1, 1,"n");
//				n = cplex.intVar(1, numAvailableProcessors, "n");
			}else {
			n = cplex.intVar(1, numAvailableProcessors, "n");}
		} catch (IloException e) {
			log.error("Error at var creation"+ e.getMessage());
		}
		List<IloNumVar> vars = new ArrayList<IloNumVar>();
		vars.add(tsi);
		vars.add(n);

		variables.put(s, vars);
	};

	public void cleanup() throws IloException {
		cplex.clearModel();
		cplex.end();
	}
	
}
