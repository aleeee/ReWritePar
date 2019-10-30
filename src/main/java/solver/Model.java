package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import graph.DiGraphGen3;
import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import tree.model.SkeletonPatt;

public class Model {

	private Map<SkeletonPatt, List<IloNumVar>> variables;
	private IloCplex cplex;
	private SkeletonPatt g;
	private int numAvailableProcessors;

	public Model(SkeletonPatt skeletonPatt, int maxParDegree)
			throws IloException {
		super();
		
		this.cplex = new IloCplex();

		this.variables = new HashMap<SkeletonPatt, List<IloNumVar>>();
		this.g = skeletonPatt;
		this.numAvailableProcessors = maxParDegree;

		addVariables();
		addConstraints();
		addObjective();

		cplex.exportModel("C:\\Users\\me\\Desktop\\out\\cplexModel.lp");

		cplex.setOut(null);
	}

	private void addVariables() {
		g.getChildren().forEach(varCreator);

	}

	private void addConstraints() throws IloException {
		try {
			List<IloRange> constraints = new ArrayList<IloRange>();
			IloNumExpr expr = cplex.constant(0);

			for(SkeletonPatt v : g.getChildren()){
				List<IloNumVar> vars = variables.get(v);

				expr = cplex.sum(expr, vars.get(1));

				constraints.add(cplex.addGe(cplex.prod(vars.get(0), vars.get(1)), v.getIdealServiceTime()));
			}
			cplex.addLe(expr, numAvailableProcessors);
		} catch (IloException e) {
			throw e;
		}
	}

	private void addObjective() throws IloException {
		IloNumExpr obj = cplex.constant(0);
		for(SkeletonPatt sp : g.getChildren()) {
			List<IloNumVar> vars = variables.get(sp);
			
			obj = cplex.sum(obj, vars.get(0));
		}
		cplex.addMinimize(obj);

	}
	public List<SkeletonPatt> getSolutions() throws IloException{
		List<SkeletonPatt> result = new ArrayList<>();
		for (SkeletonPatt p :g.getChildren()){
			List<IloNumVar> vars = variables.get(p);
//			System.out.println( p.getServiceTime() +" ts " +cplex.getValue(vars.get(0)));
//			System.out.println(" parDeg " +cplex.getValue(vars.get(1)));
//			System.out.println("objective " +cplex.getObjective().toString());
//			
//			System.out.println("model " + cplex.getModel().toString());
//			
//			System.out.println("alg "+ cplex.getParameterSet().toString());
			double value = cplex.getValue(vars.get(0));
			int parDegree = (int) cplex.getValue(vars.get(1));
			p.setOptServiceTime(value);
			p.setOptParallelismDegree((int)parDegree);
			if (value >= 0.5)
			{
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

}
