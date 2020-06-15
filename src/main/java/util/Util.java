package util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import cpo.CPOSolver2;
import cpo.CPOSolverV;
import cpo.ILOCPOSolver;
import ilog.concert.IloException;
import pattern.skel4.Skel4Parser.AssignmentContext;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class Util {
	final  Logger log = LoggerFactory.getLogger(Util.class);
	public Util() {
		
	}
	enum SkeletonType {F,P, S, C, M};
//	 int n = 256;

	public  SkeletonPatt getType(AssignmentContext ctx) {
		String type = ctx.expr.sType.getText();
		switch (type) {
		case "Seq":
			SeqPatt s = new SeqPatt(Integer.parseInt(ctx.expr.sequence().ts.getText()),(ctx.varName.getText() != null? ctx.varName.getText(): "seq"));
			return s;

		case "Comp":
			return  new CompPatt();
		case "Farm":
			return new FarmPatt();
		case "Pipe":
			return new PipePatt();
		case "Map":
			return new MapPatt();
		}
		return null;
	}

	
 private  double roundToTwoDecimal(double n) {
	 try {
	 BigDecimal stime = BigDecimal.valueOf(n);
	  stime = stime.setScale(2, RoundingMode.HALF_UP);
	    return stime.doubleValue();
	 }catch(Exception e) {
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
	public  double getServiceTime(PipePatt pat) {
		return roundToTwoDecimal(pat.getChildren().stream().mapToDouble(SkeletonPatt::getIdealServiceTime).reduce(0,
				(c1, c2) -> c1 > c2 ? c1 : c2));

	}
	/**
	 * calculate optimal ts
	 * @param pat
	 * @return
	 */
	public  double getOptimalServiceTime(PipePatt pat) {
//		pat.getChildren().forEach(p-> System.out.println(p.getLable()+" " +p.calculateOptimalServiceTime()));
		return roundToTwoDecimal( pat.getChildren().stream().mapToDouble(SkeletonPatt::calculateOptimalServiceTime).reduce(0,
				(c1, c2) -> c1 > c2 ? c1 : c2));

	}
	/**
	 * calculates the service time of Comp pattern
	 * 
	 * @param pat
	 * @return
	 */
	public  double getServiceTime(CompPatt pat) {
		return roundToTwoDecimal( pat.getChildren().stream().mapToDouble(SkeletonPatt::getIdealServiceTime).reduce(0, (c1, c2) -> c1 + c2));
	}
	/**
	 * calculate the optimal ts
	 * @param pat
	 * @return
	 */
	public  double getOptimalServiceTime(CompPatt pat) {
		return roundToTwoDecimal(pat.getChildren().stream().mapToDouble(SkeletonPatt::calculateOptimalServiceTime).reduce(0, (c1, c2) -> c1 + c2));
	}
	/**
	 * calculates the ideal service time of Farm
	 * 
	 * @param pat
	 * @return
	 */
	public  double getServiceTime(FarmPatt pat) {
		try {
		SkeletonPatt farmWorker = pat.getChildren().get(0);
		farmWorker.calculateIdealServiceTime();
		int parallelismDegree = (int) (farmWorker.getIdealServiceTime()/Constants.TEmitter);
		pat.setIdealParDegree(parallelismDegree);
		return roundToTwoDecimal( Math.max(Math.max(Constants.TEmitter,Constants.TCollector),farmWorker.getIdealServiceTime()/pat.getIdealParDegree()));
		}catch (Exception e) {
			log.error(e.getMessage() );
			return 0;
		}
		}
	/**
	 * calculate optimized ts
	 * @param pat
	 * @return
	 */
	public  double getOptimizedTs(FarmPatt pat) {
		SkeletonPatt farmWorker = pat.getChildren().get(0);
		farmWorker.calculateOptimalServiceTime();
		return roundToTwoDecimal(Math.max(Math.max(Constants.TEmitter,Constants.TCollector),farmWorker.calculateOptimalServiceTime()/pat.getOptParallelismDegree()));
	}
	/**
	 * calculates the service time of Map
	 * 
	 * @param pat
	 * @return
	 */
	public  double getServiceTime(MapPatt pat) {
		SkeletonPatt mapWorker = pat.getChildren().get(0);
		int parallelismDegree = (int) Math.sqrt(mapWorker.getIdealServiceTime()/Math.max(Constants.TScatter, Constants.TGather));
		pat.setIdealParDegree(parallelismDegree);
		return roundToTwoDecimal(mapWorker.getIdealServiceTime()/pat.getIdealParDegree());
	}
	/**
	 * calculate the optimal service time
	 * @param pat
	 * @return
	 */
	public  double getOptimalServiceTime(MapPatt pat) {
//		SkeletonPatt mapWorker = pat.getChildren().get(0);
//		return mapWorker.calculateOptimalServiceTime()/pat.getOptParallelismDegree();
		SkeletonPatt mapWorker = pat.getChildren().get(0);
		mapWorker.calculateOptimalServiceTime();
		return roundToTwoDecimal( Math.max(Math.max(Constants.TEmitter,Constants.TCollector),mapWorker.calculateOptimalServiceTime()/pat.getOptParallelismDegree()));
	
	}

	/**
	 * creates n trees from the input tree replacing input node with its n rewriting
	 * (refactoring option) pattern
	 * since the refactoring is at pattern level, it creates the tree structure by replacing one childNode with it's
	 * refactoring options so if it has N rewriting options, the N trees will be created
	 * @param parent
	 * @param node
	 * @return
	 */
	public  Set<SkeletonPatt> createTreeNode(SkeletonPatt parent, SkeletonPatt node) {
		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
		for (SkeletonPatt p : node.getPatterns()) {
			ArrayList<SkeletonPatt> sc = new ArrayList<SkeletonPatt>();

			SkeletonPatt newP = null;
			try {
				newP = parent.getClass().getDeclaredConstructor().newInstance();
			} catch ( Exception e1) {
				log.error("Error init object " + e1.getMessage());
			}

//			sc.addAll(parent.getChildren());
			sc.addAll(parent.getChildren().stream().map(o -> clone(o)).collect(Collectors.toList()));
//			newP.setParent(parent);
//			newP.setLable(parent.getLable());
			newP.setDepth(p.getDepth());
			newP.setChildren(sc);
			newP.getChildren().set(newP.getChildren().indexOf(node), p);
			newP.setReWritingRule(p.getRule());
			newP.calculateIdealServiceTime();
			newP.setReWriteNodes(false);
			patterns.add(newP);
//			System.out.println(newP);
//			if(DiGraphGen3.g.containsVertex(newP)) {
////				System.out.println(newP + "already exists");
//			}else {
//			DiGraphGen3.g.addVertex(newP);}
//			if(newP.getChildren() != null) {
//			int depth = getMaxDepth(newP);
////			System.out.println("depth: : #@ " +depth + "  " + newP);
			}
//			CPOSolver model;
//			try {
//				newP.calculateIdealServiceTime();
//				model = new CPOSolver(newP, 16);
//				model.solveIt();
//			// Print the solution
//			model.getSolutions();
//			newP.calculateOptimalServiceTime();
//			model.cleanup();
//			} catch (IloException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			// Solve the model
//			
//		}
//		Set<SkeletonPatt> best = new HashSet<SkeletonPatt>();
//		best.add(patterns.stream().min(Comparator.comparing(SkeletonPatt::getOptServiceTime)).get());
//		return best;
		return patterns;
//		optimalPattern.add(patterns.stream().min(Comparator.comparing(SkeletonPatt::getIdealServiceTime)).get());
//		return optimalPattern;
	}
	
	public  int getHeight(SkeletonPatt pat) {
		int height = 0;
		if (pat == null)
			return height;
		if(pat.getChildren() == null) 
			return 1;
		for(SkeletonPatt sk: pat.getChildren()) {			
			height = Math.max(height, getHeight(sk));
		}
		return height +1;
	}
	
	public  SkeletonPatt clone(SkeletonPatt original) {
		SkeletonPatt copy = null;
		try {
			if(original instanceof SeqPatt) {
				copy = new SeqPatt((SeqPatt)original);
			}else {
			copy = original.getClass().getDeclaredConstructor().newInstance();
		
			copy.setChildren((ArrayList<SkeletonPatt>) original.getChildren().stream().map(o -> clone(o)).collect(Collectors.toList()));
			copy.setLable(original.getLable());
			}
			copy.setReWritingRule(original.getRule());
			copy.setIdealServiceTime(original.getIdealServiceTime());
			copy.calculateIdealServiceTime();
			return copy;
		} catch (Exception e1) {
			log.error("Error copying object " + e1.getMessage());
			System.exit(1);
			return original;
		}
	
	}
	 int sum=0;
	public  int getNumberOfResources(SkeletonPatt pat) {
		 sum =+pat.getOptParallelismDegree();
		
		if( pat.getChildren() ==null) 
			return sum;
		
		for(SkeletonPatt sk: pat.getChildren()) {			
			   sum +=  getNumberOfResources(sk);
		}
		return sum;
	}
	
	public  double getCost(SkeletonPatt p,int maxNumberOfResources) {
		if(p instanceof SeqPatt) return p.getIdealServiceTime();
//		CPOSolver2 model;
		CPOSolverV model;
		try {
			p.calculateIdealServiceTime();
			model = new CPOSolverV(p, maxNumberOfResources);
			model.solveIt();
			model.getSolutions(p);
			p.calculateOptimalServiceTime();
			model.cleanup();
//			Thread.sleep(20);
		} catch (Exception e) {
			log.warn("No solution " + e.getMessage());
		}
		return p.getOptServiceTime();
	}
//	public synchronized  double getCost(SkeletonPatt p,int maxNumberOfResources) {
//		if(p instanceof SeqPatt) return p.getIdealServiceTime();
////		CPOSolver2 model;
//		CPOSolverV model;
//		try {
//			p.calculateIdealServiceTime();
//			ILOCPOSolver.init();
//			ILOCPOSolver.setNumAvailableProcessors(maxNumberOfResources);
//			ILOCPOSolver.setSkeleton(p);
//			ILOCPOSolver.addVars();
//			ILOCPOSolver.addConstraints();
//			ILOCPOSolver.addObjective();
//			ILOCPOSolver.solveIt();
//			ILOCPOSolver.getSolutions();
//			p.calculateOptimalServiceTime();
//			ILOCPOSolver.cleanup();
//			
////			model = new CPOSolverV(p, maxNumberOfResources);
//		
//		} catch (Exception e) {
//			log.warn("No solution " + e.getMessage());
//		}
//		return p.getOptServiceTime();
//	}
	public  <T> boolean detectLoop(SkeletonPatt pat, T s) {
//		System.out.println("detecting...." + pat);
		if (pat == null  || pat.getParent() == null) 
			return false;
		
		if(pat.getParent().getClass() == s) {
			return true;
		}
		return detectLoop(pat.getParent(),s);
	}
	
}
