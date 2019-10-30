package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.PipePatt;
import tree.model.SkeletonPatt;

public class Solver {

	private Map<SkeletonPatt, List<IloNumVar>> variables;
	private IloCplex cplex;
	private SkeletonPatt g;
	private int numAvailableProcessors;
	List<IloRange> constraints = new ArrayList<IloRange>();
	IloNumExpr expr ;
	
	public Solver(SkeletonPatt skeletonPatt, int maxParDegree)
			throws IloException {
		super();
		
		this.cplex = new IloCplex();
		expr = cplex.constant(0);
		this.variables = new HashMap<SkeletonPatt, List<IloNumVar>>();
		this.g = skeletonPatt;
		this.numAvailableProcessors = maxParDegree;
		addVariables(g);
		addConstraints(g);
		addObjective();
		
		cplex.exportModel("C:\\Users\\me\\Desktop\\out\\cplexModel1"+skeletonPatt.hashCode()+".lp");

//		cplex.setOut(null);
	}
	private void addConstraints(SkeletonPatt p) throws IloException {
		
		if(p instanceof PipePatt) {
			addPipeConstraints(p);
		}else if(p instanceof CompPatt){
			addCompConstraints(p);
		}else if(p instanceof FarmPatt) {
			addFarmConstraints(p);
		}
		cplex.addLe(expr, numAvailableProcessors);

		
	}
	private void addPipeConstraints(SkeletonPatt p) throws IloException {

		try {
			for (SkeletonPatt v : p.getChildren()) {
				System.out.println(v);
				List<IloNumVar> vars = variables.get(v);
				System.out.println(vars);
				expr = cplex.sum(expr, vars.get(1));
				if (v instanceof FarmPatt) {
					constraints.add(cplex.addGe(
							cplex.prod(vars.get(0), vars.get(1)), v.getIdealServiceTime()*v.getIdealParDegree()));
					addFarmConstraints(v);
				}else if(v instanceof CompPatt) {
					addCompConstraints(v);
				}else if(v instanceof PipePatt) {
					addPipeConstraints(v);
				}
			}
//			cplex.addLe(expr, numAvailableProcessors);

		} catch (IloException e) {
			throw e;
		}
	}

	private void addCompConstraints(SkeletonPatt p) throws IloException {

		try {
			for (SkeletonPatt v : p.getChildren()) {
				List<IloNumVar> vars = variables.get(v);
				constraints.add(cplex.addGe(numAvailableProcessors, vars.get(1)));
				expr = cplex.sum(expr, vars.get(1));
				if (v instanceof FarmPatt) {
					constraints.add(cplex.addGe(
							cplex.prod(vars.get(0), vars.get(1)), v.getIdealServiceTime()*v.getIdealParDegree()));
					addFarmConstraints(v);
				}else if(v instanceof CompPatt) {
					addCompConstraints(v);
				}else if(v instanceof PipePatt) {
					addPipeConstraints(v);
				}
			}

		} catch (IloException e) {
			throw e;
		}
	}

	//create variables for all nodes
	private void addVariables(SkeletonPatt p) {
		for (SkeletonPatt v : p.getChildren()) {
			varCreator.accept(v);
			if (v instanceof FarmPatt) {
				addVariables(v);
			}else if(v instanceof CompPatt) {
				addVariables(v);
			}else if(v instanceof PipePatt) {
				addVariables(v);
			}
		}
	}

	private void addFarmConstraints(SkeletonPatt p) throws IloException {
		try {
			List<IloRange> constraints = new ArrayList<IloRange>();
			IloNumExpr expr = cplex.constant(0);

			for (SkeletonPatt v : p.getChildren()) {
				List<IloNumVar> vars = variables.get(v);
				expr = cplex.sum(expr, vars.get(1));
				if (v instanceof FarmPatt) {
					constraints.add(cplex.addGe(
							cplex.prod(vars.get(0), vars.get(1)), v.getIdealServiceTime()*v.getIdealParDegree()));
					addFarmConstraints(v);
				}else if(v instanceof CompPatt) {
					addCompConstraints(v);
				}else if(v instanceof PipePatt) {
					addPipeConstraints(v);
				}
			}
			
		} catch (IloException e) {
			throw e;
		}
	}

	private void addObjective() throws IloException {
		IloNumExpr obj = cplex.constant(0);
		for (SkeletonPatt sp : g.getChildren()) {
			List<IloNumVar> vars = variables.get(sp);

			obj = cplex.sum(obj, vars.get(0));
		}
		cplex.addMinimize(obj);

	}
	

	public List<SkeletonPatt> getSolutions() throws IloException {
		List<SkeletonPatt> result = new ArrayList<>();
		for (SkeletonPatt p : g.getChildren()) {
			List<IloNumVar> vars = variables.get(p);
//		System.out.println( p.getServiceTime() +" ts " +cplex.getValue(vars.get(0)));
//		System.out.println(" parDeg " +cplex.getValue(vars.get(1)));
//		System.out.println("objective " +cplex.getObjective().toString());
//		
//		System.out.println("model " + cplex.getModel().toString());
//		
//		System.out.println("alg "+ cplex.getParameterSet().toString());
			double value = cplex.getValue(vars.get(0));
			int parDegree = (int) cplex.getValue(vars.get(1));
			p.setOptServiceTime(value);
			p.setOptParallelismDegree((int) parDegree);
			if (value >= 0.5) {
				result.add(p);
			}
		}
		return result;

	}

	public void solveIt() throws IloException {
		cplex.solve();
	}

	Consumer<SkeletonPatt> varCreator = s -> {
		IloNumVar tsi = null, n = null;
		try {
			tsi = cplex.numVar(1, s.getIdealServiceTime(), "tsi");
			n = cplex.numVar(1, numAvailableProcessors, "n");
		} catch (IloException e) {
			System.out.println(e.getMessage());
		}
		List<IloNumVar> vars = new ArrayList<IloNumVar>();
		vars.add(tsi);
		vars.add(n);

		variables.put(s, vars);
	};

	public void cleanup() throws IloException
	{
		cplex.clearModel();
		cplex.end();
	}
}
