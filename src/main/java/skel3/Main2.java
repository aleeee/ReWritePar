//package skel3;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.stream.Collectors;
//
//import org.antlr.v4.runtime.CharStreams;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.antlr.v4.runtime.tree.ParseTree;
//import org.jgrapht.io.Attribute;
//import org.jgrapht.io.ComponentAttributeProvider;
//import org.jgrapht.io.ComponentNameProvider;
//import org.jgrapht.io.DOTExporter;
//import org.jgrapht.io.ExportException;
//import org.jgrapht.io.GraphExporter;
//import org.jgrapht.io.JSONExporter;
//
//import cpo.CPOSolver;
//import cpo.CPOSolver2;
//import graph.DiGraphGen4;
//import graph.DiGraphGen4.Edge;
//import guru.nidi.graphviz.engine.Format;
//import guru.nidi.graphviz.engine.Graphviz;
//import guru.nidi.graphviz.engine.GraphvizJdkEngine;
//import pattern.skel4.Skel4Lexer;
//import pattern.skel4.Skel4Parser;
//import solver.Solver;
//import solver.Solver10;
//import solver.Solver11;
//import solver.Solver12;
//import solver.Solver13;
//import solver.Solver2;
//import solver.Solver3;
//import solver.Solver4;
//import solver.Solver5;
//import solver.Solver6;
//import solver.Solver7;
//import solver.Solver8;
//import solver.Solver9;
//import tree.model.FarmPatt;
//import tree.model.SkeletonPatt;
//import visitor.TBuilder2;
//
//public class Main2 {
//	/**
//	 * @param <T>
//	 * @param args
//	 * @throws Exception
//	 */
//	public static <T> void main(String[] args) throws Exception {
//		Main2 m = new Main2();
//		m.start(args);
//	}
//		public void start(String [] args) throws Exception {
//		if (args.length == 0) {
//			args = new String[] { "src/main/pattern/pattern2.skel" };
//		}
//
//		System.out.println("parsing: " + args[0]);
//		Path path = Paths.get(args[0]);
//		Skel4Lexer lexer = new Skel4Lexer(CharStreams.fromPath(path));
//		Skel4Parser parser = new Skel4Parser(new CommonTokenStream(lexer));
//		ParseTree tree = parser.skeletonProgram();
////		Visitor6 visitor3 = new Visitor6();
//		TBuilder2 tb = new TBuilder2();
////		SkeletonPatt n = visitor3.visit(tree); // parse and construct the tree
//		SkeletonPatt n = tb.visit(tree);
//		
////		BFS bfs = new BFS();
////		bfs.bfs(n);
////		MCTS2 mcts = new MCTS2();
////		mcts.selectAction(n);
//		
//		DiGraphGen4 dg = new  DiGraphGen4();
//		dg.bfs(n, 4);
////		System.out.println(dg);
////		SkeletonPatt p = dg.g.vertexSet().iterator().next();
////		System.out.println(p);
//		for(SkeletonPatt p: dg.g.vertexSet()) {
//			try {
////			if(p.getChildren().stream().noneMatch(sk-> sk instanceof FarmPatt))
////			continue;
////		System.out.println(p);
////		solver.Model3 model = new solver.Model3(p, 16 );
//		CPOSolver2 model = new CPOSolver2(p, 16);
//		// Solve the model
//		model.solveIt();
//		// Print the solution
//		model.getSolutions();
//		model.cleanup();
//		
////		p.calculateOptimalServiceTime();
////		p.getChildren().forEach(pp->System.out.println(pp));
//		System.out.println( p.print() );
//		
//			}catch(Exception e) {
//				System.out.println("Error at solve" + e.getMessage());
//			}
//			
//		}
//		
////		renderHrefGraph(dg);
////		exportJson(dg);
////		try {
////			Graphviz.fromString(dg.g.toString()).height(200).render(Format.PNG).toFile(new File("C:\\Users\\me\\Desktop\\mg.png"));
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		ViewTree vT = new ViewTree(n);
////		vT.showTree("my tree");
////		RW reWriter = new RW(); // refactors skeletons
////		n.setReWriteNodes(true);
////		n.refactor(reWriter);
////
////		NodesVisitor v = new NodesVisitor(); /// calculates the service time
////		n.accept(v);
////		n.getPatterns().forEach(p -> p.accept(v));
////
////		n.getPatterns().stream().sorted(Comparator.comparing(SkeletonPatt::getServiceTime))
////				.forEach(sk -> System.out.println(sk + "\t" + sk.getServiceTime()));
//
//	}
//
//	 private static void renderHrefGraph(DiGraphGen4 dg_)
//	 
//		        throws ExportException
//		    {	
////		 dg.g.removeAllVertices( dg.g.vertexSet().stream().filter( v ->  v.getPatterns() == null).collect(Collectors.toList()));
//		        // @example:render:begin
//
//		        // use helper classes to define how vertices should be rendered,
//		        // adhering to the DOT language restrictions
//		        ComponentNameProvider<SkeletonPatt> vertexIdProvider = new ComponentNameProvider<SkeletonPatt>()
//		        {
//		     
//					@Override
//					public String getName(SkeletonPatt component) {
//						// TODO Auto-generated method stub
//						return String.valueOf(component.toString().hashCode());
//					}
//		        };
//		        ComponentNameProvider<SkeletonPatt> vertexLabelProvider = new ComponentNameProvider<SkeletonPatt>()
//		        {
//		            public String getName(SkeletonPatt sk)
//		            {
//		                return sk.print();
//		            }
//		        };
//		        
//		        ComponentNameProvider<DiGraphGen4.Edge> edgeLabelProvider = (DiGraphGen4.Edge sk) -> {return sk.getRule() != null? sk.getRule().toString():
//		        													"NO LABLE";}; 
////		        GraphExporter<SkeletonPatt, Edge> exporter =
////		            new DOTExporter<>(vertexIdProvider, vertexLabelProvider, null);
////		        GraphExporter<SkeletonPatt, Edge> exporter = 	new JSONExporter<SkeletonPatt, Edge>(vertexIdProvider);
////		        ComponentNameProvider<SkeletonPatt> vertexIDProvider = (SkeletonPatt pat) ->  pat.toString().hashCode() ;
////				ComponentAttributeProvider<SkeletonPatt> vertexAttributeProvider=(SkeletonPatt pat) -> 
////				{return null;} ;
////				ComponentNameProvider<Edge> edgeIDProvider = (Edge e) -> {return e.getRule().toString();};
////				ComponentAttributeProvider<Edge> edgeAttributeProvider = (Edge ee) ->
////				{return null;};
//				  GraphExporter<SkeletonPatt, DiGraphGen4.Edge> exporter =//new JSONExporter<SkeletonPatt, Edge>(vertexIDProvider, vertexAttributeProvider, edgeIDProvider, edgeAttributeProvider);
//		        		new DOTExporter<SkeletonPatt, DiGraphGen4.Edge>(vertexIdProvider, vertexLabelProvider, edgeLabelProvider);
//		        Writer writer=new StringWriter();
//		        File f = new File("C:\\\\Users\\\\me\\\\Desktop\\\\out\\\\ddd1.dot");;
////				try {
////					writer = new FileWriter("C:\\\\Users\\\\me\\\\Desktop\\\\out\\\\d1.dot");
////				} catch (IOException e1) {
////					// TODO Auto-generated catch block
////					e1.printStackTrace();
////				}
//		        
//		        exporter.exportGraph(dg_.g, f);
//		        
////		        System.out.println(writer.toString());
//		       
//		        try {
//		        	
//		        	Graphviz.useEngine(new GraphvizJdkEngine());
////					CGraphviz..VizjsOptions();
//					Graphviz.fromFile(f).totalMemory(160000000).height(900).render(Format.SVG_STANDALONE)
//						.toFile(new File("C:\\Users\\me\\Desktop\\out\\"+dg_.hashCode()+"new.svg"));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		        // @example:render:end
//		    }
//	 private static void exportJson(DiGraphGen4 dgJson)  throws ExportException, IOException
//		    {	
//		 AtomicInteger id = new AtomicInteger();
////		 dgJson.g.removeAllVertices( dgJson.g.vertexSet().stream().filter( v ->  v.getPatterns() == null).collect(Collectors.toList()));
//		       
//		       
//		        
//		        ComponentNameProvider<Edge> edgeLabelProvider = (Edge sk) -> {return sk.getRule() != null? sk.getRule().toString():
//		        													"NO LABLE";}; 
//		        ComponentNameProvider<SkeletonPatt> vertexIDProvider = (SkeletonPatt pat) ->  String.valueOf(pat.getId()) ;
//				ComponentAttributeProvider<SkeletonPatt> vertexAttributeProvider= new ComponentAttributeProvider<SkeletonPatt>() {
//					
//					@Override
//					public Map<String, Attribute> getComponentAttributes(SkeletonPatt node) {
//						  Map<String, Attribute> attrs = new HashMap<>();
//						    attrs.put("children", (Attribute) node.getChildren().get(0));
//						   
//						    return attrs;
//					}
//				};
////				ComponentNameProvider<Edge> edgeIDProvider = (Edge e) -> String.valueOf(id.incrementAndGet());
//				ComponentNameProvider<Edge> edgeIDProvider = (Edge e) -> String.valueOf(e.getRule());
//
//				ComponentAttributeProvider<Edge> edgeAttributeProvider = new ComponentAttributeProvider<DiGraphGen4.Edge>() {
//					
//					@Override
//					public Map<String, Attribute> getComponentAttributes(Edge edge) {
//						Map<String, Attribute> attrs = new HashMap<>();
//					    attrs.put("rule", (Attribute) edge.getVertex());
//					   
//					    return attrs;
//					}
//				};
////		        GraphExporter<SkeletonPatt, Edge> exporter = 	new JSONExporter<SkeletonPatt, Edge>(vertexIDProvider);
//
//				
//				  GraphExporter<SkeletonPatt, Edge> exporter =new JSONExporter<SkeletonPatt, Edge>(vertexIDProvider, vertexAttributeProvider, edgeIDProvider, edgeAttributeProvider);
////		        		new DOTExporter<SkeletonPatt, DiGraphGen3.Edge>(vertexIdProvider, vertexLabelProvider, edgeLabelProvider);
//		        Writer writer=new StringWriter();
////		        File f = 
//		        		writer = new FileWriter("C:\\\\Users\\\\me\\\\Desktop\\\\out\\json\\jsonFull1_.json");
//		        exporter.exportGraph(dgJson.g, writer);
//		        
//		       
//		        
//		    }
//	 
//	  class VAttributeProv implements ComponentAttributeProvider<SkeletonPatt> {
//		  
//
//		  @Override
//		  public Map<String, Attribute> getComponentAttributes(SkeletonPatt node) {
//			  Map<String, Attribute> attrs = new HashMap<>();
//		    attrs.put("children", (Attribute) node.getPatterns());
//		   
//		    return attrs;
//		  }
//
//		
//	  }
//
//}
