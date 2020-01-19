package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
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

public class SimulatedAnnealing extends RecursiveTask<Edge> {
	private Logger log = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = 1L;
	private SkeletonPatt s;
	RW rw ;
	public static Graph<SkeletonPatt, Edge> g; 	
	AtomicInteger intId = new AtomicInteger();
	private int maxHieght;
	public SimulatedAnnealing(SkeletonPatt p, int maxHieght) {
		this.s=p;
		this.maxHieght =maxHieght;
		g= new DefaultDirectedGraph<>(Edge.class);
		rw= new RW();
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
	@Override
	protected Edge compute() {
		return expandAndSearch();
	}

	public Edge expandAndSearch() {
		double temprature = 9;
		double maxIteration = 20;
		double coolingRate = 0.97;
		
		s.setId(intId.getAndIncrement());
		g.addVertex(s);
		s.refactor(rw);
		List<SkeletonPatt> initialSolutions = new ArrayList<SkeletonPatt>(s.getPatterns());
		SkeletonPatt currentSolution = Util.clone(initialSolutions.stream().filter(ss -> Util.getHeight(ss) < maxHieght).findAny().get());
		SkeletonPatt bestSolution = Util.clone(currentSolution);
		double currentCost = Util.getCost(currentSolution);
		double bestCost = Util.getCost(bestSolution);
		int x=0;
		bestSolution.print();
		System.out.println(currentSolution);
		this.add(s, currentSolution, currentSolution.getRule());
		this.add(s, bestSolution, bestSolution.getRule());
		//list to hold generated solutions but not selected for best or current solution
		//they are going to be considered in the random selection
		List<SkeletonPatt> solutionPool = new ArrayList<SkeletonPatt>();
		while (x++ < maxIteration && temprature > 0.1 ) {
			
			currentSolution.refactor(rw);
			List<SkeletonPatt> solutions = new ArrayList<SkeletonPatt>(currentSolution.getPatterns());
			for(SkeletonPatt sol: solutions)
					this.add(currentSolution, sol, sol.getRule());
			SkeletonPatt newSolution = Util.clone(solutions.stream().filter(m -> Util.getHeight(m) < maxHieght).findAny().get());
			//add the discarded solutions into pool for later consideration
			solutionPool.addAll(solutions.stream().filter(sol -> !sol.equals(newSolution)).collect(Collectors.toList()));
//			this.add(currentSolution, newSolution, newSolution.getRule());
			
			double newCost = Util.getCost(newSolution);
			if(newCost < currentCost ) {
				newSolution.print();
				currentSolution = newSolution;
				currentCost = newCost;
				
				if(newCost < bestCost) {
					bestSolution = newSolution;
					bestCost=newCost;

					newSolution.print();
				}
			}else {
				if(Math.exp((newCost - currentCost)/temprature) > Math.random()){
					solutionPool.add(newSolution);
					currentSolution = solutionPool.get(ThreadLocalRandom.current().nextInt(solutionPool.size()));
					newSolution.print();
				}
			}
			temprature *= coolingRate;

		}
		System.out.println("best " + bestSolution);
		bestSolution.print();
		Set<Edge> paths = g.getAllEdges(bestSolution,s);
		System.out.println("eddges");
		System.out.println(paths);
		if(paths.isEmpty()) {
			System.out.println("Null");
			return null;
		}
		return paths.iterator().next();
	}
}
