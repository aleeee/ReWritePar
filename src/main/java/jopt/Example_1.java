package jopt;

import ilog.concert.*;
import ilog.cp.IloCP;
import ilog.cplex.*;

import java.util.ArrayList;
import java.util.List;


public class Example_1 {
	public static void main(String[] args) {
		solveMe();
	}
	public static void solveMe() {
		try {
			IloCP cplex = new IloCP();
			
			// variables
			IloIntVar x = cplex.intVar(0, Integer.MAX_VALUE, "x");
			IloIntVar y = cplex.intVar(0, Integer.MAX_VALUE, "y");
			
			// expressions
			IloLinearNumExpr objective = cplex.linearNumExpr();
			objective.addTerm(1, x);
			objective.addTerm(1, y);
			
			// define objective
			cplex.addMinimize(objective);
			
			// define constraints
			List<IloRange> constraints = new ArrayList<IloRange>();
//			constraints.add(cplex.addGe(cplex.sum(cplex.prod(60, x),cplex.prod(60, y)), 300));
			cplex.addGe(x, 10);
			cplex.addGe(y, 1);
			cplex.addLe(cplex.prod(x, y), 100);
			
			cplex.addLe(cplex.div(x, y),4);
			
			// display option
//			cplex.setParam(IloCplex.Param.Simplex.Display, 0);
			
			// solve
			if (cplex.solve()) {
				System.out.println("obj = "+cplex.getObjValue());
				System.out.println("x   = "+cplex.getValue(x));
				System.out.println("y   = "+cplex.getValue(y));
			}
			else {
				System.out.println("Model not solved");
			}
			
			cplex.end();
		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}
}
