package graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.io.ExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import rewriter.RW;
import tree.model.SkeletonPatt;
import tree.model.Solution;
import util.ReWritingRules;
import util.Util;
import static java.util.stream.Collectors.joining;
public class SimulatedAnnealingSeq  implements Callable<Solution> {
	private Logger log = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = 1L;
	private SkeletonPatt s;
	RW reWriter;
	private Graph<SkeletonPatt, Edge> g;
	AtomicInteger intId;	
	private int maxIteration;
	private int maxNumberOfResources;
	private Map<SkeletonPatt, Integer> solutionMap;
	private Solution solution;
	public SimulatedAnnealingSeq(SkeletonPatt p,int simAnnealingMaxIter,int maxNumberOfResources) {
		this.s = p;		
		g = new DefaultDirectedGraph<>(Edge.class);
		reWriter = new RW();
		intId = new AtomicInteger();
		this.maxIteration = simAnnealingMaxIter;	
		this.maxNumberOfResources=maxNumberOfResources;
		this.solutionMap = new HashMap<>();
		this.solution=new Solution();
	}

	
	

	public Solution  expandAndSearch() {
		double temprature = 19;
		double coolingRate = 0.7;

		Set<SkeletonPatt> solutionPool = new HashSet<>();
		SkeletonPatt bestSolution = Util.clone(s);
		SkeletonPatt currentSolution = Util.clone(s);
		double currentCost = Util.getCost(currentSolution,maxNumberOfResources);
		double bestCost = Util.getCost(bestSolution,maxNumberOfResources);
		int x = 0;
		currentSolution.refactor(reWriter);
		
		for (SkeletonPatt sol : currentSolution.getPatterns()) {
			Util.getCost(sol,maxNumberOfResources);
			this.add(currentSolution, sol, sol.getRule());
		}
		
		SkeletonPatt first = currentSolution.getPatterns().stream().min(Comparator.comparing(SkeletonPatt::getOptServiceTime)).get();
		solutionPool.addAll(currentSolution.getPatterns());
		this.add(s, first, first.getRule());
		currentSolution = Util.clone(first);
		
		while (x++ < maxIteration && temprature > 0.1) {
			
			currentSolution.refactor(reWriter);
			
			List<SkeletonPatt> solutions = new ArrayList<SkeletonPatt>(currentSolution.getPatterns());
			for (SkeletonPatt sol : solutions) {
				Util.getCost(sol,maxNumberOfResources);
				this.add(currentSolution, sol, sol.getRule());

			}
			addSolutionMap(currentSolution);
			Stream<SkeletonPatt> temp = solutions.stream().filter(s -> !solutionPool.contains(s));
			SkeletonPatt newSolution=null;
			if(temp.findAny().isPresent()) {
				 newSolution= Util.clone(solutions.stream().filter(s -> !solutionPool.contains(s)).min(Comparator.comparing(SkeletonPatt::getOptServiceTime)).get());
			}else {			
				newSolution=solutions.stream().skip(ThreadLocalRandom.current().nextInt(solutions.size())).findAny().get();
			}
			
			solutionPool.addAll(solutions);
			log.debug("best " + bestSolution.toString());
			double newCost = Util.getCost(newSolution,maxNumberOfResources);
			if (newCost <= currentCost) {
				currentSolution = Util.clone(newSolution);
				currentCost = newCost;
				log.debug("currentCost " + currentSolution.toString());
				if (newCost < bestCost || (newCost == bestCost
						&& newSolution.getNumberOfResources() < bestSolution.getNumberOfResources())) {
					bestSolution = Util.clone(newSolution);
					bestCost = newCost;
					this.add(currentSolution, bestSolution, bestSolution.getRule());
				}
			} else {
				if (Math.exp((newCost - currentCost) / temprature) > Math.random()) {
					currentSolution = solutionPool.isEmpty() ? newSolution
							: solutionPool.stream().skip(ThreadLocalRandom.current().nextInt(solutionPool.size()))
									.findAny().get();
					
					solutionPool.add(newSolution);
				}
			}
			log.debug("best " + bestSolution + "\t" + Util.getCost(bestSolution,maxNumberOfResources));
			
			Util.getCost(currentSolution,maxNumberOfResources);
			if (currentSolution.getNumberOfResources() > maxNumberOfResources || currentSolution.getNumberOfResources() <1) {
				currentSolution.getChildren().forEach(c -> {
					System.out.println(c.getNumberOfResources());
					System.out.println(c.getOptParallelismDegree());});
			}
			log.info("iteration: "+x +" -> "+ currentSolution.print() +"\t res: " + currentSolution.getNumberOfResources());
			log.debug("new  " + newSolution + "\t" + Util.getCost(newSolution,maxNumberOfResources));
			temprature *= coolingRate;
		}
		log.info(" best : " + bestSolution.print());
		
		addSolutionMap(bestSolution);
		solution.setBestSolution(bestSolution);
		solution.setSolutionList(solutionMap);
		
		return solution;
	}

	public Graph<SkeletonPatt, Edge> getG() {
		return g;
	}

	public void setG(Graph<SkeletonPatt, Edge> g) {
		this.g = g;
	}
	
	private void addSolutionMap(SkeletonPatt sol) {
		if(solutionMap.containsKey(sol)) {
			solutionMap.put(sol,Integer.valueOf(solutionMap.get(sol).intValue() +1));
		}else {
			solutionMap.put(sol,1);
		}
	}
	/**
	 * Add an edge to the graph
	 */
	public void add(SkeletonPatt from, SkeletonPatt to, ReWritingRules rule) {
		try {
					
			if (!g.containsVertex(from)) {
				from.setId(intId.getAndIncrement());
				g.addVertex(from);
			}
			if (!g.containsVertex(to)) {
				to.setId(intId.getAndIncrement());
				g.addVertex(to);
			}
			g.addEdge(from, to, new Edge(from, to, rule));
		} catch (Exception e) {
			log.error("ERR " + g.vertexSet().size());
			throw e;
		}
	}



	@Override
	public Solution call() throws Exception {
		return expandAndSearch();
	}

}
