package starter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graph.Edge;
import graph.SimulatedAnnealing;
import graph.SimulatedAnnealingSeq;
import graph.SimulatedAnnealingThread;
import tree.model.SkeletonPatt;
import tree.model.Solution;

public class StarterSeq  {
	Logger log = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = 1L;
	private SkeletonPatt inputPattern;
	private int maxNumOfSim;
	private int simAnnealingMaxIter;
	private String outputDir;
	private int maxNumberOfResources;
	public StarterSeq(File file) {
	}
	public  StarterSeq(SkeletonPatt input, int maxNumberOfSimulation, int simulatedAnnealingMaxIter, int maxNumberOfResources,String outputDir) {
		this.inputPattern =input;
		this.maxNumOfSim=maxNumberOfSimulation;
		this.simAnnealingMaxIter=simulatedAnnealingMaxIter;
		this.outputDir=outputDir;
		this.maxNumberOfResources=maxNumberOfResources;
	}
	public void run () {
		forkJoinSim(inputPattern,outputDir,maxNumberOfResources);
	}
	
	private void forkJoinSim(SkeletonPatt n,String outputDir,int maxNumberOfResources){
		
	    List<Solution> results = new ArrayList<Solution>();
		SimulatedAnnealingThread seqSim = new SimulatedAnnealingThread(n,  simAnnealingMaxIter,maxNumberOfResources);

		for (int i = 0; i < maxNumOfSim; i++) {
			
			results.add(seqSim.expandAndSearch());
		}

		writeResults( results);
	}
	
	private void writeResults(List<Solution> results){

	Map<SkeletonPatt,Integer> solutions = new HashMap<>();
	Map<SkeletonPatt,Integer> bestSolutions = new HashMap<>();
	
	for(Solution solution: results) {
		for(Entry<SkeletonPatt, Integer> entry : solution.getSolutionList().entrySet()) {
			if(solutions.containsKey(entry.getKey())) {
				solutions.put(entry.getKey(),Integer.valueOf(solutions.get(entry.getKey()).intValue() + entry.getValue().intValue() ));
			}else {
				solutions.put(entry.getKey(),1);
			}
		}
	}
	
	
	for(Solution solution: results) {
		
			if(bestSolutions.containsKey(solution.getBestSolution())) {
				bestSolutions.put(solution.getBestSolution(),Integer.valueOf(bestSolutions.get(solution.getBestSolution()).intValue() + 1));
			}else {
				bestSolutions.put(solution.getBestSolution(),1);
			}
		}
	
//	Comparator<Entry<SkeletonPatt, Integer>> comparator = (e1, e2) -> 
//	e1.getValue().compareTo(e2.getValue());
//	Stream<Entry<SkeletonPatt, Integer>> sortedSolution = solutions.entrySet().stream().sorted(comparator);
	try {
		Files.write(new File(outputDir + "/solutions_"+Instant.now().toEpochMilli()+"+.txt").toPath(), () -> solutions.entrySet().stream()
			    .<CharSequence>map(e -> e.getKey().print() + ";" + e.getValue())
			    .iterator());
		
		Files.write(new File(outputDir + "/bestsolutions_"+Instant.now().toEpochMilli()+"+.txt").toPath(), () -> bestSolutions.entrySet().stream()
			    .<CharSequence>map(e -> e.getKey().print() + ";" + e.getValue())
			    .iterator());
	} catch (IOException e) {
		log.error("Error Writing to file " + e.getMessage());
		System.exit(-1);
	}

	}
}
