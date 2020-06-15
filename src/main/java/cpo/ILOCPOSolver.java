package cpo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.enterprise.inject.Instance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cp.IloCP;
import ilog.cp.IloCPEngine;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class ILOCPOSolver {
	static Logger log = LoggerFactory.getLogger(ILOCPOSolver.class);

	public static Map<SkeletonPatt, List<IloNumVar>> variables;
	public static Map<SkeletonPatt, IloIntExpr> resourcesVars;
	public static IloCP cplex;
	private static SkeletonPatt skeleton;
	private static int numAvailableProcessors;
	static IloNumExpr expr;
	public static IloNumExpr obj;
	public static IloNumExpr pd_obj;
	static IloNumExpr objTs;
	static List<SkeletonPatt> result = new ArrayList<>();
	private static SolverModel model;
	
	private ILOCPOSolver() {
		
	}
 public synchronized	static void init(){

		
		try {
			cplex = new IloCP();
			cplex.setParameter("Workers", 1);
		
		cplex.setParameter(IloCP.IntParam.ParallelMode, 1);
		expr = cplex.constant(0);
		obj = cplex.constant(0);
		pd_obj= cplex.constant(0);
		variables = new HashMap<SkeletonPatt, List<IloNumVar>>();
		resourcesVars = new HashMap<SkeletonPatt, IloIntExpr>();
		
		
		model = new SolverModel(cplex, variables, numAvailableProcessors, obj, pd_obj,resourcesVars);
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cplex.setOut(null);
	}
	

	public static synchronized SkeletonPatt getSkeleton() {
		return skeleton;
	}

	public static synchronized void setSkeleton(SkeletonPatt skeleton) throws IloException {
		ILOCPOSolver.skeleton = skeleton;
//		cplex = new IloCP();
//		model = new SolverModel(cplex, variables, numAvailableProcessors, obj, pd_obj,resourcesVars);
	
	}


	public static synchronized void setNumAvailableProcessors(int numAvailableProcessors) {
		ILOCPOSolver.numAvailableProcessors = numAvailableProcessors;
		model.setNumAvailableProcessors(numAvailableProcessors);
	}


	public static  synchronized int getNumAvailableProcessors() {
		return numAvailableProcessors;
	}




	public static synchronized void addConstraints() throws IloException {
//		cplex.addLe(variables.get(p).get(1), numAvailableProcessors);
		skeleton.addConstraint(model);
//		cplex.addLe(expr, numAvailableProcessors);

	}
	
	public static synchronized void addVars() {
		varCreator.accept(skeleton);
		addVariables(skeleton);
	}
	// create variables for all nodes
	private static synchronized void addVariables(SkeletonPatt p) {
		if(p.getChildren() != null) {
		for (SkeletonPatt v : p.getChildren()) {
			varCreator.accept(v);
			
				addVariables(v);
		}
		
		}
	}

	
	public static synchronized void addObjective() throws IloException {
		model = skeleton.addObjective(model);
		cplex.addMinimize(cplex.diff(model.getObj(), model.getPd_obj()));
	}


	public static synchronized List<SkeletonPatt> getSolutions() throws IloException {
		getSolutions(skeleton);
		for (SkeletonPatt v : skeleton.getChildren()) {
			getSolutions(v);
		}
		return result;
	}

	public static synchronized void getSolutions(SkeletonPatt p) throws IloException {
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

		if (p.getChildren() == null)
			return;
		}catch(Exception e) {
			p.setOptParallelismDegree(1);
			log.warn("!  getSolution "+p +"\t"+e.getMessage());
		}
		if(p.getChildren() !=null) {
		for (SkeletonPatt d : p.getChildren()) {
			getSolutions(d);
		}
		}
		

	}

	public static synchronized void solveIt() throws IloException {
		cplex.solve();
	}

	static Consumer<SkeletonPatt> varCreator = s -> {
		IloNumVar tsi = null;
		IloIntVar n = null;
		IloIntExpr r = null;
		try {
			int ts_i = (int) s.getIdealServiceTime();
			int n_i = s.getIdealParDegree() ==0? 1: s.getIdealParDegree();
			tsi = cplex.intVar(1 ,ts_i*n_i, "tsi");
			if(s instanceof SeqPatt) {
				n = cplex.intVar(1, 1,"n");
			}else {
			n = cplex.intVar(1, numAvailableProcessors, "n");}

				r = cplex.intVar(1,numAvailableProcessors);

		} catch (IloException e) {
			log.error("Error at var creation"+ e.getMessage());
		}
		List<IloNumVar> vars = new ArrayList<IloNumVar>();
		
		vars.add( tsi);
		vars.add(n);
		
        variables.put(s,vars);
		resourcesVars.put(s,r);
	};

	public static synchronized void cleanup() throws IloException {
		cplex.clearModel();
		
		cplex.end();
	}
	
}
