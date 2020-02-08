package graph;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.io.Attribute;
import org.jgrapht.io.ComponentAttributeProvider;

import tree.model.SkeletonPatt;

class VAttributeProvider implements ComponentAttributeProvider<SkeletonPatt> {

	@Override
	public Map<String, Attribute> getComponentAttributes(SkeletonPatt node) {
		Map<String, Attribute> attrs = new HashMap<>();
		attrs.put("children", (Attribute) node.getPatterns());

		return attrs;
	}

}