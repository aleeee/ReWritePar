package util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SerializationUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import pattern.skel4.Skel4Parser.AssignmentContext;
import pattern.skel4.Skel4Parser.PatternExprContext;
import pattern.skel4.Skel4Parser.SequenceContext;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class Util {
	static int n = 4;

	public static SkeletonPatt getType(AssignmentContext ctx) {
		String type = ctx.expr.sType.getText();
		switch (type) {
		case "Seq":
			SeqPatt s = new SeqPatt(Double.parseDouble(ctx.expr.sequence().ts.getText()));
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
	public static double getServiceTime(PipePatt pat) {
		return pat.getChildren().stream().mapToDouble(SkeletonPatt::getServiceTime).reduce(0,
				(c1, c2) -> c1 > c2 ? c1 : c2);

	}

	/**
	 * calculates the service time of Comp pattern
	 * 
	 * @param pat
	 * @return
	 */
	public static double getServiceTime(CompPatt pat) {
		return pat.getChildren().stream().mapToDouble(SkeletonPatt::getServiceTime).reduce(0, (c1, c2) -> c1 + c2);
	}

	/**
	 * calculates the service time of Farm
	 * 
	 * @param pat
	 * @return
	 */
	public static double getServiceTime(FarmPatt pat) {
		return pat.getChildren().get(0).getServiceTime() / n;
	}

	/**
	 * calculates the service time of Map
	 * 
	 * @param pat
	 * @return
	 */
	public static double getServiceTime(MapPatt pat) {
		return pat.getChildren().get(0).getServiceTime() / n;
	}

	/**
	 * creates n trees from the input tree replacing input node with its n rewriting
	 * (refactoring option) pattern
	 * 
	 * @param parent
	 * @param node
	 * @return
	 */
	public static ArrayList<SkeletonPatt> createTreeNode(SkeletonPatt parent, SkeletonPatt node) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<SkeletonPatt>();

		for (SkeletonPatt p : node.getPatterns()) {
			ArrayList<SkeletonPatt> sc = new ArrayList<SkeletonPatt>();

			SkeletonPatt newP = null;
			try {
				newP = parent.getClass().getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
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
		}
		return patterns;
	}
}
