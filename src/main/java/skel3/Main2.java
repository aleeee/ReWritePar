package skel3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.ComponentAttributeProvider;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.GraphExporter;
import org.jgrapht.io.JSONExporter;

import com.ibm.icu.impl.duration.impl.DataRecord.EGender;

import edu.emory.mathcs.backport.java.util.Collections;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import pattern.skel4.Skel4Lexer;
import pattern.skel4.Skel4Parser;
import test.DigraphT4;
import test.DigraphT4.Edge;
import tree.model.SkeletonPatt;
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
		
		DigraphT4 dg = new  DigraphT4();
		dg.bfs(n);
		System.out.println(dg);
		renderHrefGraph(dg);
//		try {
//			Graphviz.fromString(dg.g.toString()).height(200).render(Format.PNG).toFile(new File("C:\\Users\\me\\Desktop\\mg.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ViewTree vT = new ViewTree(n);
//		vT.showTree("my tree");
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

	 private static void renderHrefGraph(DigraphT4 dg)
		        throws ExportException
		    {	
		
		        // @example:render:begin

		        // use helper classes to define how vertices should be rendered,
		        // adhering to the DOT language restrictions
		        ComponentNameProvider<SkeletonPatt> vertexIdProvider = new ComponentNameProvider<SkeletonPatt>()
		        {
		     
					@Override
					public String getName(SkeletonPatt component) {
						// TODO Auto-generated method stub
						return String.valueOf(component.toString().hashCode());
					}
		        };
		        ComponentNameProvider<SkeletonPatt> vertexLabelProvider = new ComponentNameProvider<SkeletonPatt>()
		        {
		            public String getName(SkeletonPatt sk)
		            {
		                return sk.toString();
		            }
		        };
		        
		        ComponentNameProvider<Edge> edgeLabelProvider = (Edge sk) -> {return sk.getRule() != null? sk.getRule().toString():
		        													"NO LABLE";}; 
//		        GraphExporter<SkeletonPatt, Edge> exporter =
//		            new DOTExporter<>(vertexIdProvider, vertexLabelProvider, null);
//		        GraphExporter<SkeletonPatt, Edge> exporter = 	new JSONExporter<SkeletonPatt, Edge>(vertexIdProvider);
//		        ComponentNameProvider<SkeletonPatt> vertexIDProvider = (SkeletonPatt pat) ->  pat.toString().hashCode() ;
//				ComponentAttributeProvider<SkeletonPatt> vertexAttributeProvider=(SkeletonPatt pat) -> 
//				{return null;} ;
//				ComponentNameProvider<Edge> edgeIDProvider = (Edge e) -> {return e.getRule().toString();};
//				ComponentAttributeProvider<Edge> edgeAttributeProvider = (Edge ee) ->
//				{return null;};
				  GraphExporter<SkeletonPatt, Edge> exporter =//new JSONExporter<SkeletonPatt, Edge>(vertexIDProvider, vertexAttributeProvider, edgeIDProvider, edgeAttributeProvider);
		        		new DOTExporter<SkeletonPatt, DigraphT4.Edge>(vertexIdProvider, vertexLabelProvider, edgeLabelProvider);
		        Writer writer=new StringWriter();
//				try {
//					writer = new FileWriter("C:\\\\Users\\\\me\\\\Desktop\\\\out\\\\d1.dot");
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
		        exporter.exportGraph(dg.g, writer);
//		        System.out.println(writer.toString());
		       
		        try {
					Graphviz.fromString(writer.toString()).height(200).render(Format.SVG_STANDALONE).toFile(new File("C:\\Users\\me\\Desktop\\out\\s1.svg"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        // @example:render:end
		    }
}
