package starter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import graph.Edge;
import graph.SimulatedAnnealing;
import pattern.skel4.Skel4Lexer;
import pattern.skel4.Skel4Parser;
import tree.model.SkeletonPatt;
import visitor.TBuilder2;

public class Starter extends RecursiveTask<List<List<Edge>>> {

	private static final long serialVersionUID = 1L;
	private File file;
	private SkeletonPatt inputPattern;

	public Starter(File file) {
		this.file = file;
	}
	public  Starter(SkeletonPatt input) {
		this.inputPattern =input;
	}
	@Override
	protected List<List<Edge>> compute() {
		System.out.println("parsing: " + inputPattern);

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
		return forkJoinSim(inputPattern);
//		return ParallelStreamSim(inputPattern);
//		return seqSim(inputPattern);
	}
	private List<List<Edge>> forkJoinSim(SkeletonPatt n){
		List<ForkJoinTask<List<Edge>>> forks = new ArrayList<ForkJoinTask<List<Edge>>>();
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < 100; i++) {
			forks.add(new SimulatedAnnealing(n, 6).fork());
		}

		List<List<Edge>> results = new ArrayList<List<Edge>>();
		for (ForkJoinTask<List<Edge>> task : forks)
			results.add(task.join());
		long stopTime = (System.currentTimeMillis() - startTime);
		System.out.println("stoping> " + stopTime);
		System.out.println("results > " + results);
//		return results.stream().flatMap(List::stream).collect(Collectors.toList());
		return results;
	}
	private List<Edge> ParallelStreamSim(SkeletonPatt n){
		
		long startTime = System.currentTimeMillis();
		
		SimulatedAnnealing sa = new SimulatedAnnealing(n, 5);
		List<List<Edge>> results = new ArrayList<List<Edge>>();
		List<Integer> simRange =IntStream.range(0, 20).boxed().collect(Collectors.toList());
		simRange.parallelStream().forEach( i ->  results.add(new SimulatedAnnealing(n, 5).compute()));
		
		long stopTime = (System.currentTimeMillis() - startTime);
		System.out.println("stoping> " + stopTime);
		System.out.println("results > " + results);
		return results.stream().flatMap(List::stream).collect(Collectors.toList());
	}
        private List<Edge> seqSim(SkeletonPatt n){
		
		long startTime = System.currentTimeMillis();
		
		SimulatedAnnealing sa = new SimulatedAnnealing(n, 5);
		List<List<Edge>> results = new ArrayList<List<Edge>>();
		IntStream.range(0, 20).forEachOrdered( i ->  results.add(sa.expandAndSearch()));
		
		long stopTime = (System.currentTimeMillis() - startTime);
		System.out.println("stoping> " + stopTime);
		System.out.println("results > " + results);
		return results.stream().flatMap(List::stream).collect(Collectors.toList());
	}

}
