package cpo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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

public class CPOSolver2 {

	private Map<SkeletonPatt, List<IloNumVar>> variables;
	private IloCP cplex;
	private SkeletonPatt g;
	private int numAvailableProcessors;
	IloNumExpr expr;
	IloNumExpr obj;
	IloNumExpr pd_obj;
	IloNumExpr objTs;
	List<SkeletonPatt> result = new ArrayList<>();

	public CPOSolver2(SkeletonPatt skeletonPatt, int maxParDegree) throws IloException {

		this.cplex = new IloCP();
		expr = cplex.constant(0);
		obj = cplex.constant(0);
		pd_obj= cplex.constant(0);
		this.variables = new HashMap<SkeletonPatt, List<IloNumVar>>();
		this.g = skeletonPatt;
		this.numAvailableProcessors = maxParDegree;
		
		addVars(g);
		addConstraints(g);
		addObjective();
		String modelName = "C:\\Users\\me\\Desktop\\out\\cpo\\cpoModel1" + skeletonPatt.hashCode()
				+ skeletonPatt.getLable() + skeletonPatt.getIdealServiceTime() + ".cpo";
//		System.out.println(g + "modelName " + modelName);
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
			addFarmConstraints(p);
		}else if (p instanceof MapPatt) {
			addMapConstraints(p);
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
				List<IloNumVar> vars = variables.get(v);		
				IloNumVar ts_i = vars.get(0);
				IloIntVar n_i = (IloIntVar) vars.get(1);
				if (v instanceof FarmPatt) {
					cplex.addLe(n_i, numAvailableProcessors);
					pStages =addResources(v, pStages);
//					cplex.addGe(cplex.prod(ts_i, n_i),v.getIdealServiceTime() * v.getIdealParDegree());
					addFarmConstraints(v);
					
				}else if(v instanceof MapPatt) {
					cplex.addLe(n_i, numAvailableProcessors);
					pStages =addResources(v, pStages);
					addMapConstraints(v);
				}
				else if (v instanceof CompPatt) {
					addCompConstraints(v);
					pStages = cplex.sum(pStages, vars.get(1));
				} else if (v instanceof PipePatt) {
					addPipeConstraints(v);
					pStages = cplex.sum(pStages, vars.get(1));
				} else if (v instanceof SeqPatt) {
					cplex.addEq(vars.get(1), 1);
					pStages = cplex.sum(pStages, vars.get(1));
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
		try {
			for (SkeletonPatt v : p.getChildren()) {
				List<IloNumVar> vars = variables.get(v);		
				IloIntVar n_i = (IloIntVar) vars.get(1);
				cplex.addLe(n_i, numAvailableProcessors);
				if (v instanceof FarmPatt) {
					cplex.addLe(cplex.sum(n_i,2), numAvailableProcessors);
					addFarmConstraints(v);
				} else if (v instanceof MapPatt){
					cplex.addLe(cplex.sum(n_i,2), numAvailableProcessors);
					addMapConstraints(v );
				}
				else if (v instanceof CompPatt) {
					addCompConstraints(v);
				} else if (v instanceof PipePatt) {
					addPipeConstraints(v);
				}else if (v instanceof SeqPatt) {
				}
			}

		} catch (IloException e) {
			throw e;
		}
	}

	private void addFarmConstraints(SkeletonPatt p) throws IloException {
		List<IloNumVar> fVars = variables.get(p);
		IloIntVar pd = (IloIntVar) fVars.get(1);
		cplex.addLe(pd, numAvailableProcessors-2);
		cplex.addLe(pd, p.getIdealParDegree());

		try {
			for (SkeletonPatt v : p.getChildren()) {
					List<IloNumVar> vars = variables.get(v);		
					IloIntVar n_i = (IloIntVar) vars.get(1);
					cplex.addLe(n_i, numAvailableProcessors);
					
				if (v instanceof FarmPatt) {					
					cplex.addLe(cplex.sum(cplex.prod(pd ,vars.get(1)),2), numAvailableProcessors);
					addFarmConstraints(v);

				} else if(v instanceof MapPatt) {
					cplex.addLe(cplex.sum(cplex.prod(pd ,vars.get(1)),2), numAvailableProcessors);
					addMapConstraints(v);
				}
				else if (v instanceof CompPatt) {
					addCompConstraints(v);
				}else if (v instanceof PipePatt) {
					addPipeConstraints(v);
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}
	private void addMapConstraints(SkeletonPatt p) throws IloException {
		List<IloNumVar> fVars = variables.get(p);
		IloIntVar pd = (IloIntVar) fVars.get(1);
		cplex.addLe(pd, numAvailableProcessors-2);
		cplex.addLe(pd, p.getIdealParDegree());

		try {
			for (SkeletonPatt v : p.getChildren()) {
					List<IloNumVar> vars = variables.get(v);		
					IloIntVar n_i = (IloIntVar) vars.get(1);
					cplex.addLe(n_i, numAvailableProcessors);
					
				if (v instanceof FarmPatt) {					
					cplex.addLe(cplex.sum(cplex.prod(pd ,vars.get(1)),2), numAvailableProcessors);
					addFarmConstraints(v);

				} else if(v instanceof MapPatt) {
					cplex.addLe(cplex.sum(cplex.prod(pd ,vars.get(1)),2), numAvailableProcessors);
					addMapConstraints(v);
				}
				else if (v instanceof CompPatt) {
					addCompConstraints(v);
				}else if (v instanceof PipePatt) {
					addPipeConstraints(v);
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}

	private void addObjective() throws IloException {
		addObjective(g);
//		
//		cplex.minimize(cplex.staticLex(obj));
//		cplex.addMaximize(pd_obj);
		cplex.addMinimize(cplex.diff(obj, pd_obj));
	}

	private void addObjective(SkeletonPatt p) throws IloException {
		List<IloNumVar> vars = variables.get(p);
		IloIntVar pd = (IloIntVar) vars.get(1);
		if (p instanceof FarmPatt || p instanceof MapPatt) {
				double ts = p.getChildren().get(0).getIdealServiceTime();			
//				cplex.addGe(cplex.prod(vars.get(0) , vars.get(1)),p.getIdealServiceTime()*p.getIdealParDegree());	
				cplex.addEq(vars.get(0) , cplex.div( (int)ts,(IloIntExpr) vars.get(1)));
//				cplex.addGe(cplex.prod(pd,vars.get(0)) , ts);
				obj =  cplex.sum(obj, vars.get(0));
				pd_obj = cplex.sum(pd_obj,vars.get(1));
		}	
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
			} else if(v instanceof MapPatt) {
				getSolutions(v);
			}else  if (v instanceof CompPatt) {
				getSolutions(v);
			} else if (v instanceof PipePatt) {
				getSolutions(v);
			}
		}
		return result;
	}

	public void getSolutions(SkeletonPatt p) throws IloException {
		if (p instanceof FarmPatt || p instanceof MapPatt) {
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
		}
		if (p.getChildren() == null)
			return;
		for (SkeletonPatt d : p.getChildren()) {
			getSolutions(d);
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
			if(s instanceof SeqPatt || s instanceof CompPatt) {
				n = cplex.intVar(1, 1,"n");
//				n = cplex.intVar(1, numAvailableProcessors, "n");
			}else {
			n = cplex.intVar(1, numAvailableProcessors, "n");}
		} catch (IloException e) {
			System.out.println("Error at var creation"+ e.getMessage());
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
	private IloNumExpr addResources(SkeletonPatt f, IloNumExpr pipe) throws IloException {
		pipe = addResource(f, pipe);
		return pipe;
	}
	private IloNumExpr addResource(SkeletonPatt farm,IloNumExpr pipeStages ) throws IloException {
		
		pipeStages = cplex.sum(pipeStages,cplex.sum(getFarmResources(farm),2));

		return pipeStages;
	}
	private IloNumExpr getFarmResources(SkeletonPatt farm) throws IloException {
		IloNumExpr farmResource = cplex.constant(1);
		return getN(farm, farmResource);
	}
	private IloNumExpr getN(SkeletonPatt farm,IloNumExpr farmResources) throws IloException {
		IloIntVar n = (IloIntVar) variables.get(farm).get(1);
		SkeletonPatt farmWorker = farm.getChildren().get(0);
		farmResources = cplex.prod(n, variables.get(farmWorker).get(1));
		if( farmWorker instanceof FarmPatt || farmWorker instanceof MapPatt)
			farmResources=getN(farmWorker, farmResources);
		return farmResources;
	}
}
