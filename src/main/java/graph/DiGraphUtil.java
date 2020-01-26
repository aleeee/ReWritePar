package graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.jgrapht.Graph;
import org.jgrapht.io.Attribute;
import org.jgrapht.io.ComponentAttributeProvider;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.GraphExporter;
import org.jgrapht.io.JSONExporter;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizJdkEngine;
import tree.model.SkeletonPatt;

public class DiGraphUtil {

	public static void renderHrefGraph(Graph<SkeletonPatt, Edge> g)

			throws ExportException {
//		 dg.g.removeAllVertices( dg.g.vertexSet().stream().filter( v ->  v.getPatterns() == null).collect(Collectors.toList()));
		// @example:render:begin

		// use helper classes to define how vertices should be rendered,
		// adhering to the DOT language restrictions
		ComponentNameProvider<SkeletonPatt> vertexIdProvider = new ComponentNameProvider<SkeletonPatt>() {

			@Override
			public String getName(SkeletonPatt component) {
				// TODO Auto-generated method stub
				return String.valueOf(component.toString().hashCode());
			}
		};
		ComponentNameProvider<SkeletonPatt> vertexLabelProvider = new ComponentNameProvider<SkeletonPatt>() {
			public String getName(SkeletonPatt sk) {
				return sk.print();
			}
		};

		ComponentNameProvider<Edge> edgeLabelProvider = (Edge sk) -> {
			return sk.getRule() != null ? sk.getRule().toString() : "NO LABLE";
		};
//		        GraphExporter<SkeletonPatt, Edge> exporter =
//		            new DOTExporter<>(vertexIdProvider, vertexLabelProvider, null);
//		        GraphExporter<SkeletonPatt, Edge> exporter = 	new JSONExporter<SkeletonPatt, Edge>(vertexIdProvider);
//		        ComponentNameProvider<SkeletonPatt> vertexIDProvider = (SkeletonPatt pat) ->  pat.toString().hashCode() ;
//				ComponentAttributeProvider<SkeletonPatt> vertexAttributeProvider=(SkeletonPatt pat) -> 
//				{return null;} ;
//				ComponentNameProvider<Edge> edgeIDProvider = (Edge e) -> {return e.getRule().toString();};
//				ComponentAttributeProvider<Edge> edgeAttributeProvider = (Edge ee) ->
//				{return null;};
		GraphExporter<SkeletonPatt, Edge> exporter = // new JSONExporter<SkeletonPatt, Edge>(vertexIDProvider,
														// vertexAttributeProvider, edgeIDProvider,
														// edgeAttributeProvider);
				new DOTExporter<SkeletonPatt, Edge>(vertexIdProvider, vertexLabelProvider, edgeLabelProvider);
		Writer writer = new StringWriter();
		File f = new File("C:\\\\Users\\\\me\\\\Desktop\\\\out\\cpo\\ddd1.dot");
		;
//				try {
//					writer = new FileWriter("C:\\\\Users\\\\me\\\\Desktop\\\\out\\\\d1.dot");
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}

		exporter.exportGraph(g, f);

//		        System.out.println(writer.toString());

		try {

			Graphviz.useEngine(new GraphvizJdkEngine());
//					CGraphviz..VizjsOptions();
			Graphviz.fromFile(f).totalMemory(160000000).height(900).render(Format.SVG_STANDALONE)
					.toFile(new File("C:\\Users\\me\\Desktop\\out\\cpo\\" + g.hashCode() + "new.svg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// @example:render:end
	}

	private static void exportJson(Graph<SkeletonPatt, Edge> g) throws ExportException, IOException {
		AtomicInteger id = new AtomicInteger();
//		 dgJson.g.removeAllVertices( dgJson.g.vertexSet().stream().filter( v ->  v.getPatterns() == null).collect(Collectors.toList()));

		ComponentNameProvider<Edge> edgeLabelProvider = (Edge sk) -> {
			return sk.getRule() != null ? sk.getRule().toString() : "NO LABLE";
		};
		ComponentNameProvider<SkeletonPatt> vertexIDProvider = (SkeletonPatt pat) -> String.valueOf(pat.getId());
		ComponentAttributeProvider<SkeletonPatt> vertexAttributeProvider = new ComponentAttributeProvider<SkeletonPatt>() {

			@Override
			public Map<String, Attribute> getComponentAttributes(SkeletonPatt node) {
				Map<String, Attribute> attrs = new HashMap<>();
				attrs.put("children", (Attribute) node.getChildren().get(0));

				return attrs;
			}
		};
//				ComponentNameProvider<Edge> edgeIDProvider = (Edge e) -> String.valueOf(id.incrementAndGet());
		ComponentNameProvider<Edge> edgeIDProvider = (Edge e) -> String.valueOf(e.getRule());

		ComponentAttributeProvider<Edge> edgeAttributeProvider = new ComponentAttributeProvider<Edge>() {

			@Override
			public Map<String, Attribute> getComponentAttributes(Edge edge) {
				Map<String, Attribute> attrs = new HashMap<>();
				attrs.put("rule", (Attribute) edge.getVertex());

				return attrs;
			}
		};
//		        GraphExporter<SkeletonPatt, Edge> exporter = 	new JSONExporter<SkeletonPatt, Edge>(vertexIDProvider);

		GraphExporter<SkeletonPatt, Edge> exporter = new JSONExporter<SkeletonPatt, Edge>(vertexIDProvider,
				vertexAttributeProvider, edgeIDProvider, edgeAttributeProvider);
//		        		new DOTExporter<SkeletonPatt, DiGraphGen3.Edge>(vertexIdProvider, vertexLabelProvider, edgeLabelProvider);
		Writer writer = new StringWriter();
//		        File f = 
		writer = new FileWriter("C:\\\\Users\\\\me\\\\Desktop\\\\out\\json\\jsonFull1_.json");
		exporter.exportGraph(g, writer);

	}

	class VAttributeProv implements ComponentAttributeProvider<SkeletonPatt> {

		@Override
		public Map<String, Attribute> getComponentAttributes(SkeletonPatt node) {
			Map<String, Attribute> attrs = new HashMap<>();
			attrs.put("children", (Attribute) node.getPatterns());

			return attrs;
		}

	}
}
