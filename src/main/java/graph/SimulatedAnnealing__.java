package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import rewriter.RW;
import tree.model.SkeletonPatt;
import tree.model.Solution;
import util.Util;

public class SimulatedAnnealing__ implements Callable<Solution> {

	private static final long serialVersionUID = 1L;
	private SkeletonPatt s;
	RW reWriter;
	AtomicInteger intId;
	private int maxIteration;
	private int maxNumberOfResources;
	private Map<SkeletonPatt, Integer> solutionMap;
	private Map<SkeletonPatt, Set<SkeletonPatt>> calculatedSolutions;
	private Solution solution;
	private Util util;

	public SimulatedAnnealing__(SkeletonPatt p, int simAnnealingMaxIter, int maxNumberOfResources) {
		this.s = p;
		this.util = new Util();
		reWriter = new RW();
		intId = new AtomicInteger();
		this.maxIteration = simAnnealingMaxIter;
		this.maxNumberOfResources = maxNumberOfResources;
		this.solutionMap = new HashMap<>();
		this.solution = new Solution();
		calculatedSolutions = new HashMap<SkeletonPatt, Set<SkeletonPatt>>();
	}

	public Solution expandAndSearch() {
		double temprature = 200;
		double coolingRate = 0.9;

		Set<SkeletonPatt> solutionPool = new HashSet<>();
		SkeletonPatt bestSolution = util.clone(s);
		SkeletonPatt currentSolution = util.clone(s);
		double currentCost = util.getCost(currentSolution, maxNumberOfResources);
		double bestCost = util.getCost(bestSolution, maxNumberOfResources);
		int x = 0;
		currentSolution.refactor(reWriter);
		getCostAndKnownRefactorings(currentSolution);
		for (SkeletonPatt sol : currentSolution.getPatterns()) {
			getCostAndKnownRefactorings(sol);
		}

		SkeletonPatt first = currentSolution.getPatterns().stream()
				.min(Comparator.comparing(SkeletonPatt::getOptServiceTime)).get();
		solutionPool.addAll(currentSolution.getPatterns());

		currentSolution = util.clone(first);

		while (x++ < maxIteration && temprature > 0.1) {

			refactor(currentSolution);
			List<SkeletonPatt> solutions = new ArrayList<SkeletonPatt>(currentSolution.getPatterns());
			for (SkeletonPatt sol : solutions) {
				getCostAndKnownRefactorings(sol);

			}
			addSolutionMap(currentSolution);
			Stream<SkeletonPatt> temp = solutions.stream().filter(s -> !solutionPool.contains(s));
			SkeletonPatt newSolution = null;
			if (temp.findAny().isPresent()) {
				newSolution = util.clone(solutions.stream().filter(s -> !solutionPool.contains(s))
						.min(Comparator.comparing(SkeletonPatt::getOptServiceTime)).get());
			} else {
				newSolution = solutions.stream().skip(ThreadLocalRandom.current().nextInt(solutions.size())).findAny()
						.get();
			}

			solutionPool.addAll(solutions);

			double newCost = getCostAndKnownRefactorings(newSolution);

			if (newCost <= currentCost) {
				currentSolution = util.clone(newSolution);
				currentCost = newCost;
				if (newCost < bestCost || (newCost == bestCost
						&& newSolution.getNumberOfResources() < bestSolution.getNumberOfResources())) {
					bestSolution = util.clone(newSolution);
					bestCost = newCost;
				}
			} else {
				if (Math.exp((newCost - currentCost) / temprature) > Math.random()) {
					currentSolution = solutionPool.isEmpty() ? newSolution
							: solutionPool.stream().skip(ThreadLocalRandom.current().nextInt(solutionPool.size()))
									.findAny().get();

					solutionPool.add(newSolution);
				}
			}

	
			temprature *= coolingRate;
		}

		addSolutionMap(bestSolution);
		solution.setBestSolution(bestSolution);
		solution.setSolutionList(solutionMap);
		return solution;
	}

	private void addSolutionMap(SkeletonPatt sol) {
		if (solutionMap.containsKey(sol)) {
			solutionMap.put(sol, Integer.valueOf(solutionMap.get(sol).intValue() + 1));
		} else {
			solutionMap.put(sol, 1);
		}
	}

	private double getCostAndKnownRefactorings(SkeletonPatt skel) {
		calculatedSolutions.computeIfPresent(skel, (k, value) -> {
			if (value != null && !value.isEmpty()) {
				skel.setPatterns(value);
				skel.setIdealParDegree(k.getIdealParDegree());
				skel.setIdealServiceTime(k.getIdealServiceTime());
				skel.setOptParallelismDegree(k.getOptParallelismDegree());
				skel.setOptServiceTime(k.getOptServiceTime());
				skel.calculateOptimalServiceTime();
				return value;
			} else {
				return skel.getPatterns() != null ? skel.getPatterns() : new HashSet<SkeletonPatt>();
			}
		});
		calculatedSolutions.computeIfAbsent(skel, key -> {
			util.getCost(skel, maxNumberOfResources);
			return skel.getPatterns() != null ? skel.getPatterns() : new HashSet<SkeletonPatt>();
		});

		return skel.getOptServiceTime();
	}

	SkeletonPatt refactor(SkeletonPatt skel) {
		calculatedSolutions.computeIfPresent(skel, (k, value) -> {
			if (value != null && !value.isEmpty()) {
				skel.setPatterns(value);
				skel.setIdealParDegree(k.getIdealParDegree());
				skel.setIdealServiceTime(k.getIdealServiceTime());
				skel.setOptParallelismDegree(k.getOptParallelismDegree());
				skel.setOptServiceTime(k.getOptServiceTime());

				return value;
			} else {
				skel.refactor(reWriter);

				return skel.getPatterns() != null ? skel.getPatterns() : new HashSet<SkeletonPatt>();
			}
		});
		return skel;

	}

	@Override
	public Solution call() throws Exception {
		return expandAndSearch();
	}

}
