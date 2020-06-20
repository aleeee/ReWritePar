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
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graph.SimulatedAnnealing;
import tree.model.SkeletonPatt;
import tree.model.Solution;
import util.Util;

public class SimRunner {
	Logger log = LoggerFactory.getLogger(getClass());

	private SkeletonPatt inputPattern;
	private int maxNumOfSim;
	private int simAnnealingMaxIter;
	private String outputDir;
	private int maxNumberOfResources;
	private int parallelism;
	Util util;

	public SimRunner(File file) {
	}

	public SimRunner(SkeletonPatt input, int maxNumberOfSimulation, int simulatedAnnealingMaxIter,
			int maxNumberOfResources, String outputDir, int parallelism, int method) {
		this.inputPattern = input;
		this.maxNumOfSim = maxNumberOfSimulation;
		this.simAnnealingMaxIter = simulatedAnnealingMaxIter;
		this.outputDir = outputDir;
		this.maxNumberOfResources = maxNumberOfResources;
		this.parallelism = parallelism;
		util = new Util();
	}

	public void run() {

		List<Solution> res = new ArrayList<Solution>();
		ExecutorService pool = Executors.newFixedThreadPool(parallelism);

		List<Callable<Solution>> tasks = new ArrayList<>();

		for (int i = 0; i < maxNumOfSim; i++) {

			tasks.add(new SimulatedAnnealing(util.clone(inputPattern), simAnnealingMaxIter, maxNumberOfResources));
		}
//		CompletableFuture<List<Solution>> t = IntStream.range(0, maxNumOfSim).boxed()
//		          .map(o -> CompletableFuture.supplyAsync(() ->
//		          {
//					try {
//						return new SimulatedAnnealing(inputPattern, simAnnealingMaxIter, maxNumberOfResources).call();
//					} catch (Exception e1) {
//						log.error("Error running simulated annealing {}",e1.getMessage());
//						System.exit(1);
//					}
//					return null;
//				}, pool))
//		          .collect(toFuture());
		try {
			List<Future<Solution>> results = pool.invokeAll(tasks);
			for (Future<Solution> future : results) {
				if (future.isDone())
					res.add(future.get());

			}
//			res.addAll(t.get());
			pool.shutdown();

		} catch (Exception e) {
			log.error("Error while running simulation {}", e.getMessage());
		}

		writeResults(res);

	}

	public static <T> Collector<CompletableFuture<T>, ?, CompletableFuture<List<T>>> toFuture() {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			CompletableFuture<List<T>> future = CompletableFuture
					.allOf(list.toArray(new CompletableFuture[list.size()]))
					.thenApply(__ -> list.stream().map(CompletableFuture::join).collect(Collectors.toList()));

			for (CompletableFuture<T> f : list) {
				f.whenComplete((integer, throwable) -> {
					if (throwable != null) {
						future.completeExceptionally(throwable);
					}
				});
			}

			return future;
		});
	}

	private void writeResults(List<Solution> results) {

		Map<SkeletonPatt, Integer> solutions = new HashMap<>();
		Map<SkeletonPatt, Integer> bestSolutions = new HashMap<>();
		try {
			for (Solution solution : results) {
				for (Entry<SkeletonPatt, Integer> entry : solution.getSolutionList().entrySet()) {
					if (solutions.containsKey(entry.getKey())) {
						solutions.put(entry.getKey(), Integer
								.valueOf(solutions.get(entry.getKey()).intValue() + entry.getValue().intValue()));
					} else {
						solutions.put(entry.getKey(), 1);
					}
				}
			}
		} catch (Exception e) {
			log.error("error  writing ", e.getMessage());
		}

		for (Solution solution : results) {

			try {

				if (bestSolutions.containsKey(solution.getBestSolution())) {
					bestSolutions.put(solution.getBestSolution(),
							Integer.valueOf(bestSolutions.get(solution.getBestSolution()).intValue() + 1));
				} else {
					bestSolutions.put(solution.getBestSolution(), 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Error result ", e.getMessage());
			}
		}

		Comparator<Entry<SkeletonPatt, Integer>> comparator = (e1, e2) -> e1.getValue().compareTo(e2.getValue());
		Stream<Entry<SkeletonPatt, Integer>> sortedSolution = solutions.entrySet().stream().sorted(comparator);
		try {
			Files.write(new File(outputDir + "/solutions_" + Instant.now().toEpochMilli() + "+.txt").toPath(),
					() -> sortedSolution.<CharSequence>map(e -> e.getKey().print() + ";" + e.getValue()).iterator());

			Files.write(new File(outputDir + "/bestsolutions_" + Instant.now().toEpochMilli() + "+.txt").toPath(),
					() -> bestSolutions.entrySet().stream()
							.<CharSequence>map(e -> e.getKey().print() + ";" + e.getValue()).iterator());
		} catch (IOException e) {
			log.error("Error Writing to file " + e.getMessage());
			System.exit(-1);
		}

	}

void test() {
	final ExecutorService executor = Executors.newFixedThreadPool(parallelism);
	final LinkedBlockingQueue<Future<Solution>> futures = new LinkedBlockingQueue<>(parallelism);
	List<Solution> res = new ArrayList<Solution>();
	try {   
	    Thread taskGenerator = new Thread() {
	        @Override
	        public void run() {
	            while (true) {
	            	maxNumOfSim--;
	                Callable<Solution> task = new SimulatedAnnealing(util.clone(inputPattern), simAnnealingMaxIter, maxNumberOfResources);
	                Future<Solution> future = executor.submit(task);
	                try {
	                    // if queue is full blocks until a task
	                    // is completed and hence no future tasks are submitted.
	                    futures.put(future);
	                } catch (InterruptedException ex) {
	                    Thread.currentThread().interrupt();         
	                }
	                if(maxNumOfSim < 1) 
	                	return;
	                }
	            } 
	    };
	    taskGenerator.start();

	    // read from queue as long as task are being generated
	    // or while Queue has elements in it
	    while (taskGenerator.isAlive()
	                    || !futures.isEmpty()) {
	        Future<Solution> compoundFuture = futures.take();
	               
	        try {    
						res .add(compoundFuture.get());

			} catch (Exception e) {
				log.error("Error while running simulation {}", e.getMessage());
			}

			writeResults(res);
	    }
	    executor.shutdown();
	} catch (InterruptedException ex) {
	    Thread.currentThread().interrupt();     
	} finally {
	    executor.shutdownNow();
	}
}
}
