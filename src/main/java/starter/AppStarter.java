package starter;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pattern.skel4.SkeletonLexer;
import pattern.skel4.SkeletonParser;
import tree.model.SkeletonPatt;
import visitor.SkeletonTreeBuilder;

public class AppStarter {
	final  Logger log = LoggerFactory.getLogger(AppStarter.class);
	File inputCode;
	SkeletonPatt inputSkeleton;
	
	public AppStarter(String folderPath, String outputDir, int simulatedAnnealingMaxIter, int maxNumberOfSimulation, int maxNumberOfResources, int parallelism) {
		Instant startTime = Instant.now();
		log.info("input " + folderPath);
		try {
			inputCode =  new File(folderPath); 

			SkeletonLexer lexer = null;

			lexer = new SkeletonLexer(CharStreams.fromFileName(inputCode.getPath()));

			SkeletonParser parser = new SkeletonParser(new CommonTokenStream(lexer));
			ParseTree tree = parser.skeletonProgram();
			SkeletonTreeBuilder tb = new SkeletonTreeBuilder();
			inputSkeleton = tb.visit(tree);
			
		} catch (IOException e) {
			log.error("Error while parsing input " + e.getMessage());
			System.exit(-1);
		}
		
		
		SimRunner simRunner = new SimRunner(inputSkeleton, maxNumberOfSimulation, simulatedAnnealingMaxIter,maxNumberOfResources,outputDir,parallelism);
		
	
			simRunner.run();
		

		Duration timelapse = Duration.between(startTime, Instant.now());

		log.info("Finished: process takes " + timelapse.toMillis() + " milliseconds");

	}


	public static void main(String[] args) {
		if (args.length < 6) {
			System.err.println(
					"use: java -Djava.library.path=$cplex_inst_dir/opl/bin/x86-64_linux  -jar $projectName.jar $inputDir $outputDir $saMaxIter $numberOfsim $maxResources $parallelism");
			System.exit(0);
		}
		System.setProperty("reWriter.logging.path", args[1]+"logs/");
		System.out.println("Starting with input args : " + args);
	
			String inputDir = args[0];
			String outputDir = args[1];
			int simulatedAnnealingMaxIter = Integer.parseInt(args[2]);
			int maxNumberOfSimulation = Integer.parseInt(args[3]);
			int maxNumberOfResources = Integer.parseInt(args[4]);
			int parallelism = Integer.parseInt(args[5]);
		
			new AppStarter(inputDir, outputDir, simulatedAnnealingMaxIter, maxNumberOfSimulation,maxNumberOfResources,parallelism);
		
	}
   
}