package starter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tree.model.SkeletonPatt;
import tree.model.Solution;
import util.Util;
public class Starter5  {
//	Logger log = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = 1L;
	private SkeletonPatt inputPattern;
	private int maxNumOfSim;
	private int simAnnealingMaxIter;
	private String outputDir;
	private int maxNumberOfResources;
	private int parallelism;
	private int methodId;
	Util util ;
	public Starter5(File file) {
	}
	public  Starter5(SkeletonPatt input, int maxNumberOfSimulation, int simulatedAnnealingMaxIter, int maxNumberOfResources,String outputDir, int parallelism, int method) {
		this.inputPattern =input;
		this.maxNumOfSim=maxNumberOfSimulation;
		this.simAnnealingMaxIter=simulatedAnnealingMaxIter;
		this.outputDir=outputDir;
		this.maxNumberOfResources=maxNumberOfResources;
		this.parallelism=parallelism;
		this.methodId=method;
		util = new Util();
	}
	public void run () {
		Instant start = Instant.now();
//		forkJoinSim(inputPattern,outputDir,maxNumberOfResources);
//		parallelStreamSim(inputPattern);
		spc(inputPattern);
			Instant end = Instant.now();
		
//			System.out.println("end processing ");
//			System.out.println((end.getNano() - start.getNano() /1000000 ) );
//			}
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
		e.printStackTrace();
//		log.error("error  writing ",e.getMessage());
	}
	
	for(Solution solution: results) {
		
		try {
		
			if(bestSolutions.containsKey(solution.getBestSolution())) {
				bestSolutions.put(solution.getBestSolution(),Integer.valueOf(bestSolutions.get(solution.getBestSolution()).intValue() + 1));
			}else {
				bestSolutions.put(solution.getBestSolution(),1);
			}
		}catch(Exception e) {
			e.printStackTrace();
//			log.error("Error result ",e.getMessage());
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
		e.printStackTrace();
//		log.error("Error Writing to file " + e.getMessage());
		System.exit(-1);
	}

	}

    
    private void spc(SkeletonPatt n) {

		long startTime = System.currentTimeMillis();
		int batchSize = parallelism;
		List<Solution> res = new ArrayList<Solution>();
		ExecutorService pool2 = Executors.newFixedThreadPool(parallelism);
		int size = maxNumOfSim;
		int simSize = simAnnealingMaxIter;
		int numRes= maxNumberOfResources;
		
		CompletableFuture<List<List<Solution>>> t = IntStream.range(0, batchSize).boxed()
				          .map(o -> CompletableFuture.supplyAsync(() ->
				          		new SimRunner_(size/batchSize, simSize, numRes, n).call(), pool2))
				          .collect(toFuture());
//		List<CompletableFuture<List<Solution>>> t = new ArrayList<>();
//		IntStream.range(0, batchSize).boxed().forEach(
//		           f ->t.add(CompletableFuture.supplyAsync(() ->
//		           		
//		          		new SimRunner(size/batchSize, simSize, numRes, util.clone(n)).call(), pool2)));
//		      
				 
		    try {
		    	
		    	t.get().forEach(f -> 
		    	res.addAll(f));
//		    for (CompletableFuture<List<Solution>> future : t) {
//		    		if(future.isDone()) {
//		    				res.addAll(future.get());
//		    		}
//		    	
//		        }
		    pool2.shutdown();
		}catch(Exception e) {
			System.out.println("Error"+ e.getMessage());
		}
		   
		   writeResults(res);
			long stopTime = (System.currentTimeMillis() - startTime);
			System.out.println("stoping> " + stopTime);
			
	}
    public static <T> Collector<CompletableFuture<T>, ?, CompletableFuture<List<T>>> toFuture() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            CompletableFuture<List<T>> future = CompletableFuture
              .allOf(list.toArray(new CompletableFuture[list.size()]))
              .thenApply(__ -> list.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));

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
}
