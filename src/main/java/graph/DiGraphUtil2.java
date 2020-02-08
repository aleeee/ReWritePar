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

public class DiGraphUtil2 {


	public static void exportJson(Graph<SkeletonPatt, Edge> g) throws ExportException, IOException {

		ComponentNameProvider<Edge> edgeLabelProvider = (Edge sk) -> {
			return sk.getRule() != null ? sk.getRule().toString() : "NO LABLE";
		};
		ComponentNameProvider<SkeletonPatt> vertexLabelProvider = new ComponentNameProvider<SkeletonPatt>() {
			public String getName(SkeletonPatt sk) {
				return sk.print();
			}
		};
		ComponentNameProvider<SkeletonPatt> vertexIDProvider = (SkeletonPatt pat) -> String.valueOf(pat.getId());
		ComponentAttributeProvider<SkeletonPatt> vertexAttributeProvider = new ComponentAttributeProvider<SkeletonPatt>() {

			@Override
			public Map<String, Attribute> getComponentAttributes(SkeletonPatt node) {
				Map<String, Attribute> attrs = new HashMap<>();
				attrs.put("children",  node.getPatterns() != null? (Attribute)node.getPatterns().iterator().next():(Attribute) node);
				return attrs;
			}
		};
		ComponentNameProvider<Edge> edgeIDProvider = (Edge e) -> String.valueOf(e.getRule());

		ComponentAttributeProvider<Edge> edgeAttributeProvider = new ComponentAttributeProvider<Edge>() {

			@Override
			public Map<String, Attribute> getComponentAttributes(Edge edge) {
				Map<String, Attribute> attrs = new HashMap<>();
				attrs.put("rule", (Attribute) edge.getVertex());

				return attrs;
			}
		};

		GraphExporter<SkeletonPatt, Edge> exporter = new JSONExporter<SkeletonPatt, Edge>(vertexIDProvider,
				vertexAttributeProvider, edgeIDProvider, edgeAttributeProvider);
//		        		new DOTExporter<SkeletonPatt, DiGraphGen3.Edge>(vertexIdProvider, vertexLabelProvider, edgeLabelProvider);
		Writer writer = new StringWriter();
//		        File f = 
		writer = new FileWriter("C:\\\\Users\\\\me\\\\Desktop\\\\out\\json\\jsonFull2_.json");
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
