package starter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graph.Edge;
import graph.SimulatedAnnealing2;
import tree.model.SkeletonPatt;

public class Starter extends RecursiveTask<List<List<Edge>>> {
	Logger log = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = 1L;
	private SkeletonPatt inputPattern;
	private int maxNumOfSim;
	private int simAnnealingMaxIter;
	private String outputDir;
	private int maxNumberOfResources;
	public Starter(File file) {
	}
	public  Starter(SkeletonPatt input, int maxNumberOfSimulation, int simulatedAnnealingMaxIter, String outputDir, int maxNumberOfResources) {
		this.inputPattern =input;
		this.maxNumOfSim=maxNumberOfSimulation;
		this.simAnnealingMaxIter=simulatedAnnealingMaxIter;
		this.outputDir=outputDir;
		this.maxNumberOfResources=maxNumberOfResources;
	}
	@Override
	protected List<List<Edge>> compute() {

//		Skel4Lexer lexer = null;
//		try {
//			lexer = new Skel4Lexer(CharStreams.fromFileName(file.getPath()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Skel4Parser parser = new Skel4Parser(new CommonTokenStream(lexer));
//		ParseTree tree = parser.skeletonProgram();
//		TBuilder2 tb = new TBuilder2();
//		SkeletonPatt n = tb.visit(tree);

//		List<ForkJoinTask<List<Edge>>> forks = new ArrayList<ForkJoinTask<List<Edge>>>();
//		long startTime = System.currentTimeMillis();
//
//		for (int i = 0; i < 2; i++) {
//			forks.add(new SimulatedAnnealing(n, 5).fork());
//		}
//
//		List<List<Edge>> results = new ArrayList<List<Edge>>();
//		for (ForkJoinTask<List<Edge>> task : forks)
//			results.add(task.join());
//		long stopTime = (System.currentTimeMillis() - startTime);
//		System.out.println("stoping> " + stopTime);
//		System.out.println("results > " + results);
////		return results.stream().flatMap(List::stream).collect(Collectors.toList());
//		
		return forkJoinSim(inputPattern,outputDir,maxNumberOfResources);
//		return ParallelStreamSim(inputPattern);
//		return seqSim(inputPattern);
	}
	private List<List<Edge>> forkJoinSim(SkeletonPatt n,String outputDir,int maxNumberOfResources){
		List<ForkJoinTask<List<Edge>>> forks = new ArrayList<ForkJoinTask<List<Edge>>>();

		for (int i = 0; i < maxNumOfSim; i++) {
			forks.add(new SimulatedAnnealing2(n, 6, simAnnealingMaxIter,outputDir,maxNumberOfResources).fork());
		}

		List<List<Edge>> results = new ArrayList<List<Edge>>();
		for (ForkJoinTask<List<Edge>> task : forks)
			results.add(task .join());
		return results;
	}
//	private List<Edge> ParallelStreamSim(SkeletonPatt n){
//		
//		long startTime = System.currentTimeMillis();
//		
//		new SimulatedAnnealing(n, 5,simAnnealingMaxIter);
//		List<List<Edge>> results = new ArrayList<List<Edge>>();
//		List<Integer> simRange =IntStream.range(0, 20).boxed().collect(Collectors.toList());
//		simRange.parallelStream().forEach( i ->  results.add(new SimulatedAnnealing(n, 5,simAnnealingMaxIter).compute()));
//		
//		long stopTime = (System.currentTimeMillis() - startTime);
//		System.out.println("stoping> " + stopTime);
//		System.out.println("results > " + results);
//		return results.stream().flatMap(List::stream).collect(Collectors.toList());
//	}
//        private List<Edge> seqSim(SkeletonPatt n){
//		
//		long startTime = System.currentTimeMillis();
//		
//		SimulatedAnnealing sa = new SimulatedAnnealing(n, 5,simAnnealingMaxIter);
//		List<List<Edge>> results = new ArrayList<List<Edge>>();
//		IntStream.range(0, 20).forEachOrdered( i ->  results.add(sa.expandAndSearch()));
//		
//		long stopTime = (System.currentTimeMillis() - startTime);
//		System.out.println("stoping> " + stopTime);
//		System.out.println("results > " + results);
//		return results.stream().flatMap(List::stream).collect(Collectors.toList());
//	}

}
