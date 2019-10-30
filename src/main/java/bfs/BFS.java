package bfs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import rewriter.RW;
import tree.model.SkeletonPatt;

public class BFS {
	private Queue<SkeletonPatt> queue = new LinkedList<SkeletonPatt>();
	ArrayList<SkeletonPatt> visited = new ArrayList<SkeletonPatt>();
	RW rw = new RW();

	public void bfs(SkeletonPatt s) {
		s.refactor(rw);
		queue.add(s);
//		s.visited = true;
		System.out.println("skel" + s);
		System.out.println(queue.size());
		while (!queue.isEmpty()) {
			SkeletonPatt curNode = queue.remove();
			System.out.println("cur " + curNode);
			List<SkeletonPatt> children = curNode.getChildren();
			SkeletonPatt child = curNode.getChild();
			if (children != null) {
				for (SkeletonPatt node : children) {
//				if(!node.visited) {
					node.refactor(rw);
					queue.add(node);
//					node.visited= false;
//				}
				}
			}
			if (child != null) {
				queue.add(child);
				child.refactor(rw);
			}
		}
		System.out.println(s);
		s.getPatterns().stream().sorted(Comparator.comparing(SkeletonPatt::getIdealServiceTime))
				.forEach(sk -> System.out.println(sk + "\t" + sk.getIdealServiceTime()));
		if (s.getChildren() != null) {
			s.getChildren()
					.forEach(node -> node.getPatterns().stream()
							.sorted(Comparator.comparing(SkeletonPatt::getIdealServiceTime))
							.forEach(sk -> System.out.println("nodes : " + sk + "\t" + sk.getIdealServiceTime())));
		}
		if (s.getChild() != null) {
			s.getChild().getPatterns().stream().sorted(Comparator.comparing(SkeletonPatt::getIdealServiceTime))
					.forEach(sk -> System.out.println("nodes : " + sk + "\t" + sk.getIdealServiceTime()));
		}
		
	}
}
