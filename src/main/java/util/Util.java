package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpo.CPOSolverV;
import pattern.skel4.SkeletonParser.AssignmentContext;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class Util {
	final Logger log = LoggerFactory.getLogger(Util.class);
	
	public Util() {
		
	}

	public SkeletonPatt getType(AssignmentContext ctx) {
		String type = ctx.expr.sType.getText();
		switch (type) {
		case "Seq":
			SeqPatt s = new SeqPatt(Integer.parseInt(ctx.expr.sequence().ts.getText()),
					(ctx.varName.getText() != null ? ctx.varName.getText() : "seq"));
			return s;
		case "Comp":
			return new CompPatt();
		case "Farm":
			return new FarmPatt();
		case "Pipe":
			return new PipePatt();
		case "Map":
			return new MapPatt();
		}
		return null;
	}

	private double roundToTwoDecimal(double n) {
		try {
			BigDecimal stime = BigDecimal.valueOf(n);
			stime = stime.setScale(2, RoundingMode.HALF_UP);
			return stime.doubleValue();
		} catch (Exception e) {
			System.out.println("Error rounding number " + e.getMessage());
		}
		return n;
	}

	/**
	 * calculates the service time of pipeline
	 * 
	 * @param pat
	 * @return
	 */
	public double getServiceTime(PipePatt pat) {
		return roundToTwoDecimal(pat.getChildren().stream().mapToDouble(SkeletonPatt::getIdealServiceTime).reduce(0,
				(c1, c2) -> c1 > c2 ? c1 : c2));

	}
	
	public double getCompletionTime(PipePatt pat) {
		return ((pat.getChildren().size() -1) * pat.getOptServiceTime()) + (Constants.InputSize * pat.getOptServiceTime());
	}
	
	public double getLatency(PipePatt skel) {
		return roundToTwoDecimal(skel.getChildren().stream().mapToDouble(SkeletonPatt::getLatency).reduce(0,
				(c1, c2) -> c1 + c2)); 
	}
	/**
	 * calculate optimal ts
	 * 
	 * @param pat
	 * @return
	 */
	public double getOptimalServiceTime(PipePatt pat) {
		return roundToTwoDecimal(pat.getChildren().stream().mapToDouble(SkeletonPatt::calculateOptimalServiceTime)
				.reduce(0, (c1, c2) -> c1 > c2 ? c1 : c2));

	}

	/**
	 * calculates the service time of Comp pattern
	 * 
	 * @param pat
	 * @return
	 */
	public double getServiceTime(CompPatt pat) {
		return roundToTwoDecimal(pat.getChildren().stream().mapToDouble(SkeletonPatt::getIdealServiceTime).reduce(0,
				(c1, c2) -> c1 + c2));
	}

	/**
	 * calculate the optimal ts
	 * 
	 * @param pat
	 * @return
	 */
	public double getOptimalServiceTime(CompPatt pat) {
		return roundToTwoDecimal(pat.getChildren().stream().mapToDouble(SkeletonPatt::calculateOptimalServiceTime)
				.reduce(0, (c1, c2) -> c1 + c2));
	}

	/**
	 * calculates the ideal service time of Farm
	 * 
	 * @param pat
	 * @return
	 */
	public double getServiceTime(FarmPatt pat) {
		try {
			SkeletonPatt farmWorker = pat.getChildren().get(0);
			farmWorker.calculateIdealServiceTime();
			int parallelismDegree = (int) (farmWorker.getIdealServiceTime() / Constants.TEmitter);
			pat.setIdealParDegree(parallelismDegree);
			return roundToTwoDecimal(Math.max(Math.max(Constants.TEmitter, Constants.TCollector),
					farmWorker.getIdealServiceTime() / pat.getIdealParDegree()));
		} catch (Exception e) {
			log.error(e.getMessage());
			return 0;
		}
	}

	/**
	 * calculate optimized ts
	 * 
	 * @param pat
	 * @return
	 */
	public double getOptimizedTs(FarmPatt pat) {
		SkeletonPatt farmWorker = pat.getChildren().get(0);
		farmWorker.calculateOptimalServiceTime();
		return roundToTwoDecimal(Math.max(Math.max(Constants.TEmitter, Constants.TCollector),
				farmWorker.calculateOptimalServiceTime() / pat.getOptParallelismDegree()));
	}
	
	public double getComletiontime(FarmPatt skel, int size) {
		return size * skel.getOptServiceTime();
	}
	
	public double getLatency(FarmPatt skel) {
		return Constants.TEmitter + Constants.TCollector + skel.getChildren().get(0).getLatency();
	}

	/**
	 * calculates the service time of Map
	 * 
	 * @param pat
	 * @return
	 */
	public double getIdealServiceTime(MapPatt pat) {
		SkeletonPatt mapWorker = pat.getChildren().get(0);
		int parallelismDegree = (int) Math
				.sqrt(mapWorker.getIdealServiceTime() / Math.max(Constants.TScatter, Constants.TGather));
		pat.setIdealParDegree(parallelismDegree);
		return roundToTwoDecimal(mapWorker.getIdealServiceTime() *(Constants.InputSize /parallelismDegree));
	}


	/**
	 * calculate the optimal service time
	 * 
	 * @param pat
	 * @return
	 */
	public double getOptimalServiceTime(MapPatt pat) {
		SkeletonPatt mapWorker = pat.getChildren().get(0);
		mapWorker.calculateOptimalServiceTime();
		int parallelismDegree = (int) Math
				.sqrt(mapWorker.getOptServiceTime() / Math.max(Constants.TScatter, Constants.TGather));
		pat.setIdealParDegree(parallelismDegree);
		return roundToTwoDecimal(Math.max(Math.max(Constants.TEmitter, Constants.TCollector),
				mapWorker.calculateOptimalServiceTime() *(Constants.InputSize /parallelismDegree)));

	}

	/**
	 * creates n trees from the input tree replacing input node with its n rewriting
	 * (refactoring option) pattern since the refactoring is at pattern level, it
	 * creates the tree structure by replacing one childNode with it's refactoring
	 * options so if it has N rewriting options, the N trees will be created
	 * 
	 * @param parent
	 * @param node
	 * @return
	 */
	public Set<SkeletonPatt> createTreeNode(SkeletonPatt parent, SkeletonPatt node) {
		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
		for (SkeletonPatt p : node.getPatterns()) {
			ArrayList<SkeletonPatt> sc = new ArrayList<SkeletonPatt>();

			SkeletonPatt newP = null;
			try {
				newP = parent.getClass().getDeclaredConstructor().newInstance();
			} catch (Exception e1) {
				log.error("Error init object " + e1.getMessage());
			}
			sc.addAll(parent.getChildren().stream().map(o -> clone(o)).collect(Collectors.toList()));

			newP.setDepth(p.getDepth());
			newP.setChildren(sc);
			newP.getChildren().set(newP.getChildren().indexOf(node), p);
			newP.setReWritingRule(p.getRule());
			newP.setIdealServiceTime(parent.getIdealServiceTime());
			newP.setIdealServiceTime(parent.getIdealParDegree());
			newP.calculateIdealServiceTime();
			newP.setReWriteNodes(false);
			patterns.add(newP);

		}

		return patterns;

	}

	public int getHeight(SkeletonPatt pat) {
		int height = 0;
		if (pat == null)
			return height;
		if (pat.getChildren() == null)
			return 1;
		for (SkeletonPatt sk : pat.getChildren()) {
			height = Math.max(height, getHeight(sk));
		}
		return height + 1;
	}

	public SkeletonPatt clone(SkeletonPatt original) {
		SkeletonPatt copy = null;
		try {
			if (original instanceof SeqPatt) {
				copy = new SeqPatt((SeqPatt) original);
			} else {
				copy = original.getClass().getDeclaredConstructor().newInstance();

				copy.setChildren((ArrayList<SkeletonPatt>) original.getChildren().stream().map(o -> clone(o))
						.collect(Collectors.toList()));
				copy.setLable(original.getLable());
			}
			copy.setReWritingRule(original.getRule());
			copy.setIdealServiceTime(original.getIdealServiceTime());
			copy.setOptParallelismDegree(original.getOptParallelismDegree());
			copy.setOptServiceTime(original.getOptServiceTime());
			copy.calculateIdealServiceTime();
			copy.calculateOptimalServiceTime();
			copy.getNumberOfResources();
			return copy;
		} catch (Exception e1) {
			log.error("Error copying object " + e1.getMessage());
			return original;
		}

	}

	int sum = 0;

	public int getNumberOfResources(SkeletonPatt pat) {
		sum = +pat.getOptParallelismDegree();

		if (pat.getChildren() == null)
			return sum;

		for (SkeletonPatt sk : pat.getChildren()) {
			sum += getNumberOfResources(sk);
		}
		return sum;
	}

	public double getCost(SkeletonPatt p, int maxNumberOfResources) {
		if (p instanceof SeqPatt)
			return p.getIdealServiceTime();
		CPOSolverV model;
		try {
			p.calculateIdealServiceTime();
			model = new CPOSolverV(p, maxNumberOfResources);
			model.solveIt();
			model.getSolutions(p);
			p.calculateOptimalServiceTime();
//			model.cleanup();
		} catch (Exception e) {
			log.warn("No solution " + e.getMessage());
		}
		return p.getOptServiceTime();
	}

//	public <T> boolean detectLoop(SkeletonPatt pat, T s) {
//		if (pat == null || pat.getParent() == null)
//			return false;
//
//		if (pat.getParent().getClass() == s) {
//			return true;
//		}
//		return detectLoop(pat.getParent(), s);
//	}

}
