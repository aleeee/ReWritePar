package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.io.ExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import rewriter.RW;
import tree.model.SkeletonPatt;
import util.ReWritingRules;
import util.Util;

public class SimulatedAnnealing extends RecursiveTask<List<Edge>> {
	private Logger log = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = 1L;
	private SkeletonPatt s;
	RW rw ;
	private  Graph<SkeletonPatt, Edge> g; 	
	AtomicInteger intId ;
	private int maxHieght;
	public SimulatedAnnealing(SkeletonPatt p, int maxHieght) {
		this.s=p;
		this.maxHieght =maxHieght;
		g= new DefaultDirectedGraph<>(Edge.class);
		rw= new RW();
		intId= new AtomicInteger();
	}
	

	/**
	 * Add an edge to the graph
	 */
	public void add(SkeletonPatt from, SkeletonPatt to, ReWritingRules rule) {
		try {
			
		if(!g.containsVertex(from)) {
			from.setId(intId.getAndIncrement());
			g.addVertex(from);}
		if(!g.containsVertex(to)) {
			to.setId(intId.getAndIncrement());
			g.addVertex(to);}
//		if(g.getEdge(from, to) == null || !g.getEdge(from, to).getRule().equals(rule)){
		g.addEdge(from, to, new Edge(from, to, rule));
//		}
		}catch(Exception e) {
			System.out.println("ERR "+g.vertexSet().size());
			System.out.println(e.getMessage());
		}
	}
	@Override
	public List<Edge> compute() {
		return expandAndSearch();
	}

	public List<Edge> expandAndSearch() {
		double temprature = 19;
		double maxIteration = 20;
		double coolingRate = 0.97;
		
//		s.setId(intId.getAndIncrement());
//		g.addVertex(s);
//		s.refactor(rw);
		Set<SkeletonPatt> solutionPool = new HashSet<>();
		
//		List<SkeletonPatt> initialSolutions = new ArrayList<SkeletonPatt>(s.getPatterns());
//		solutionPool.addAll(initialSolutions);
//		SkeletonPatt currentSolution = Util.clone(initialSolutions.stream().filter(ss -> Util.getHeight(ss) < maxHieght).findAny().get());
	
		SkeletonPatt bestSolution = Util.clone(s);
		SkeletonPatt currentSolution = Util.clone(s);
		double currentCost = Util.getCost(currentSolution);
		double bestCost = Util.getCost(bestSolution);
		int x=0;
//		bestSolution.print();
//		System.out.println(bestSolution);
//		this.add(s, currentSolution, currentSolution.getRule());
//		this.add(s, bestSolution, bestSolution.getRule());
		//list to hold generated solutions but not selected for best or current solution
		//they are going to be considered in the random selection
		
		solutionPool.add(bestSolution);
		while (x++ < maxIteration && temprature > 0.1 ) {

			currentSolution.refactor(rw);
			List<SkeletonPatt> solutions = new ArrayList<SkeletonPatt>(currentSolution.getPatterns());
			solutions.forEach(newPattern -> Util.getCost(newPattern));
			for(SkeletonPatt sol: solutions)
					this.add(currentSolution, sol, sol.getRule());
			SkeletonPatt newSolution = Util.clone(solutions.stream().filter(m -> Util.getHeight(m) < maxHieght).findAny().get());
			//add the discarded solutions into pool for later consideration
			solutionPool.addAll(solutions);
//			this.add(currentSolution, newSolution, newSolution.getRule());
//			System.out.println(bestSolution);
			double newCost = Util.getCost(newSolution);
			if(newCost <= currentCost  ) {
//				newSolution.print();
				currentSolution = newSolution;
				currentCost = newCost;
				
				if(newCost < bestCost || (newCost <= bestCost && Util.getHeight(newSolution) < Util.getHeight(bestSolution))) {
					bestSolution = newSolution;
					bestCost=newCost;

//					newSolution.print();
				}
			}else {
				if(Math.exp((newCost - currentCost)/temprature) > Math.random()){
					currentSolution=solutionPool.stream().skip(ThreadLocalRandom.current().nextInt(solutionPool.size())).findAny().get();
//					currentSolution = Collections.shuffle(new ArrayList<solutionPool).stream().get(ThreadLocalRandom.current().nextInt(solutionPool.size()));
//					newSolution.print();
					solutionPool.add(newSolution);
				}
			}
			temprature *= coolingRate;

		}
		System.out.println("best " + bestSolution.print());
//		bestSolution.print();
		List<Edge> paths = DijkstraShortestPath.findPathBetween(g, s, bestSolution).getEdgeList();
		System.out.println("input> " +s);
//		solutionPool.forEach(ss -> System.out.println(s + " > "+ss + " TS > "+ ss.getOptServiceTime() + " OTS> " + ss.calculateOptimalServiceTime() + " cpo >" + Util.getCost(ss)));
		if(paths.isEmpty()) {
			System.out.println("no edge> "+g.vertexSet().size());
			System.out.println("bestSolution" + bestSolution);
//			return null;
		}
//		try {
//			DiGraphUtil.renderHrefGraph(g);
//		} catch (ExportException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return paths;
	}


	public Graph<SkeletonPatt, Edge> getG() {
		return g;
	}


	public void setG(Graph<SkeletonPatt, Edge> g) {
		this.g = g;
	}
	
}
