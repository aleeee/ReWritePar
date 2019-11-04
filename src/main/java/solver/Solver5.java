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
import ilog.cplex.IloCplex;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class Solver5 {

	private Map<SkeletonPatt, List<IloNumVar>> variables;
	private IloCplex cplex;
	private SkeletonPatt g;
	private int numAvailableProcessors;
	List<IloRange> constraints = new ArrayList<IloRange>();
	IloNumExpr expr;
	IloNumExpr obj;
	List<SkeletonPatt> result = new ArrayList<>();

	public Solver5(SkeletonPatt skeletonPatt, int maxParDegree) throws IloException {

		this.cplex = new IloCplex();
		expr = cplex.constant(0);
		obj = cplex.constant(0);
		this.variables = new HashMap<SkeletonPatt, List<IloNumVar>>();
		this.g = skeletonPatt;
		this.numAvailableProcessors = maxParDegree;
		addVars(g);
		addConstraints(g);
		addObjective();
		String modelName = "C:\\Users\\me\\Desktop\\out\\cplexModel1" + skeletonPatt.hashCode()
				+ skeletonPatt.getLable() + skeletonPatt.getIdealServiceTime() + ".lp";
		System.out.println(g + "modelName " + modelName);
		cplex.exportModel(modelName);

		cplex.setOut(null);
	}

	private void addConstraints(SkeletonPatt p) throws IloException {

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
		for (SkeletonPatt v : p.getChildren()) {
			varCreator.accept(v);
			if (v instanceof FarmPatt) {
				addVariables(v);
			} else if (v instanceof CompPatt) {
				addVariables(v);
			} else if (v instanceof PipePatt) {
				addVariables(v);
			}
		}
	}

	private void addPipeConstraints(SkeletonPatt p) throws IloException {
		List<IloNumVar> pipeVars = variables.get(p);
		IloNumVar pipeParDegree = pipeVars.get(1);
		IloNumExpr pStages = cplex.constant(0);
		try {
			for (SkeletonPatt v : p.getChildren()) {
				List<IloNumVar> vars = variables.get(v);		
				pStages = cplex.sum(pStages, vars.get(1));
				if (v instanceof FarmPatt) {
					
					expr = cplex.sum(expr, vars.get(1));
					constraints.add(cplex.addLe(vars.get(1), v.getIdealParDegree()));
					constraints.add(cplex.addGe(cplex.prod(vars.get(0), vars.get(1)),v.getIdealServiceTime() * v.getIdealParDegree()));
					addFarmConstraints(v, false);
				} else if (v instanceof CompPatt) {
					addCompConstraints(v);
				} else if (v instanceof PipePatt) {
					addPipeConstraints(v);
				} else if (v instanceof SeqPatt) {
					constraints.add(cplex.addEq(vars.get(1), 1));
					expr = cplex.sum(expr, vars.get(1));
				}
			}
			cplex.addEq(pipeParDegree, pStages);
		} catch (IloException e) {
			throw e;
		}
	}

	private void addCompConstraints(SkeletonPatt p) throws IloException {
		List<IloNumVar> compVars = variables.get(p);
		
//		cplex.addEq(compVars.get(1), 1);
		try {
			for (SkeletonPatt v : p.getChildren()) {
				List<IloNumVar> vars = variables.get(v);
				constraints.add(cplex.addLe(vars.get(1), numAvailableProcessors));
				if (v instanceof FarmPatt) {
					constraints.add(cplex.addLe(vars.get(1), v.getIdealParDegree()));
					constraints.add(cplex.addGe(cplex.prod(vars.get(0), vars.get(1)),v.getIdealServiceTime() * v.getIdealParDegree()));
					addFarmConstraints(v, true);
				} else if (v instanceof CompPatt) {
					addCompConstraints(v);
				} else if (v instanceof PipePatt) {
					addPipeConstraints(v);
				}else if (v instanceof SeqPatt) {
					constraints.add(cplex.addEq(vars.get(1), 1));
				}
			}

		} catch (IloException e) {
			throw e;
		}
	}

	private void addFarmConstraints(SkeletonPatt p, boolean reusable) throws IloException {

//				List<IloNumVar> vars = variables.get(p);
//				if(reusable) {
//					constraints.add(cplex.addLe(vars.get(1), numAvailableProcessors));
//
//				}else {
//					expr = cplex.sum(expr, vars.get(1));
//				}
//				
//				SkeletonPatt stage = p.getChildren().get(0);
//				constraints.add(cplex.addLe( vars.get(1), p.getIdealParDegree()));
//				constraints.add(cplex.addGe(cplex.prod(vars.get(0), vars.get(1)), p.getIdealServiceTime()*p.getIdealParDegree()));
		try {
			for (SkeletonPatt v : p.getChildren()) {
				List<IloNumVar> vars = variables.get(v);

				if(reusable) {
					constraints.add(cplex.addLe(vars.get(1), numAvailableProcessors));

				}
				if (v instanceof FarmPatt) {
					if(!reusable) {
						expr = cplex.sum(expr, vars.get(1));
//						constraints.add(cplex.addLe(variables.get(p).get(1) , numAvailableProcessors);
					}
					constraints.add(cplex.addLe(cplex.prod(variables.get(p).get(1) ,vars.get(1)), numAvailableProcessors));
					constraints.add(cplex.addLe(vars.get(1), v.getIdealParDegree()));
					constraints.add(cplex.addGe(cplex.prod(vars.get(0), vars.get(1)),v.getIdealServiceTime() * v.getIdealParDegree()));
					addFarmConstraints(v, reusable);

				} else if (v instanceof CompPatt) {
					addCompConstraints(v);
				} else if (v instanceof PipePatt) {
					constraints.add(cplex.addLe(cplex.prod(variables.get(p).get(1) ,vars.get(1)), numAvailableProcessors));

					addPipeConstraints(v);
				}else if (v instanceof SeqPatt) {
					constraints.add(cplex.addEq(vars.get(1), 1));
//					expr = cplex.sum(expr, vars.get(1));
				}
			}

		} catch (IloException e) {
			throw e;
		}
	}

	private void addObjective() throws IloException {
		addObjective(g);
		cplex.addMinimize(obj);
//		cplex.addMinimize(expr);
	}

	private void addObjective(SkeletonPatt p) throws IloException {

		if (p instanceof FarmPatt) {
			List<IloNumVar> vars = variables.get(p);

			obj = cplex.sum(obj, vars.get(0));
		}
		if (p.getChildren() == null)
			return;
		for (SkeletonPatt c : p.getChildren()) {

			addObjective(c);
		}

	}

	public List<SkeletonPatt> getSolutions() throws IloException {

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
		if (p instanceof FarmPatt) {
			List<IloNumVar> vars = variables.get(p);
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
			tsi = cplex.numVar(1, (s.getIdealServiceTime() * s.getIdealParDegree()), "tsi");
			n = cplex.intVar(1, numAvailableProcessors, "n");
		} catch (IloException e) {
			System.out.println(e.getMessage());
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