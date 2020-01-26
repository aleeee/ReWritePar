package starter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import graph.Edge;
import pattern.skel4.Skel4Lexer;
import pattern.skel4.Skel4Parser;
import tree.model.SkeletonPatt;
import visitor.TBuilder2;

public class AppStarter {
	List<File> inputCodes;
	List<SkeletonPatt> inputs;
	
	public AppStarter(String folderPath) {
		try {
			inputCodes = readAllInputCodesInFolder(folderPath);
			inputs = new ArrayList<>();
		
		for(File file: inputCodes) {
		Skel4Lexer lexer = null;
			
				lexer = new Skel4Lexer(CharStreams.fromFileName(file.getPath()));
			
			Skel4Parser parser = new Skel4Parser(new CommonTokenStream(lexer));
			ParseTree tree = parser.skeletonProgram();
			TBuilder2 tb = new TBuilder2();
			inputs.add(tb.visit(tree));
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		List<ForkJoinTask<List<List<Edge>>>> forks = new ArrayList<>();
		
		List<List<Edge>> results = new ArrayList<>();
		
//		for(File f : inputCodes) {
//			forks.add(new Starter(f).fork());
//		}
		for(SkeletonPatt p : inputs) {
			forks.add(new Starter(p).fork());
		}
		
		for(ForkJoinTask<List<List<Edge>>> task: forks)
			results.addAll(task.join());
		
		StringJoiner stringPaths = new StringJoiner("\n");
		for(List<Edge> edge: results) {
			StringJoiner path = new StringJoiner(",","(",")");
			edge.stream().map(Edge::getRule).forEach(r -> path.add(r.getRule()));
//			System.out.println(path.toString());
			stringPaths.merge(path);
		}
		System.out.println("end");
		System.out.println(stringPaths.toString());
	}
	private List<File> readAllInputCodesInFolder(String folderPath) throws IOException {
		
		return Files.walk(Paths.get(folderPath))
				.filter(Files::isRegularFile)
			.map(Path::toFile)
			.collect(Collectors.toList());
		
	}
	
	public static void main(String[] args) {
		 new AppStarter("C:\\Users\\me\\Desktop\\in");
		
	}
			
}