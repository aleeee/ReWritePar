package skel3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.ibm.icu.impl.CharTrie;

import pattern.skel3.Skel3Lexer;
import pattern.skel3.Skel3Parser;
import rewriter.RW;
import tree.model.SkeletonPatt;
import util.Util;
import visitor.NodesVisitor;
import visitor.Visitor6;

public class Main {
	public static <T> void main(String[] args) throws Exception {

		if (args.length == 0) {
			args = new String[] { "src/main/pattern/pattern2.skel" };
		}

		System.out.println("parsing: " + args[0]);
		Path path = Paths.get(args[0]);
		Skel3Lexer lexer = new Skel3Lexer(CharStreams.fromPath(path));
		Skel3Parser parser = new Skel3Parser(new CommonTokenStream(lexer));
		ParseTree tree = parser.skeletonProgram();
		Visitor6 visitor3 = new Visitor6();
		SkeletonPatt n = visitor3.visit(tree); // parse and construct the tree


		RW reWriter = new RW(); // refactors skeletons
		n.setReWriteNodes(true);
		n.refactor(reWriter);

		NodesVisitor v = new NodesVisitor(); /// calculates the service time
		n.accept(v);
		n.getPatterns().forEach(p -> p.accept(v));

		n.getPatterns().stream().sorted(Comparator.comparing(SkeletonPatt::getServiceTime))
				.forEach(sk -> System.out.println(sk + "\t" + sk.getServiceTime()));

	}


}
