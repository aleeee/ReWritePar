package starter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graph.Edge;
import pattern.skel4.Skel4Lexer;
import pattern.skel4.Skel4Parser;
import tree.model.SkeletonPatt;
import visitor.TBuilder2;

public class AppStarter {
	final static Logger log = LoggerFactory.getLogger(AppStarter.class);
	List<File> inputCodes;
	List<SkeletonPatt> inputs;

	public AppStarter(String folderPath, String outputDir, int simulatedAnnealingMaxIter, int maxNumberOfSimulation) {
		long startTime = System.currentTimeMillis();
		log.info("input " + folderPath);
		try {
			inputCodes = readAllInputCodesInFolder(folderPath);
			inputs = new ArrayList<>();

			for (File file : inputCodes) {
				Skel4Lexer lexer = null;

				lexer = new Skel4Lexer(CharStreams.fromFileName(file.getPath()));

				Skel4Parser parser = new Skel4Parser(new CommonTokenStream(lexer));
				ParseTree tree = parser.skeletonProgram();
				TBuilder2 tb = new TBuilder2();
				inputs.add(tb.visit(tree));
			}
		} catch (IOException e) {
			log.error("Error while parsing input " + e.getMessage());
			System.exit(-1);
		}
		
		List<ForkJoinTask<List<List<Edge>>>> forks = new ArrayList<>();

		List<List<Edge>> results = new ArrayList<>();


		for (SkeletonPatt p : inputs) {
			try(FileWriter writer= new FileWriter(new File(outputDir + "/solutions_"+p.hashCode()+".txt"), false)){
			writer.write("////------input----->  " + p + "------------/////");
			writer.close();}
			catch (IOException e) {
				log.error("Error creating solution list file {}" , e.getMessage());
			}
			forks.add(new Starter(p,maxNumberOfSimulation, simulatedAnnealingMaxIter,outputDir).fork());
		}

		for (ForkJoinTask<List<List<Edge>>> task : forks)
			results.addAll(task.join());

		StringJoiner stringPaths = new StringJoiner("\n");
		for (List<Edge> edge : results) {
			StringJoiner path = new StringJoiner(",", "(", ")");
			edge.stream().map(Edge::getRule).forEach(r -> path.add(r.getRule()));
			stringPaths.merge(path);
		}
		log.info("paths  {}", stringPaths );
		try(FileWriter writer= new FileWriter(new File(outputDir + "/paths.csv"))){
		writer.write(stringPaths.toString());
		writer.close();
		} catch (IOException e) {
			log.error("Error Writing to file " + e.getMessage() );
			System.exit(-1);
		}
		 long stopTime = (System.currentTimeMillis() - startTime) ;
		
		log.info("Finished: process takes " + stopTime + " milliseconds");
		
	}

	private List<File> readAllInputCodesInFolder(String folderPath) throws IOException {

		return Files.walk(Paths.get(folderPath)).filter(Files::isRegularFile).map(Path::toFile)
				.collect(Collectors.toList());

	}

	public static void main(String[] args) {
		log.info("Starting with input args : " + args);
		if(args.length < 4) {
			log.error("use: java -Djava.library.path=$cplex_inst_dir/opl/bin/x86-64_linux  -jar $projectName.jar $inputDir $outputDir $saMaxIter $numberOfsim " );
			System.exit(0);
		}
		try {
		String inputDir = args[0];
		String outputDir = args[1];
		int simulatedAnnealingMaxIter = Integer.parseInt(args[2]);
		int maxNumberOfSimulation = Integer.parseInt(args[3]);
			
		new AppStarter(inputDir, outputDir, simulatedAnnealingMaxIter, maxNumberOfSimulation);
		}catch(Exception e) {
			log.error("Error  " + e.getMessage());
			System.exit(0);
		}
	}

}