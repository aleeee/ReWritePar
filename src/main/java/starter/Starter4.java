package starter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.google.common.collect.Lists;

import ch.qos.logback.core.net.ssl.SSL;
import graph.Edge;
import graph.SimulatedAnnealing;
import graph.SimulatedAnnealingSeq;
import graph.SimulatedAnnealingThread;
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
	private int methodId;
	public Starter4(File file) {
	}
	public  Starter4(SkeletonPatt input, int maxNumberOfSimulation, int simulatedAnnealingMaxIter, int maxNumberOfResources,String outputDir, int parallelism, int method) {
		this.inputPattern =input;
		this.maxNumOfSim=maxNumberOfSimulation;
		this.simAnnealingMaxIter=simulatedAnnealingMaxIter;
		this.outputDir=outputDir;
		this.maxNumberOfResources=maxNumberOfResources;
		this.parallelism=parallelism;
		this.methodId=method;
	}
	public void run () {
		Instant start = Instant.now();
//		forkJoinSim(inputPattern,outputDir,maxNumberOfResources);
//		parallelStreamSim(inputPattern);
		if(methodId == 0) {
			sub(inputPattern);}
		else {
			executorService(inputPattern);
		}
//		boolean processEnded = test(inputPattern);
//		
//		if(processEnded) {
			Instant end = Instant.now();
		
			System.out.println("end processing ");
			System.out.println((end.getNano() - start.getNano() /1000000 ) );
//			}
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
	
	private void writeResults(List<Solution> results){

	Map<SkeletonPatt,Integer> solutions = new HashMap<>();
	Map<SkeletonPatt,Integer> bestSolutions = new HashMap<>();
	try {
	for(Solution solution: results) {
		for(Entry<SkeletonPatt, Integer> entry : solution.getSolutionList().entrySet()) {
			if(solutions.containsKey(entry.getKey())) {
				solutions.put(entry.getKey(),Integer.valueOf(solutions.get(entry.getKey()).intValue() + entry.getValue().intValue() ));
			}else {
				solutions.put(entry.getKey(),1);
			}
		}
	}
	}catch(Exception e) {
		log.error("error  writing ",e.getMessage());
	}
	
	for(Solution solution: results) {
		
		try {
		
			if(bestSolutions.containsKey(solution.getBestSolution())) {
				bestSolutions.put(solution.getBestSolution(),Integer.valueOf(bestSolutions.get(solution.getBestSolution()).intValue() + 1));
			}else {
				bestSolutions.put(solution.getBestSolution(),1);
			}
		}catch(Exception e) {
			log.error("Error result ",e.getMessage());
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
	private void parallelStreamSim(SkeletonPatt n){
	
	long startTime = System.currentTimeMillis();
	ForkJoinPool pool = new ForkJoinPool(parallelism);
	
	SimulatedAnnealingSeq seq = new SimulatedAnnealingSeq(n,  simAnnealingMaxIter,maxNumberOfResources);
	List<Solution> results = new ArrayList<Solution>();
	List<Integer> simRange =IntStream.range(0, maxNumOfSim).boxed().collect(Collectors.toList());
	simRange.parallelStream().forEach( i ->  results.add(seq.expandAndSearch()));
//	try {
////		pool.submit(() -> simRange.parallelStream().forEach(i -> results.add(seq.expandAndSearch()))).get();
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (ExecutionException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	
	writeResults( results);	
	
	long stopTime = (System.currentTimeMillis() - startTime);
	System.out.println("stoping> " + stopTime);
	
}
	private void executorService(SkeletonPatt n) {

		long startTime = System.currentTimeMillis();
		
		List<Solution> res = new ArrayList<Solution>();
		 ExecutorService pool = Executors.newFixedThreadPool(parallelism);
//		 ExecutorService pool = Executors.newWorkStealingPool(parallelism);
		    List<Callable<Solution>> tasks = new ArrayList<>();
		    
		   SimulatedAnnealingThread sa= new SimulatedAnnealingThread(n,  simAnnealingMaxIter,maxNumberOfResources);
 	       
		   
		    for(int i = 0; i< maxNumOfSim; i++) {
				
		    	tasks.add(new SimulatedAnnealingThread(n,  simAnnealingMaxIter,maxNumberOfResources) );
			}
		    
		   try {
		    List<Future<Solution>> results = pool.invokeAll(tasks);
//
		    for (Future<Solution> future : results) {
		    	if(future.isDone()) {
		    		res.add(future.get());
		        }
		    }
		    pool.shutdown();
//		
		}catch(Exception e) {
			System.out.println("Error"+ e.getMessage());
		}
		   
		   writeResults(res);
			long stopTime = (System.currentTimeMillis() - startTime);
			System.out.println("stoping> " + stopTime);
			
	}
	private boolean test(SkeletonPatt n) {
	     
        ExecutorService executor = Executors.newFixedThreadPool(parallelism);
        List<Future<Solution>> taskList = new ArrayList<Future<Solution>>();
        ForkJoinPool pool = new ForkJoinPool(parallelism);
        
        List<Solution> results = new ArrayList<Solution>();
         long startTime = System.currentTimeMillis();
        Collection<Callable<Solution>> tasks = new ArrayList<Callable<Solution>>();
        List<CompletableFuture<Solution>>  solutionsC = new ArrayList<CompletableFuture<Solution>>();
        for(int i = 0; i < maxNumOfSim; i++) {
        	solutionsC.add( CompletableFuture.supplyAsync(() ->
                	new SimulatedAnnealingThread(n,  simAnnealingMaxIter,maxNumberOfResources).expandAndSearch(),executor));
        }
        	 CompletableFuture<Void> f= CompletableFuture.allOf(solutionsC.toArray(new CompletableFuture[solutionsC.size()]));
        	
        	
 			
             f.thenRun(()-> {System.out.println("end");
             
        solutionsC.stream().forEach(s -> {
			try {
				
				 results.add(s.get());
				 
//			} catch (InterruptedException | ExecutionException e1) {
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		}); 
        writeResults(results);
        executor.shutdown();
        long stopTime = System.currentTimeMillis();
        System.out.println("stoping> " );
        System.out.println(stopTime - startTime );
        ;});
       
         return true;
//        CompletableFuture.allOf( solutionsC.toArray(new CompletableFuture<?>[0])).
//        thenApply(v -> solutionsC.stream() .map(future -> future.join()) 
//        		.collect(Collectors.<T>toList()) );
        
//        for(int j =0; j< solutionsC.size(); j++) {
//        	try {
//	        	if(j == solutionsC.size()-1) {
//	        		
//						solutionsC.get(j).get();
//					
//					
//	        	}else {
//	        		if(solutionsC.get(j).isDone()) {
//	   				 results.add(solutionsC.get(j).get());
//	   				 }
//	        	}
//        	} catch (InterruptedException | ExecutionException e) {
//        		System.out.println(e.getMessage());
//        	}
//        }
//       solutionsC.stream().forEach(sol -> sol.join());
       
       
	}
	  private CompletableFuture<Solution> getSkeletonExpansions(SkeletonPatt n) {
	        return CompletableFuture
	            // Asynchronously get the contents of the page.
	            .supplyAsync(() -> new SimulatedAnnealingThread(n,  simAnnealingMaxIter,maxNumberOfResources).expandAndSearch());
	    }
	private void writeFutureResults(List<Future<Solution>> results){

		Map<SkeletonPatt,Integer> solutions = new HashMap<>();
		Map<SkeletonPatt,Integer> bestSolutions = new HashMap<>();
		try {
		for(Future<Solution> fsolution: results) {
			Solution solution = fsolution.get();
			for(Entry<SkeletonPatt, Integer> entry : solution.getSolutionList().entrySet()) {
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
		
		for(Future<Solution> fsolution: results) {
			
			try {Solution solution = fsolution.get();
			
				if(bestSolutions.containsKey(solution.getBestSolution())) {
					bestSolutions.put(solution.getBestSolution(),Integer.valueOf(bestSolutions.get(solution.getBestSolution()).intValue() + 1));
				}else {
					bestSolutions.put(solution.getBestSolution(),1);
				}
			}catch(Exception e) {
				log.error("Error ",e.getMessage());
			}
		}
		
//		Comparator<Entry<SkeletonPatt, Integer>> comparator = (e1, e2) -> 
//		e1.getValue().compareTo(e2.getValue());
//		Stream<Entry<SkeletonPatt, Integer>> sortedSolution = solutions.entrySet().stream().sorted(comparator);
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
	
	private void sub(SkeletonPatt n) {
		 long startTime = System.currentTimeMillis();
		 ExecutorService executor = Executors.newFixedThreadPool(parallelism);
		final List<Future<Solution>> futures = new ArrayList<>();
		CompletableFuture<Solution>[] futuresarray = futures.stream()
	            .toArray(i -> new CompletableFuture[i]);
		
	    for (int i = 0; i< maxNumOfSim; i++) {
	          futures.add(executor.submit(	            	
						new SimulatedAnnealingThread(n,  simAnnealingMaxIter,maxNumberOfResources)				
	            ));
	           
	        }
	  
	    CompletableFuture<Void> ff = CompletableFuture.allOf(futuresarray);
//	    ArrayList<Solution> res = new ArrayList<Solution>();
	   ff.thenApply( 
	    		v -> {
	    			futures.stream().forEach(m -> {
						try {
							m.get();
						} catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
	    			  return null;}
			   );
	 
//	    CompletableFuture<List<Solution>> result = CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
//	            .thenApply(v -> futures.stream()
//	                    .map(CompletableFuture::join)
//	                    .collect(Collectors.toList())
//	            );

//	    futures.forEach(f -> f.whenComplete((t, ex) -> {
//	        if (ex != null) {
//	            result.completeExceptionally(ex);
//	        }
//	    }));

//	    return result;
	    ArrayList<Solution> res = new ArrayList<Solution>();

//	    try {
//			f.(()-> {System.out.println("end");
			
			futures.stream().forEach(s -> {
//				for(int k=0; k<futuresarray.length; k++) {
				try {
					res.add(s.get());
//					 res.add(futuresarray[k].get());
					 
//			} catch (InterruptedException | ExecutionException e1) {
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}); 
//	    try {
//	        for (Future<Solution> future : futures) {
//	        		if(future.isDone())
//	        			res.add(future.get()); // do anything you need, e.g. isDone(), ...
//	        }
//	    } catch (InterruptedException | ExecutionException e) {
//	        System.out.println("error while getting future " + e.getMessage());
//	    }
	   executor.shutdown();
			   writeResults(res);


long stopTime = (System.currentTimeMillis() - startTime);
			System.out.println("stoping> " + stopTime);
//			}).get();
//		} catch (InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	executor.shutdown();    
	}
	
}
