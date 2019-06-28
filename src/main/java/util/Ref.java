package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import rewriter.RW;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class Ref {
	private static RW reWriter = new RW();

	/**
	 * refactor Seqential pattern
	 * 
	 * @param seq
	 * @return Seq with its possible refactoring options (list of patterns)
	 */
	public static SeqPatt refactor(SeqPatt seq) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();
		// farm intro
		FarmPatt farm = new FarmPatt("farm", 0);
		farm.setChild(seq);
		patterns.add(farm);
		// map intro
		MapPatt map = new MapPatt("map", 0);
		map.setChild(seq);
		patterns.add(map);
		seq.setPatterns(patterns);

		return seq;
	}

	/**
	 * reWrite Comp pattern
	 * 
	 * @param comp
	 * @return CompPatt with its possible refactoring options (list of patterns)
	 */
	public static CompPatt refactor(CompPatt comp) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();
		// pipe intro
		PipePatt pipe = new PipePatt("pipe", 0);
		pipe.setChildren(comp.getChildren());
		patterns.add(pipe);

		// farm intro
		FarmPatt farm = new FarmPatt("farm", 0);
		farm.setChild(comp);
		patterns.add(farm);

		// map intro

		// for each stage
		// rewrite
		if (comp.reWriteNodes()) {
			for (SkeletonPatt skel : comp.getChildren()) {
				skel.setReWriteNodes(false);
				skel.refactor(reWriter);
			}
		}
		return comp;
	}

	/**
	 * reWrites Farm pattern
	 * 
	 * @param farm
	 * @return Farm pattern with its possible refactoring options (list of patterns)
	 */
	public static FarmPatt refactor(FarmPatt farm) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();

		// farm elim
		patterns.add(farm.getChild());
		// farm intro
		FarmPatt farmPat = new FarmPatt("farm", 0);
		farmPat.setChild(farm);
		patterns.add(farmPat);

		if (farm.reWriteNodes()) {
			farm.getChild().setReWriteNodes(false);
			farm.getChild().refactor(reWriter);
		}
		farm.setPatterns(patterns);
		return farm;
	}

	/**
	 * rewrites Pipe pattern
	 * 
	 * @param pipe
	 * @return Pipeline pattern with its possible refactoring options (list of
	 *         patterns)
	 */
	public static PipePatt refactor(PipePatt pipe) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();

		// farm intro
		FarmPatt farm = new FarmPatt("farm", 0);
		farm.setChild(pipe);
		patterns.add(farm);

		// pipe elim
		CompPatt comp = new CompPatt("comp", 0);
		comp.setChildren(pipe.getChildren());
		patterns.add(comp);

		// pipeassoc pipe(D1; pipe(D2;D3)) = pipe(pipe(D1;D2);D3)
		if (pipe.getChildren() != null && pipe.getChildren().stream().anyMatch(sk -> sk instanceof PipePatt)) {

			PipePatt p0 = (PipePatt) pipe.getChildren().stream().filter(e -> e instanceof PipePatt).findFirst()
					.orElse(null);
			if (p0 != null) {
				int index = pipe.getChildren().indexOf(p0);
				if (index == 0) {

					PipePatt pipe0 = (PipePatt) pipe.getChildren().get(index);
					SkeletonPatt pat = pipe0.getChildren().get(0);
					ArrayList<SkeletonPatt> innerPipeNodes = new ArrayList<SkeletonPatt>();
					ArrayList<SkeletonPatt> outerPipeNodes = new ArrayList<SkeletonPatt>();

					PipePatt outerPipe = new PipePatt("pipe", 0);
					PipePatt innerPipe = new PipePatt("pipe", 0);
					for (int i = 1; i < pipe0.getChildren().size(); i++) { // start i at 1 because we took the first
																			// element to form associative pipe
						innerPipeNodes.add(pipe0.getChildren().get(i));
					}
					innerPipeNodes.addAll(pipe.getChildren().subList(1, pipe.getChildren().size()));
					innerPipe.setChildren(innerPipeNodes);
					outerPipeNodes.add(pat);
					outerPipeNodes.add(innerPipe);
					outerPipe.setChildren(outerPipeNodes);
					patterns.add(outerPipe);
				} else {
					PipePatt pipei = (PipePatt) pipe.getChildren().get(index);
					SkeletonPatt pat = pipei.getChildren().get(pipei.getChildren().size() - 1); // get the last element
																								// of the inner pipe
					ArrayList<SkeletonPatt> innerPipeNodes = new ArrayList<SkeletonPatt>();
					ArrayList<SkeletonPatt> outerPipeNodes = new ArrayList<SkeletonPatt>();

					PipePatt outerPipe = new PipePatt("pipe", 0);
					PipePatt innerPipe = new PipePatt("pipe", 0);
					innerPipeNodes.addAll(pipe.getChildren().subList(0, index));
					innerPipeNodes.addAll(pipei.getChildren().subList(0, pipei.getChildren().size() - 1));
					innerPipe.setChildren(innerPipeNodes);
					// eg . pipe(a, pipe(b,c), d) ----> pipe(pipe(a,b),c,d)

					outerPipeNodes.add(innerPipe);
					outerPipeNodes.addAll(pipe.getChildren().subList(index + 1, pipe.getChildren().size())); // if there are elements after inner pipe
					outerPipeNodes.add(pat);

					outerPipe.setChildren(outerPipeNodes);
					patterns.add(outerPipe);

				}
			}
		}
		// mapofpipe pipe(map(D1);map(D2))= map(pipe((D1;D2))
		// if pipe has map stages
				// remove the map as map.getchild
				// create pipe of map.getchild for all
				// creat map of the pipe
				if (pipe.getChildren().stream().allMatch(sk -> sk instanceof MapPatt)) {
					System.out.println("maps");
					ArrayList<SkeletonPatt> listOfChildrens = (ArrayList<SkeletonPatt>) pipe.getChildren().stream()
							.map(p -> p.getChild()).collect(Collectors.toList());
					MapPatt map = new MapPatt("map", 0);

					PipePatt piMap = new PipePatt(pipe.getLable(), listOfChildrens.stream().mapToDouble(SkeletonPatt::getServiceTime)
							.reduce(0, (c1, c2) -> c1 > c2 ? c1 : c2));

					piMap.setChildren(listOfChildrens);
					map.setChild(piMap);
					patterns.add(map);
				}
		// refactor stages
		if (pipe.reWriteNodes()) {
			for (SkeletonPatt skel : pipe.getChildren()) {
				skel.setReWriteNodes(false);
				skel.refactor(reWriter);
			}

			List<List<SkeletonPatt>> stages = Util.getStagesPatterns(pipe);
			for (List<SkeletonPatt> stage : stages) {
				CompPatt compPat = new CompPatt("comp", 0);
				compPat.setChildren((ArrayList<SkeletonPatt>) stage);
				patterns.add(compPat);

				PipePatt pipePat = new PipePatt("pipe", 0);
				pipePat.setChildren((ArrayList<SkeletonPatt>) stage);
				patterns.add(pipePat);

				FarmPatt farmPat = new FarmPatt("farm", 0);
				farmPat.setChild(pipePat);
				patterns.add(farmPat);
			}
		}
		pipe.setPatterns(patterns);
		return pipe;
	}

	/**
	 * rewrites Map pattern
	 * 
	 * @param map
	 * @return Map pattern with its possible refactoring options (list of patterns)
	 */
	public static MapPatt refactor(MapPatt map) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();
//		mapelim map(D)!D
		patterns.add(map.getChild());

		// compofmap map(comp(D1;D2)!comp((map(D1);map(D2)) and pipeofmap
		// map(pipe(D1;D2) = pipe((map(D1);map(D2))

		if (map.getChild() instanceof CompPatt) {
			CompPatt c = (CompPatt) map.getChild();
			SkeletonPatt pat = map.getChild();
			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
			for (SkeletonPatt sk : pat.getChildren()) {
				MapPatt m = new MapPatt("map", 0);
				m.setChild(sk);
				nodes.add(m);
			}
			pat.setChildren(nodes);
			patterns.add(pat);
		}

		// farm intro
		FarmPatt farm = new FarmPatt("farm", 0);
		farm.setChild(map);
		patterns.add(farm);

		map.setPatterns(patterns);

		if (map.reWriteNodes()) {
			map.getChild().setReWriteNodes(false);
			map.getChild().refactor(reWriter);
		}

		return map;
	}

}
