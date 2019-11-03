package util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.util.Elements.Origin;

import com.google.common.collect.Sets;
import graph.DiGraphGen2;
import graph.DiGraphGen3;
import pattern.skel4.Skel4Parser.AssignmentContext;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class Util {
	static int n = 256;

	public static SkeletonPatt getType(AssignmentContext ctx) {
		String type = ctx.expr.sType.getText();
		switch (type) {
		case "Seq":
			SeqPatt s = new SeqPatt(Integer.parseInt(ctx.expr.sequence().ts.getText()),ctx.varName.getText());
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

	

	/**
	 * calculates the service time of pipeline
	 * 
	 * @param pat
	 * @return
	 */
	public static double getServiceTime(PipePatt pat) {
		return pat.getChildren().stream().mapToDouble(SkeletonPatt::getIdealServiceTime).reduce(0,
				(c1, c2) -> c1 > c2 ? c1 : c2);

	}
	/**
	 * calculate optimal ts
	 * @param pat
	 * @return
	 */
	public static double getOptimalServiceTime(PipePatt pat) {
//		pat.getChildren().forEach(p-> System.out.println(p.getLable()+" " +p.calculateOptimalServiceTime()));
		return pat.getChildren().stream().mapToDouble(SkeletonPatt::calculateOptimalServiceTime).reduce(0,
				(c1, c2) -> c1 > c2 ? c1 : c2);

	}
	/**
	 * calculates the service time of Comp pattern
	 * 
	 * @param pat
	 * @return
	 */
	public static double getServiceTime(CompPatt pat) {
		return pat.getChildren().stream().mapToDouble(SkeletonPatt::getIdealServiceTime).reduce(0, (c1, c2) -> c1 + c2);
	}
	/**
	 * calculate the optimal ts
	 * @param pat
	 * @return
	 */
	public static double getOptimalServiceTime(CompPatt pat) {
		return pat.getChildren().stream().mapToDouble(SkeletonPatt::calculateOptimalServiceTime).reduce(0, (c1, c2) -> c1 + c2);
	}
	/**
	 * calculates the ideal service time of Farm
	 * 
	 * @param pat
	 * @return
	 */
	public static double getServiceTime(FarmPatt pat) {
		SkeletonPatt farmWorker = pat.getChildren().get(0);
		farmWorker.calculateIdealServiceTime();
		int parallelismDegree = (int) (farmWorker.getIdealServiceTime()/Constants.TEmitter);
		pat.setIdealParDegree(parallelismDegree);
		return Math.max(Math.max(Constants.TEmitter,Constants.TCollector),farmWorker.getIdealServiceTime()/pat.getIdealParDegree());
	}
	/**
	 * calculate optimized ts
	 * @param pat
	 * @return
	 */
	public static double getOptimizedTs(FarmPatt pat) {
		SkeletonPatt farmWorker = pat.getChildren().get(0);
		farmWorker.calculateOptimalServiceTime();
		return Math.max(Math.max(Constants.TEmitter,Constants.TCollector),farmWorker.calculateOptimalServiceTime()/pat.getOptParallelismDegree());
	}
	/**
	 * calculates the service time of Map
	 * 
	 * @param pat
	 * @return
	 */
	public static double getServiceTime(MapPatt pat) {
		SkeletonPatt mapWorker = pat.getChildren().get(0);
		int parallelismDegree = (int) Math.sqrt(mapWorker.getIdealServiceTime()/Math.max(Constants.TScatter, Constants.TGather));
		pat.setIdealParDegree(parallelismDegree);
		return mapWorker.getIdealServiceTime()/pat.getIdealParDegree();
	}
	/**
	 * calculate the optimal service time
	 * @param pat
	 * @return
	 */
	public static double getOptimalServiceTime(MapPatt pat) {
		SkeletonPatt mapWorker = pat.getChildren().get(0);
		return mapWorker.calculateOptimalServiceTime()/pat.getOptParallelismDegree();
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
	public static Set<SkeletonPatt> createTreeNode(SkeletonPatt parent, SkeletonPatt node) {
		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
		Set<SkeletonPatt> optimalPattern = new LinkedHashSet<SkeletonPatt>();
		for (SkeletonPatt p : node.getPatterns()) {
			ArrayList<SkeletonPatt> sc = new ArrayList<SkeletonPatt>();

			SkeletonPatt newP = null;
			try {
				newP = parent.getClass().getDeclaredConstructor().newInstance();
			} catch (  InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}

			sc.addAll(parent.getChildren());
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
//			}
		}
		return patterns;
//		optimalPattern.add(patterns.stream().min(Comparator.comparing(SkeletonPatt::getIdealServiceTime)).get());
//		return optimalPattern;
	}
	
	public static int getHeight(SkeletonPatt pat) {
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
	
	public static SkeletonPatt clone(SkeletonPatt original) {
		SkeletonPatt copy = null;
		try {
			copy = original.getClass().getDeclaredConstructor().newInstance();
			copy.setChildren(original.getChildren());
			copy.setLable(original.getLable());
			copy.setDepth(original.getDepth());
			copy.setReWritingRule(original.getRule());
			copy.setPatterns(original.getPatterns());
			copy.setIdealServiceTime(original.getIdealServiceTime());
			copy.calculateIdealServiceTime();
			return copy;
		} catch (  InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
			System.exit(1);
			return null;
		}
	}
}
