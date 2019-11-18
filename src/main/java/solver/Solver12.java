package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cp.IloCP;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.OptimalityTarget;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class Solver12 {

	private Map<SkeletonPatt, List<IloIntVar>> variables;
	private IloCP cplex;
	private SkeletonPatt g;
	private int numAvailableProcessors;
	IloNumExpr expr;
	IloNumExpr obj;
	IloNumExpr objTs;
	List<SkeletonPatt> result = new ArrayList<>();

	public Solver12(SkeletonPatt skeletonPatt, int maxParDegree) throws IloException {

		this.cplex = new IloCP();
		expr = cplex.constant(0);
		obj = cplex.constant(0);
		this.variables = new HashMap<SkeletonPatt, List<IloIntVar>>();
		this.g = skeletonPatt;
		this.numAvailableProcessors = maxParDegree;
		
		addVars(g);
		addConstraints(g);
		addObjective();
		String modelName = "C:\\Users\\me\\Desktop\\out\\cplexModel1" + skeletonPatt.hashCode()
				+ skeletonPatt.getLable() + skeletonPatt.getIdealServiceTime() + ".cpo";
		System.out.println(g + "modelName " + modelName);
		cplex.exportModel(modelName);

		cplex.setOut(null);
	}

	private void addConstraints(SkeletonPatt p) throws IloException {
		cplex.addLe(variables.get(p).get(1), numAvailableProcessors);
		if (p instanceof PipePatt) {
			addPipeConstraints(p);
		} else if (p instanceof CompPatt) {
			addCompConstraints(p);
		} else if (p instanceof FarmPatt) {
			addFarmConstraints(p, false);
		}
		cplex.addLe(expr, numAvailableProcessors);

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

	private void addPipeConstraints(SkeletonPatt p) throws IloException {
		IloNumExpr pStages = cplex.constant(0);
		try {
			for (SkeletonPatt v : p.getChildren()) {
				List<IloIntVar> vars = variables.get(v);		
				IloIntVar ts_i = vars.get(0);
				IloIntVar n_i = vars.get(1);
				if (v instanceof FarmPatt) {
					cplex.addLe(n_i, numAvailableProcessors);
					pStages =addResources(v, pStages);
//					pStages = cplex.sum(pStages,cplex.sum(cplex.prod(n_i,variables.get(v.getChildren().get(0)).get(1)),2));
					cplex.addLe(n_i, v.getIdealParDegree());
//					cplex.addGe(cplex.div(ts_i,n_i ), v.getIdealServiceTime()/v.getIdealParDegree());
					cplex.addGe(cplex.prod(ts_i, n_i),v.getIdealServiceTime() * v.getIdealParDegree());
					addFarmConstraints(v, false);
					
				} else if (v instanceof CompPatt) {
//					expr = cplex.sum(expr, vars.get(1));
					addCompConstraints(v);
					pStages = cplex.sum(pStages, vars.get(1));
				} else if (v instanceof PipePatt) {
//					expr = cplex.sum(expr, vars.get(1));
					addPipeConstraints(v);
					pStages = cplex.sum(pStages, vars.get(1));
				} else if (v instanceof SeqPatt) {
					cplex.addEq(vars.get(1), 1);
					pStages = cplex.sum(pStages, vars.get(1));
//					expr = cplex.sum(expr, vars.get(1));
				}
			}
			expr = cplex.sum(expr, pStages);
			
			cplex.addEq(variables.get(p).get(1), pStages);
			cplex.addLe(variables.get(p).get(1), numAvailableProcessors);
		} catch (IloException e) {
			throw e;
		}
	}

	private void addCompConstraints(SkeletonPatt p) throws IloException {
		List<IloIntVar> compVars = variables.get(p);
		
//		cplex.addEq(compVars.get(1), 1);
		try {
			for (SkeletonPatt v : p.getChildren()) {
				List<IloIntVar> vars = variables.get(v);		
				IloIntVar ts_i = vars.get(0);
				IloIntVar n_i = vars.get(1);
				cplex.addLe(n_i, numAvailableProcessors);
				if (v instanceof FarmPatt) {
					cplex.addLe(cplex.sum(n_i,2), numAvailableProcessors);
					cplex.addLe(n_i, v.getIdealParDegree());
//					cplex.addGe(cplex.div(ts_i, n_i), v.getIdealServiceTime()/v.getIdealParDegree());
					cplex.addGe(cplex.prod(ts_i, n_i),v.getIdealServiceTime() * v.getIdealParDegree());
					addFarmConstraints(v, true);
				} else if (v instanceof CompPatt) {
					addCompConstraints(v);
				} else if (v instanceof PipePatt) {
					addPipeConstraints(v);
				}else if (v instanceof SeqPatt) {
					cplex.addEq(vars.get(1), 1);
				}
			}
//		cplex.addEq(compVars.get(1), cplex.max(variables.get(p).get(1));

		} catch (IloException e) {
			throw e;
		}
	}

	private void addFarmConstraints(SkeletonPatt p, boolean reusable) throws IloException {
		IloIntVar pd = variables.get(p).get(1);
		try {
			for (SkeletonPatt v : p.getChildren()) {
					List<IloIntVar> vars = variables.get(v);		
					IloIntVar ts_i = vars.get(0);
					IloIntVar n_i = vars.get(1);
					cplex.addLe(n_i, numAvailableProcessors);
					
//				if(!reusable) {						
//						expr = cplex.sum(expr, n_i);
//					}
				if (v instanceof FarmPatt) {					
					cplex.addLe(n_i, v.getIdealParDegree());
					cplex.addLe(cplex.sum(n_i,2), numAvailableProcessors);
//					cplex.addGe(cplex.div(ts_i, n_i), v.getIdealServiceTime()/v.getIdealParDegree());
					cplex.addGe(cplex.prod(ts_i, n_i),v.getIdealServiceTime() * v.getIdealParDegree());
					cplex.addLe(cplex.sum(cplex.prod(pd ,vars.get(1)),2), numAvailableProcessors);
					addFarmConstraints(v, reusable);

				} else if (v instanceof CompPatt) {
					
					addCompConstraints(v);
				} else if (v instanceof PipePatt) {
					
					cplex.addLe(cplex.sum(cplex.prod(pd ,vars.get(1)),2), numAvailableProcessors);
					addPipeConstraints(v);
				}else if (v instanceof SeqPatt) {
					cplex.addEq(vars.get(1), 1);
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}

	private void addObjective() throws IloException {
		addObjective(g);
		cplex.addMinimize(obj);
//		cplex.addMinimize(expr);
	}

	private void addObjective(SkeletonPatt p) throws IloException {

//		if (p instanceof FarmPatt) {
			List<IloIntVar> vars = variables.get(p);
			
			obj =  cplex.sum(obj, vars.get(0));
		if(p.getChildren() != null) {
		for (SkeletonPatt c : p.getChildren()) {

			addObjective(c);
		}
		}
	}

	public List<SkeletonPatt> getSolutions() throws IloException {
		getSolutions(g);
		for (SkeletonPatt v : g.getChildren()) {
			if (v instanceof FarmPatt) {
				getSolutions(v);
			} else if (v instanceof CompPatt) {
				getSolutions(v);
			} else if (v instanceof PipePatt) {
				getSolutions(v);
			}
		}
		return result;
	}

	public void getSolutions(SkeletonPatt p) throws IloException {
		if (p instanceof FarmPatt ) {
			List<IloIntVar> vars = variables.get(p);
//			System.out.println(p);
//				System.out.println(" parDeg " +cplex.getValue(vars.get(1)));
//			System.out.println("objective " + cplex.getObjective().toString());
//				
//			System.out.println("model " + cplex.getModel().toString());
//				
//			System.out.println("alg " + cplex.getParameterSet().toString());
			double value = cplex.getValue(vars.get(0));
			int parDegree = (int) cplex.getValue(vars.get(1));
			p.setOptServiceTime(value);
			p.setOptParallelismDegree((int) parDegree);
			if (value >= 0.5) {
				result.add(p);
			}else {
				System.out.println("not resolved");
			}
		}
		if (p.getChildren() == null)
			return;
		for (SkeletonPatt d : p.getChildren()) {
			getSolutions(d);
		}

	}

	public void solveIt() throws IloException {
//		try {
		cplex.solve();
//		}catch(Exception e) {
//			System.out.println("Error +" + e.getMessage());
//		}
	}

	Consumer<SkeletonPatt> varCreator = s -> {
		IloIntVar tsi = null;
		IloIntVar n = null;
		try {
			int ts_i = (int) s.getIdealServiceTime();
			int n_i = s.getIdealParDegree() ==0? 1: s.getIdealParDegree();
			tsi = cplex.intVar(1 ,ts_i*n_i, "tsi");
			if(s instanceof SeqPatt || s instanceof CompPatt) {
				n = cplex.intVar(1, 1,"n");
			}else {
			n = cplex.intVar(1, numAvailableProcessors, "n");}
		} catch (IloException e) {
			System.out.println("Error at var creation"+ e.getMessage());
		}
		List<IloIntVar> vars = new ArrayList<IloIntVar>();
		vars.add(tsi);
		vars.add(n);

		variables.put(s, vars);
	};

	public void cleanup() throws IloException {
		cplex.clearModel();
		cplex.end();
	}
	private IloNumExpr addResources(SkeletonPatt f, IloNumExpr pipe) throws IloException {
		pipe = addResource(f, pipe);
		return pipe;
	}
	private IloNumExpr addResource(SkeletonPatt farm,IloNumExpr pipeStages ) throws IloException {
		
		pipeStages = cplex.sum(pipeStages,cplex.sum(getFarmResources(farm),2));

//		if( farmWorker instanceof FarmPatt)
//			pipeStages=addResource(farmWorker, pipeStages);
		return pipeStages;
	}
	private IloNumExpr getFarmResources(SkeletonPatt farm) throws IloException {
		IloNumExpr farmResource = cplex.constant(1);
		return getN(farm, farmResource);
	}
	private IloNumExpr getN(SkeletonPatt farm,IloNumExpr farmResources) throws IloException {
		IloIntVar n = variables.get(farm).get(1);
		SkeletonPatt farmWorker = farm.getChildren().get(0);
		farmResources = cplex.prod(n, variables.get(farmWorker).get(1));
		if( farmWorker instanceof FarmPatt)
			farmResources=getN(farmWorker, farmResources);
		return farmResources;
	}
}
