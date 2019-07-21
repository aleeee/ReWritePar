package skel3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.dgc.DGC;
import java.util.Comparator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import bfs.BFS;
import mcts.MCTS;
import mcts.MCTS2;
import pattern.skel4.Skel4Lexer;
import pattern.skel4.Skel4Parser;
import rewriter.RW;
import test.DigraphT2;
import tree.model.SkeletonPatt;
import view.ViewTree;
import visitor.NodesVisitor;
import visitor.TBuilder2;

public class Main2 {
	/**
	 * @param <T>
	 * @param args
	 * @throws Exception
	 */
	public static <T> void main(String[] args) throws Exception {

		if (args.length == 0) {
			args = new String[] { "src/main/pattern/pattern2.skel" };
		}

		System.out.println("parsing: " + args[0]);
		Path path = Paths.get(args[0]);
		Skel4Lexer lexer = new Skel4Lexer(CharStreams.fromPath(path));
		Skel4Parser parser = new Skel4Parser(new CommonTokenStream(lexer));
		ParseTree tree = parser.skeletonProgram();
//		Visitor6 visitor3 = new Visitor6();
		TBuilder2 tb = new TBuilder2();
//		SkeletonPatt n = visitor3.visit(tree); // parse and construct the tree
		SkeletonPatt n = tb.visit(tree);
		
//		BFS bfs = new BFS();
//		bfs.bfs(n);
//		MCTS2 mcts = new MCTS2();
//		mcts.selectAction(n);
		
		DigraphT2 dg = new  DigraphT2();
		dg.bfs(n);
		System.out.println(dg);
//		RW reWriter = new RW(); // refactors skeletons
//		n.setReWriteNodes(true);
//		n.refactor(reWriter);
//
//		NodesVisitor v = new NodesVisitor(); /// calculates the service time
//		n.accept(v);
//		n.getPatterns().forEach(p -> p.accept(v));
//
//		n.getPatterns().stream().sorted(Comparator.comparing(SkeletonPatt::getServiceTime))
//				.forEach(sk -> System.out.println(sk + "\t" + sk.getServiceTime()));

	}


}
