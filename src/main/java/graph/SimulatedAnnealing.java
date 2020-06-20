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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rewriter.RW;
import tree.model.SkeletonPatt;
import tree.model.Solution;
import util.ReWritingRules;
import util.Util;
public class SimulatedAnnealing  implements Callable<Solution> {
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
	private Util util;
	public SimulatedAnnealing(SkeletonPatt p,int simAnnealingMaxIter,int maxNumberOfResources) {
		this.s = p;		
		this.util= new Util();
		g = new DefaultDirectedGraph<>(Edge.class);
		reWriter = new RW();
		intId = new AtomicInteger();
		this.maxIteration = simAnnealingMaxIter;	
		this.maxNumberOfResources=maxNumberOfResources;
		this.solutionMap = new HashMap<>();
		this.solution=new Solution();
	}

	
	

	public Solution  expandAndSearch() {
		double temprature = 100;
		double coolingRate = 0.97;

		Set<SkeletonPatt> solutionPool = new HashSet<>();
		SkeletonPatt bestSolution = util.clone(s);
		SkeletonPatt currentSolution = util.clone(s);
		double currentCost = util.getCost(currentSolution,maxNumberOfResources);
		double bestCost = util.getCost(bestSolution,maxNumberOfResources);
		int x = 0;
		currentSolution.refactor(reWriter);
		
		for (SkeletonPatt sol : currentSolution.getPatterns()) {
			util.getCost(sol,maxNumberOfResources);
			this.add(currentSolution, sol, sol.getRule());
		}
//				Set<SkeletonPatt> p =  currentSolution.getPatterns();

		SkeletonPatt first = currentSolution.getPatterns().stream().min(Comparator.comparing(SkeletonPatt::getOptServiceTime)).get();
		solutionPool.addAll(currentSolution.getPatterns());
		this.add(s, first, first.getRule());
		currentSolution = util.clone(first);
		while (x++ < maxIteration && temprature > 0.1) {
			
			currentSolution.refactor(reWriter);
//			currentSolution.setPatterns(p);
			List<SkeletonPatt> solutions = new ArrayList<SkeletonPatt>(currentSolution.getPatterns());
			for (SkeletonPatt sol : solutions) {
				util.getCost(sol,maxNumberOfResources);
				this.add(currentSolution, sol, sol.getRule());

			}
			addSolutionMap(currentSolution);
			Stream<SkeletonPatt> temp = solutions.stream().filter(s -> !solutionPool.contains(s));
			SkeletonPatt newSolution=null;
			if(temp.findAny().isPresent()) {
				 newSolution= util.clone(solutions.stream().filter(s -> !solutionPool.contains(s)).min(Comparator.comparing(SkeletonPatt::getOptServiceTime)).get());
			}else {			
				newSolution=solutions.stream().skip(ThreadLocalRandom.current().nextInt(solutions.size())).findAny().get();
			}
			
//			solutionPool.addAll(solutions);
			log.debug("best " + bestSolution.toString());
			double newCost = util.getCost(newSolution,maxNumberOfResources);
			if (newCost <= currentCost) {
				currentSolution = util.clone(newSolution);
				currentCost = newCost;
				log.debug("currentCost " + currentSolution.toString());
				if (newCost < bestCost || (newCost == bestCost
						&& newSolution.getNumberOfResources() < bestSolution.getNumberOfResources())) {
					bestSolution = util.clone(newSolution);
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
			log.debug("best " + bestSolution + "\t" + util.getCost(bestSolution,maxNumberOfResources));
			
			util.getCost(currentSolution,maxNumberOfResources);
			log.info("iteration: "+x +" -> "+ currentSolution.print() +"\t res: " + currentSolution.getNumberOfResources());
			log.debug("new  " + newSolution + "\t" + util.getCost(newSolution,maxNumberOfResources));
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
					
//			if (!g.containsVertex(from)) {
//				from.setId(intId.getAndIncrement());
//				g.addVertex(from);
//			}
//			if (!g.containsVertex(to)) {
//				to.setId(intId.getAndIncrement());
//				g.addVertex(to);
//			}
//			g.addEdge(from, to, new Edge(from, to, rule));
		} catch (Exception e) {
			log.error("ERR ", e.getMessage());
			throw e;
		}
	}



	@Override
	public Solution call() throws Exception {
		return expandAndSearch();
	}

}
