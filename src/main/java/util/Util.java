package util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import graph.DiGraphGen2;
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
			SeqPatt s = new SeqPatt(Integer.parseInt(ctx.expr.sequence().ts.getText()));
			s.setLable(ctx.varName.getText());
			return s;

		case "Comp":
			return new CompPatt("comp", 0);
		case "Farm":
			return new FarmPatt("farm", 0);
		case "Pipe":
			return new PipePatt("pipe", 0);
		case "Map":
			return new MapPatt("map", 0);
		}
		return null;
	}

	/**
	 * a method to generate all the combinations of stages after each stage
	 * component is refactored their patterns combined using cartesian product
	 * 
	 * @param pattern
	 * @return possible alterative implementations(rewritings ) of stages
	 */
	public static List<List<SkeletonPatt>> getStagesPatterns(SkeletonPatt pattern) {
		List<Set<SkeletonPatt>> sets = new ArrayList<Set<SkeletonPatt>>();
		for (SkeletonPatt node : pattern.getChildren()) {
			sets.add(new HashSet<SkeletonPatt>(node.getPatterns()));

		}
//		return (List<List<SkeletonPatt>>) Sets.cartesianProduct(sets);

		return Sets.cartesianProduct(sets).stream().map(l -> new ArrayList<SkeletonPatt>(l))
				.collect(Collectors.toList());

	}

	/**
	 * calculates the service time of pipeline
	 * 
	 * @param pat
	 * @return
	 */
	public static int getServiceTime(PipePatt pat) {
		return pat.getChildren().stream().mapToInt(SkeletonPatt::getServiceTime).reduce(0,
				(c1, c2) -> c1 > c2 ? c1 : c2);

	}

	/**
	 * calculates the service time of Comp pattern
	 * 
	 * @param pat
	 * @return
	 */
	public static int getServiceTime(CompPatt pat) {
		return pat.getChildren().stream().mapToInt(SkeletonPatt::getServiceTime).reduce(0, (c1, c2) -> c1 + c2);
	}

	/**
	 * calculates the service time of Farm
	 * 
	 * @param pat
	 * @return
	 */
	public static int getServiceTime(FarmPatt pat) {
		return pat.getChildren().get(0).getServiceTime() / n;
	}

	/**
	 * calculates the service time of Map
	 * 
	 * @param pat
	 * @return
	 */
	public static int getServiceTime(MapPatt pat) {
		return pat.getChildren().get(0).getServiceTime() / n;
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
			newP.setDepth(p.getDepth());
			newP.setChildren(sc);
			newP.getChildren().set(newP.getChildren().indexOf(node), p);
			newP.setReWritingRule(p.getRule());
			newP.calculateServiceTime();
			patterns.add(newP);
			DiGraphGen2.g.addVertex(newP);
		}
		return patterns;
	}
}
