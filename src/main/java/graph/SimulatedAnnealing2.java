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
import util.ReWritingRules;
import util.Util;
import static java.util.stream.Collectors.joining;
public class SimulatedAnnealing2 extends RecursiveTask<List<Edge>> {
	private Logger log = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = 1L;
	private SkeletonPatt s;
	RW reWriter;
	private Graph<SkeletonPatt, Edge> g;
	AtomicInteger intId;
	private int maxHieght;
	private int maxIteration;
	StringJoiner solutionList;
	StringJoiner solutionsandpaths;
	private String outputDir;
	StringJoiner bestSolutionList;
	private int maxNumberOfResources;

	public SimulatedAnnealing2(SkeletonPatt p, int maxHieght, int simAnnealingMaxIter, String outputDir,int maxNumberOfResources) {
		this.s = p;
		this.maxHieght = maxHieght;
		g = new DefaultDirectedGraph<>(Edge.class);
		reWriter = new RW();
		intId = new AtomicInteger();
		this.maxIteration = simAnnealingMaxIter;
		solutionList = new StringJoiner("\n");
		solutionsandpaths = new StringJoiner("\n");
		bestSolutionList = new StringJoiner("\n");
		solutionList.add("////--------start--------////////");
		this.outputDir = outputDir;
		this.maxNumberOfResources=maxNumberOfResources;
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
	public List<Edge> compute() {
		return expandAndSearch();
	}

	public List<Edge> expandAndSearch() {
		double temprature = 19;
		double coolingRate = 0.7;

		Set<SkeletonPatt> solutionPool = new HashSet<>();
		SkeletonPatt bestSolution = Util.clone(s);
		SkeletonPatt currentSolution = Util.clone(s);
		double currentCost = Util.getCost(currentSolution,maxNumberOfResources);
		double bestCost = Util.getCost(bestSolution,maxNumberOfResources);
		int x = 0;
		currentSolution.refactor(reWriter);
//		currentSolution.reWrite();
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
//			currentSolution.reWrite();
			List<SkeletonPatt> solutions = new ArrayList<SkeletonPatt>(currentSolution.getPatterns());
			for (SkeletonPatt sol : solutions) {
				Util.getCost(sol,maxNumberOfResources);
				this.add(currentSolution, sol, sol.getRule());
			}
//			SkeletonPatt newSolution = Util.clone(
//					solutions.stream().skip(ThreadLocalRandom.current().nextInt(solutions.size())).findAny().get());
			Stream<SkeletonPatt> temp = solutions.stream().filter(s -> !solutionPool.contains(s));
			SkeletonPatt newSolution=null;
			if(temp.findAny().isPresent()) {
				 newSolution= Util.clone(solutions.stream().filter(s -> !solutionPool.contains(s)).min(Comparator.comparing(SkeletonPatt::getOptServiceTime)).get());
			}else {			
			newSolution=solutions.stream().skip(ThreadLocalRandom.current().nextInt(solutions.size())).findAny().get();
			}
//			SkeletonPatt newSolution = Util.clone(solutions.stream().min(Comparator.comparing(SkeletonPatt::getOptServiceTime)).get());
			solutionPool.addAll(solutions);
			log.debug("best " + bestSolution.toString());
			double newCost = Util.getCost(newSolution,maxNumberOfResources);
			if (newCost <= currentCost) {
//				newSolution.print();
				currentSolution = Util.clone(newSolution);
//				currentSolution.setId(intId.incrementAndGet());
				currentCost = newCost;
				log.debug("currentCost " + currentSolution.toString());
				if (newCost < bestCost || (newCost == bestCost
						&& newSolution.getNumberOfResources() < bestSolution.getNumberOfResources())) {
					bestSolution = Util.clone(newSolution);
//					bestSolution.setId(intId.incrementAndGet());
					bestCost = newCost;
					this.add(currentSolution, bestSolution, bestSolution.getRule());
//					newSolution.print();
				}
			} else {
				if (Math.exp((newCost - currentCost) / temprature) > Math.random()) {
					currentSolution = solutionPool.isEmpty() ? newSolution
							: solutionPool.stream().skip(ThreadLocalRandom.current().nextInt(solutionPool.size()))
									.findAny().get();
					
//					currentSolution = Collections.shuffle(new ArrayList<solutionPool).stream().get(ThreadLocalRandom.current().nextInt(solutionPool.size()));
//					newSolution.print();
					solutionPool.add(newSolution);
				}
			}
			log.debug("best " + bestSolution + "\t" + Util.getCost(bestSolution,maxNumberOfResources));
//			log.info("best_ " + bestSolution );
			
			Util.getCost(currentSolution,maxNumberOfResources);
			if (currentSolution.getNumberOfResources() > maxNumberOfResources || currentSolution.getNumberOfResources() <1) {
				currentSolution.getChildren().forEach(c -> {
					System.out.println(c.getNumberOfResources());
					System.out.println(c.getOptParallelismDegree());});
			}
			log.info("iteration: "+x +" -> "+ currentSolution.print() +"\t res: " + currentSolution.getNumberOfResources());
//			log.info("current_ " + currentSolution );
			log.debug("new  " + newSolution + "\t" + Util.getCost(newSolution,maxNumberOfResources));
//			log.info("new_  " + newSolution);
			temprature *= coolingRate;
			solutionList.add(newSolution + "   ;  " + newSolution.getOptServiceTime() +"  ; " + newSolution.getNumberOfResources());

		}
		log.info(" best : " + bestSolution.print());
		if(bestSolution.getNumberOfResources()<= maxNumberOfResources)
		bestSolutionList.add("\n" + bestSolution.print() +";\t"   +bestSolution.getOptServiceTime()+  ";\t  "+bestSolution.getNumberOfResources());
		List<Edge> paths = DijkstraShortestPath.findPathBetween(g, s, bestSolution) != null
				? DijkstraShortestPath.findPathBetween(g, s, bestSolution).getEdgeList()
				: null;
		if (paths == null || paths.isEmpty()) {
			log.warn("no edge to the best solution: " + bestSolution);
		}

//		Set<Edge> st = g.outgoingEdgesOf(s);
//		GraphPath<SkeletonPatt, Edge> newSearchResults = DijkstraShortestPath.findPathBetween(g, s, bestSolution);
////try {
//			DiGraphUtil.renderHrefGraph(g);
////			DiGraphUtil2.exportJson(g);
//		} catch (ExportException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		StringJoiner stringPaths = new StringJoiner("\n");
		if(bestSolution.getNumberOfResources()<= maxNumberOfResources)
		solutionsandpaths.add(paths.stream().map(e -> e.getRule().getRule()).collect(joining("->"))+"  ,\t"+bestSolution.getNumberOfResources() + "  ,\t" +bestSolution.getOptServiceTime()+"\n");
		solutionList.add("best: " + bestSolution + "\t ts: " + bestSolution.getOptServiceTime());
		solutionList.add("//////--------end--------/////\n");
		try (FileWriter writer = new FileWriter(new File(outputDir + "/solutions_" + s.hashCode() + ".txt"), true)) {
			writer.write(solutionList.toString());
			writer.close();
		} catch (IOException e) {
			log.error("Error creating solution list file {}", e.getMessage());
		}
		try (FileWriter writer = new FileWriter(new File(outputDir + "/bestSolutions_" + s.hashCode() + ".txt"),
				true)) {
			writer.write(bestSolutionList.toString());
			writer.close();
		} catch (IOException e) {
			log.error("Error creating solution list file {}", e.getMessage());
		}
		try (FileWriter writer = new FileWriter(new File(outputDir + "/path_ts_resources" + s.hashCode() + ".txt"),
				true)) {
			writer.write(solutionsandpaths.toString());
			writer.close();
		} catch (IOException e) {
			log.error("Error creating solution list file {}", e.getMessage());
		}
		return paths;
	}

	public Graph<SkeletonPatt, Edge> getG() {
		return g;
	}

	public void setG(Graph<SkeletonPatt, Edge> g) {
		this.g = g;
	}

}
