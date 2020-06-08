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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graph.Edge;
import graph.SimulatedAnnealing;
import graph.SimulatedAnnealingSeq;
import tree.model.SkeletonPatt;
import tree.model.Solution;

public class Starter4  {
	Logger log = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = 1L;
	private SkeletonPatt inputPattern;
	private int maxNumOfSim;
	private int simAnnealingMaxIter;
	private String outputDir;
	private int maxNumberOfResources;
	private int parallelism;
	public Starter4(File file) {
	}
	public  Starter4(SkeletonPatt input, int maxNumberOfSimulation, int simulatedAnnealingMaxIter, int maxNumberOfResources,String outputDir, int parallelism) {
		this.inputPattern =input;
		this.maxNumOfSim=maxNumberOfSimulation;
		this.simAnnealingMaxIter=simulatedAnnealingMaxIter;
		this.outputDir=outputDir;
		this.maxNumberOfResources=maxNumberOfResources;
		this.parallelism=parallelism;
	}
	public void run () {
//		forkJoinSim(inputPattern,outputDir,maxNumberOfResources);
//		ParallelStreamSim(inputPattern);
		executorService(inputPattern);
	}
	
	private void forkJoinSim(SkeletonPatt n,String outputDir,int maxNumberOfResources){
		List<ForkJoinTask<Solution>> forks = new ArrayList<ForkJoinTask<Solution>>();

		for (int i = 0; i < maxNumOfSim; i++) {
			forks.add(new SimulatedAnnealing(n,  simAnnealingMaxIter,maxNumberOfResources).fork());
		}

		List<Solution> results = new ArrayList<Solution>();
		for (ForkJoinTask<Solution> task : forks)
			results.add(task.join());
		//writeResults( results);
	}
	
	private void writeResults(List<Future<Solution>> results){

	Map<SkeletonPatt,Integer> solutions = new HashMap<>();
	Map<SkeletonPatt,Integer> bestSolutions = new HashMap<>();
	try {
	for(Future<Solution> solution: results) {
		for(Entry<SkeletonPatt, Integer> entry : solution.get().getSolutionList().entrySet()) {
			if(solutions.containsKey(entry.getKey())) {
				solutions.put(entry.getKey(),Integer.valueOf(solutions.get(entry.getKey()).intValue() + entry.getValue().intValue() ));
			}else {
				solutions.put(entry.getKey(),1);
			}
		}
	}
	}catch(Exception e) {
		log.error("error ",e.getMessage());
	}
	
	for(Future<Solution> fSolution: results) {
		Solution solution;
		try {
			solution = fSolution.get();
		
			if(bestSolutions.containsKey(solution.getBestSolution())) {
				bestSolutions.put(solution.getBestSolution(),Integer.valueOf(bestSolutions.get(solution.getBestSolution()).intValue() + 1));
			}else {
				bestSolutions.put(solution.getBestSolution(),1);
			}
		}catch(Exception e) {
			log.error("Error ",e.getMessage());
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
	private void ParallelStreamSim(SkeletonPatt n){
	
	long startTime = System.currentTimeMillis();
	
	SimulatedAnnealingSeq seq = new SimulatedAnnealingSeq(n,  simAnnealingMaxIter,maxNumberOfResources);
	List<Solution> results = new ArrayList<Solution>();
	List<Integer> simRange =IntStream.range(0, maxNumOfSim).boxed().collect(Collectors.toList());
	simRange.parallelStream().forEach( i ->  results.add(seq.expandAndSearch()));
	//writeResults( results);	
	
	long stopTime = (System.currentTimeMillis() - startTime);
	System.out.println("stoping> " + stopTime);
	
}
	private void executorService(SkeletonPatt n) {
		   ExecutorService executor = Executors.newWorkStealingPool(parallelism);
			long startTime = System.currentTimeMillis();
			
			SimulatedAnnealingSeq seq = new SimulatedAnnealingSeq(n,  simAnnealingMaxIter,maxNumberOfResources);
			List<SimulatedAnnealingSeq> tasks = new ArrayList<SimulatedAnnealingSeq>();
			for(int i = 0; i< maxNumOfSim; i++) {
				tasks.add(new SimulatedAnnealingSeq(n,  simAnnealingMaxIter,maxNumberOfResources));
			}
			  List<Future<Solution>> results;
			try {
				results = executor.invokeAll(tasks);
			
	       
	            executor.shutdown();

	            
	            
			writeResults(results);	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long stopTime = (System.currentTimeMillis() - startTime);
			System.out.println("stoping> " + stopTime);
			
	}
}
