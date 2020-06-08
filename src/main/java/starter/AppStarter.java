package starter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import checkers.units.quals.m;
import graph.Edge;
import pattern.skel4.Skel4Lexer;
import pattern.skel4.Skel4Parser;
import tree.model.SkeletonPatt;
import visitor.SkeletonTreeBuilder;

public class AppStarter {
	final  Logger log = LoggerFactory.getLogger(AppStarter.class);
	File inputCode;
	SkeletonPatt inputSkeleton;

	public AppStarter(String folderPath, String outputDir, int simulatedAnnealingMaxIter, int maxNumberOfSimulation, int maxNumberOfResources, int parallelism, int runner) {
		long startTime = System.currentTimeMillis();
		log.info("input " + folderPath);
		try {
			inputCode =  new File(folderPath); 

			Skel4Lexer lexer = null;

			lexer = new Skel4Lexer(CharStreams.fromFileName(inputCode.getPath()));

			Skel4Parser parser = new Skel4Parser(new CommonTokenStream(lexer));
			ParseTree tree = parser.skeletonProgram();
			SkeletonTreeBuilder tb = new SkeletonTreeBuilder();
			inputSkeleton = tb.visit(tree);
			
		} catch (IOException e) {
			log.error("Error while parsing input " + e.getMessage());
			System.exit(-1);
		}
		
		
		Starter simRunner1 = new Starter(inputSkeleton, maxNumberOfSimulation, simulatedAnnealingMaxIter,maxNumberOfResources,outputDir);
		StarterSeq simRunner2 = new StarterSeq(inputSkeleton, maxNumberOfSimulation, simulatedAnnealingMaxIter,maxNumberOfResources,outputDir);
		Starter4 simRunner3 = new Starter4(inputSkeleton, maxNumberOfSimulation, simulatedAnnealingMaxIter,maxNumberOfResources,outputDir,parallelism);
		if(runner == 0)
			simRunner1.run();
		if(runner == 1)
			simRunner2.run();
		if(runner == 2)
			simRunner3.run();
		long stopTime = (System.currentTimeMillis() - startTime);

		log.info("Finished: process takes " + stopTime + " milliseconds");

	}


	public static void main(String[] args) {
		if (args.length < 6) {
			System.err.println(
					"use: java -Djava.library.path=$cplex_inst_dir/opl/bin/x86-64_linux  -jar $projectName.jar $inputDir $outputDir $saMaxIter $numberOfsim $maxResources $parallelism");
			System.exit(0);
		}
		System.setProperty("reWriter.logging.path", args[1]+"logs/");
		System.out.println("Starting with input args : " + args);
	
//		try {
			String inputDir = args[0];
			String outputDir = args[1];
			int simulatedAnnealingMaxIter = Integer.parseInt(args[2]);
			int maxNumberOfSimulation = Integer.parseInt(args[3]);
			int maxNumberOfResources = Integer.parseInt(args[4]);
			int parallelism = Integer.parseInt(args[5]);
			int runner = Integer.parseInt(args[6]);
			new AppStarter(inputDir, outputDir, simulatedAnnealingMaxIter, maxNumberOfSimulation,maxNumberOfResources,parallelism, runner);
//		} catch (Exception e) {
//			log.error("Error  " + e.getMessage());
//			System.exit(0);
//		}
	}
   
}