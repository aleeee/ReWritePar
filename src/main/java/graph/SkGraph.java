package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import rewriter.RW;
import tree.model.SkeletonPatt;
import util.ReWritingRules;
import util.Util;

public class SkGraph {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	RW rw = new RW();
	
	public static Graph<SkeletonPatt, Edge> g = new DefaultDirectedGraph<>(Edge.class);	

	
	AtomicInteger intId = new AtomicInteger();
	
	public static class Edge {
		private SkeletonPatt vertex;
		private ReWritingRules rule;
		private SkeletonPatt to;
		public Edge(SkeletonPatt from, SkeletonPatt to, ReWritingRules rule) {
			this.vertex = from;
			this.rule = rule;
			this.to=to;
		}

		public Edge(SkeletonPatt v, ReWritingRules c) {
			vertex = v;
			rule = c;
		}

		public SkeletonPatt getVertex() {
			return vertex;
		}

		public ReWritingRules getRule() {
			return rule;
		}
		public SkeletonPatt getTo() {
			return to;
		}

		@Override
		public String toString() {
			return "Edge [V=" + vertex + ", rule=" + vertex.getRule() + "]";
		}

	}

	/**
	 * Add an edge to the graph
	 */
	public void add(SkeletonPatt from, SkeletonPatt to, ReWritingRules rule) {
		if(!g.containsVertex(from)) {
			from.setId(intId.getAndIncrement());
			g.addVertex(from);}
		if(!g.containsVertex(to)) {
			to.setId(intId.getAndIncrement());
			g.addVertex(to);}
		g.addEdge(from, to, new Edge(from, to, rule));
	}

	public void axpandAndSearch(SkeletonPatt s, int depth) {
		double temprature = 9;
		double maxIteration = 20;
		double coolingRate = 0.97;
		
//		g.getType().asUnmodifiable();
		s.setId(intId.getAndIncrement());
		g.addVertex(s);
		s.refactor(rw);
		List<SkeletonPatt> initialSolutions = new ArrayList<SkeletonPatt>(s.getPatterns());
		 Collections.shuffle(initialSolutions);
		int random = ThreadLocalRandom.current().nextInt(0, initialSolutions.size());
		SkeletonPatt currentSolution = Util.clone(initialSolutions.get(random));
		SkeletonPatt bestSolution = Util.clone(currentSolution);
		double currentCost = Util.getCost(currentSolution);
		double bestCost = Util.getCost(bestSolution);
		int x=0;
		bestSolution.print();
		currentSolution.print();
		this.add(s, currentSolution, currentSolution.getRule());
		//list to hold generated solutions but not selected for best or current solution
		//they are going to be considered in the random selection
		List<SkeletonPatt> solutionPool = new ArrayList<SkeletonPatt>();
		while (x++ < maxIteration && temprature > 0.1 ) {
			
			currentSolution.refactor(rw);
			List<SkeletonPatt> solutions = new ArrayList<SkeletonPatt>(currentSolution.getPatterns());
			 Collections.shuffle(solutions);
//			 SkeletonPatt newSolution = solutions.stream().filter(sol-> !g.containsVertex(sol)).findAny().get();
			int randomSolution = ThreadLocalRandom.current().nextInt(0, currentSolution.getPatterns().size());
			SkeletonPatt newSolution = Util.clone(solutions.get(randomSolution));
			//add the discarded solutions into pool for later consideration
			solutionPool.addAll(solutions.stream().filter(sol -> !sol.equals(newSolution)).collect(Collectors.toList()));
			this.add(currentSolution, newSolution, newSolution.getRule());
			
			if(Util.getHeight(newSolution) > 5)
				return;
			double newCost = Util.getCost(newSolution);
//			System.out.println("the new cost si " + newCost);
			if(newCost < currentCost ) {
//				System.out.println("new < current + "+ newSolution);
				newSolution.print();
				currentSolution = newSolution;
				currentCost = newCost;
				
				if(newCost < bestCost) {
					bestSolution = newSolution;
					bestCost=newCost;
//					System.out.println("new < best + "+ newSolution);
					newSolution.print();
				}
			}else {
				if(Math.exp((newCost - currentCost)/temprature) > Math.random()){
					solutionPool.add(newSolution);
					currentSolution = solutionPool.get(ThreadLocalRandom.current().nextInt(solutionPool.size()));
//					System.out.println("random current + "+ newSolution);
					newSolution.print();
				}
			}
			temprature *= coolingRate;

//			System.out.println("temp: " +temprature);
		}
		System.out.println("the best solution is: " );
		System.out.println(bestSolution);
		System.out.println("printing :");
		bestSolution.print();
		System.out.println("printed");
//		if (patterns != null) {
//			patterns.entrySet().forEach(e -> {
//				SkeletonPatt k = e.getKey();
//				e.getValue().forEach(v -> {
//					v.calculateIdealServiceTime();
//					this.add(k, v, v.getRule());
//				});
//			});
//
//		}
	}

}