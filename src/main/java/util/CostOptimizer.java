package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpo.CPOSolverV;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public final class CostOptimizer {
 Logger log = LoggerFactory.getLogger(getClass());
 
	public static synchronized double getCost(SkeletonPatt p, int maxNumberOfResources) {
		if (p instanceof SeqPatt)
			return p.getIdealServiceTime();
		CPOSolverV model;
		try {
			p.calculateIdealServiceTime();
			model = new CPOSolverV(p, maxNumberOfResources);
			model.solveIt();
			model.getSolutions(p);
			p.calculateOptimalServiceTime();
			model.cleanup();
		} catch (Exception e) {
			System.out.println("Error solving cost " + e.getMessage());
		}
		return p.getOptServiceTime();
	}
}
