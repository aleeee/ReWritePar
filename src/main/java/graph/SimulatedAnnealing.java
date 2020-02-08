package graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
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
	private int maxIteration;
	StringJoiner solutionList;
	private String outputDir;
	
	public SimulatedAnnealing(SkeletonPatt p, int maxHieght, int simAnnealingMaxIter, String outputDir) {
		this.s=p;
		this.maxHieght =maxHieght;
		g= new DefaultDirectedGraph<>(Edge.class);
		rw= new RW();
		intId= new AtomicInteger();
		this.maxIteration = simAnnealingMaxIter;
		solutionList= new StringJoiner("\n");
		solutionList.add("$$$$$$$$$$$$$$$$$$--------start--------$$$$$$$$$$$$$$$$$$$");
//		solutionList.add("$$$----input ------>  "+s + "\t--$$");
		this.outputDir=outputDir;
		
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
		g.addEdge(from, to, new Edge(from, to, rule));
		}catch(Exception e) {
			log.error("ERR "+g.vertexSet().size());
			throw e;
		}
	}
	@Override
	public List<Edge> compute() {
		return expandAndSearch();
	}

	public List<Edge> expandAndSearch() {
		double temprature = 19;
//		double maxIteration = 20;
		double coolingRate = 0.97;
		
//		s.setId(intId.getAndIncrement());
//		g.addVertex(s);
//		s.refactor(rw);
		Set<SkeletonPatt> solutionPool = new HashSet<>();
		
//		List<SkeletonPatt> initialSolutions = new ArrayList<SkeletonPatt>(s.getPatterns());
//		solutionPool.addAll(initialSolutions);
//		SkeletonPatt currentSolution = Util.clone(initialSolutions.stream().filter(ss -> Util.getHeight(ss) < maxHieght).findAny().get());
		s.setId(intId.incrementAndGet());
		SkeletonPatt bestSolution = Util.clone(s);
		bestSolution.setId(intId.incrementAndGet());
		SkeletonPatt currentSolution = Util.clone(s);
		currentSolution.setId(intId.incrementAndGet());
		double currentCost = Util.getCost(currentSolution);
		double bestCost = Util.getCost(bestSolution);
		int x=0;
		this.g.addVertex(s);
		this.g.addVertex(bestSolution);
//		bestSolution.print();
//		System.out.println(bestSolution);
//		this.add(s, currentSolution, currentSolution.getRule());
//		this.add(s, bestSolution, bestSolution.getRule());
		//list to hold generated solutions but not selected for best or current solution
		//they are going to be considered in the random selection
		
//		solutionPool.add(bestSolution);
		while (x++ < maxIteration && temprature > 0.1 ) {

			currentSolution.refactor(rw);
			List<SkeletonPatt> solutions = new ArrayList<SkeletonPatt>(currentSolution.getPatterns());
			solutions.forEach(newPattern -> Util.getCost(newPattern));
			for(SkeletonPatt sol: solutions) {
					sol.setId(intId.incrementAndGet());
					this.add(currentSolution, sol, sol.getRule());
					}
			SkeletonPatt newSolution = Util.clone(solutions.stream().skip(ThreadLocalRandom.current().nextInt(solutions.size())).findAny().get());
			newSolution.setId(intId.incrementAndGet());
			//add the discarded solutions into pool for later consideration
			solutionPool.addAll(solutions.stream().filter(s -> !s.equals(newSolution)).collect(Collectors.toList()));
//			this.add(currentSolution, newSolution, newSolution.getRule());
			log.debug("best " +bestSolution.toString());
			double newCost = Util.getCost(newSolution);
			if(newCost <= currentCost  ) {
//				newSolution.print();
				currentSolution = Util.clone(newSolution);
				currentSolution.setId(intId.incrementAndGet());
				currentCost = newCost;
				log.debug("currentCost " +currentSolution.toString());
				if(newCost < bestCost || (newCost == bestCost && newSolution.getNumberOfResources() < bestSolution.getNumberOfResources())) {
					bestSolution = Util.clone(newSolution);
					bestSolution.setId(intId.incrementAndGet());
					bestCost=newCost;
					this.add(currentSolution, bestSolution, bestSolution.getRule());
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
			log.info("best " + bestSolution + "\t"+Util.getCost(bestSolution)+ "\t"+bestSolution.getNumberOfResources() );
//			log.info("best_ " + bestSolution );
			log.info("current " + currentSolution + "\t"+ Util.getCost(currentSolution)+ "\t"+ +currentSolution.getNumberOfResources() );
//			log.info("current_ " + currentSolution );
			log.info("new  " + newSolution+ "\t"+Util.getCost(newSolution) + "\t"+newSolution.getNumberOfResources() );
//			log.info("new_  " + newSolution);
			temprature *= coolingRate;
			solutionList.add(newSolution+ "\t ts: "+newSolution.getOptServiceTime());

		}
		log.info("Da best : " + bestSolution.print());
		
		List<Edge> paths = DijkstraShortestPath.findPathBetween(g, s, bestSolution) != null ? DijkstraShortestPath.findPathBetween(g, s, bestSolution).getEdgeList(): null;
//		solutionPool.forEach(ss -> System.out.println(s + " > "+ss + " TS > "+ ss.getOptServiceTime() + " OTS> " + ss.calculateOptimalServiceTime() + " cpo >" + Util.getCost(ss)));
		if(paths == null || paths.isEmpty()) {
			log.warn("no edge to the best solution: " + bestSolution);
//			try {
//			DiGraphUtil.renderHrefGraph(g);
//		} catch (ExportException e) {
//			log.error("Error while exporting graph" + e.getMessage());
//		}
//			return null;
		}
		solutionList.add("best: " +bestSolution + "\t ts: " + bestSolution.getOptServiceTime());
		solutionList.add("$$$$$$$$$$$$$$$$$$--------end--------$$$$$$$$$$$$$$$$$$$\n");
		try(FileWriter writer= new FileWriter(new File(outputDir + "/solutions_"+s.hashCode()+".txt"), true)){
			writer.write(solutionList.toString());
			writer.close();}
		catch (IOException e) {
				log.error("Error creating solution list file {}" , e.getMessage());
			}
//		try {
//			DiGraphUtil.renderHrefGraph(g);
////			DiGraphUtil2.exportJson(g);
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
