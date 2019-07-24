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

public class Ref2 {
	private static RW reWriter = new RW();

	/**
	 * refactor Seqential pattern
	 * 
	 * @param seq
	 * @return Seq with its possible refactoring options (list of patterns)
	 */
	public static SeqPatt refactor(SeqPatt seq) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();
		ArrayList<SkeletonPatt> fc = new  ArrayList<SkeletonPatt>();
		fc.add(seq);
		// farm intro
		FarmPatt farm = new FarmPatt("farm", 0);
		farm.setChildren(fc);
		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
		patterns.add(farm);
		seq.setPatterns(patterns);
		if(seq.getParent() != null) 
		seq.setPatterns(Util.createTreeNode(seq.getParent(), seq));
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
		ArrayList<SkeletonPatt> fc = new  ArrayList<SkeletonPatt>();

		// pipe intro
		PipePatt pipe = new PipePatt("pipe", 0);
		pipe.setChildren(comp.getChildren());
		pipe.setReWritingRule(ReWritingRules.PIPE_INTRO);
		patterns.add(pipe);
		

		// farm intro
		FarmPatt farm = new FarmPatt("farm", 0);
		fc.add(comp);
		farm.setChildren(fc);
		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
		patterns.add(farm);

		// map intro

		// for each stage
		// rewrite
		if (comp.reWriteNodes()) {
			for (SkeletonPatt skel : comp.getChildren()) {
				skel.setReWriteNodes(false);
				skel.refactor(reWriter);
			}
			List<List<SkeletonPatt>> stages = Util.getStagesPatterns(comp);
			for (List<SkeletonPatt> stage : stages) {
				CompPatt compPat = new CompPatt("comp", 0);
				compPat.setChildren((ArrayList<SkeletonPatt>) stage);
				compPat.setReWritingRule(ReWritingRules.PIPE_ELIM);
				patterns.add(compPat);

				PipePatt pipePat = new PipePatt("pipe", 0);
				pipePat.setChildren((ArrayList<SkeletonPatt>) stage);
				pipePat.setReWritingRule(ReWritingRules.PIPE_INTRO);
				patterns.add(pipePat);

				FarmPatt farmPat = new FarmPatt("farm", 0);
				fc = new ArrayList<SkeletonPatt>();
				fc.add(pipePat);
				farmPat.setChildren(fc);
				farmPat.setReWritingRule(ReWritingRules.FARM_INTRO);
				patterns.add(farmPat);
			}
		}
		comp.setPatterns(patterns);
		if(comp.getParent() != null) 
		comp.setPatterns(Util.createTreeNode(comp.getParent(), comp));
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
		SkeletonPatt c = farm.getChildren().get(0);
		c.setReWritingRule(ReWritingRules.FARM_ELIM);
		patterns.add(c);
		// farm intro
		FarmPatt farmPat = new FarmPatt("farm", 0);
		ArrayList<SkeletonPatt> fc = new  ArrayList<SkeletonPatt>();
		fc.add(farm);
		farmPat.setChildren(fc);
		farmPat.setReWritingRule(ReWritingRules.FARM_INTRO);
		patterns.add(farmPat);

		if (farm.reWriteNodes()) {
			farm.getChildren().get(0).setReWriteNodes(false);
			farm.getChildren().get(0).refactor(reWriter);
			
			for (SkeletonPatt p : farm.getChildren().get(0).getPatterns()) {
				FarmPatt fp = new FarmPatt("farm", 0);
				 fc = new  ArrayList<SkeletonPatt>();
				fc.add(p);
				fp.setChildren(fc);
				fp.setReWritingRule(ReWritingRules.FARM_INTRO);
				patterns.add(fp);
			}
		}
		farm.setPatterns(patterns);
		if(farm.getParent() != null) 
		farm.setPatterns(Util.createTreeNode(farm.getParent(), farm));

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
		ArrayList<SkeletonPatt> fc = new  ArrayList<SkeletonPatt>();
		fc.add(pipe);
		farm.setChildren(fc);
		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
		patterns.add(farm);

		// pipe elim
		CompPatt comp = new CompPatt("comp", 0);
		comp.setChildren(pipe.getChildren());
		comp.setReWritingRule(ReWritingRules.PIPE_ELIM);
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
					outerPipe.setReWritingRule(ReWritingRules.PIPE_ASSOC);
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
					outerPipe.setReWritingRule(ReWritingRules.PIPE_ASSOC);
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
							.map(p -> p.getChildren().get(0)).collect(Collectors.toList());
					MapPatt map = new MapPatt("map", 0);

					PipePatt piMap = new PipePatt(pipe.getLable(), listOfChildrens.stream().mapToDouble(SkeletonPatt::getServiceTime)
							.reduce(0, (c1, c2) -> c1 > c2 ? c1 : c2));

					piMap.setChildren(listOfChildrens);
					map.setChild(piMap);
					piMap.setReWritingRule(ReWritingRules.MAP_DIST);
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
				compPat.setReWritingRule(ReWritingRules.PIPE_ELIM);
				patterns.add(compPat);

				PipePatt pipePat = new PipePatt("pipe", 0);
				pipePat.setChildren((ArrayList<SkeletonPatt>) stage);
				pipePat.setReWritingRule(ReWritingRules.PIPE_INTRO);
				patterns.add(pipePat);
				 fc = new  ArrayList<SkeletonPatt>();

				FarmPatt farmPat = new FarmPatt("farm", 0);
				fc.add(pipePat);
				farmPat.setChildren(fc);
				farmPat.setReWritingRule(ReWritingRules.FARM_INTRO);
				patterns.add(farmPat);
			}
		}
		pipe.setPatterns(patterns);
		if(pipe.getParent() != null) 
		pipe.setPatterns(Util.createTreeNode(pipe.getParent(), pipe));

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
		if(map.getChildren() != null) {
			System.out.println("Map can can not have stages ");
			System.exit(1);
		}
//		mapelim map(D)!D
		patterns.add(map.getChildren().get(0));

		// compofmap map(comp(D1;D2)!comp((map(D1);map(D2)) and pipeofmap
		// map(pipe(D1;D2) = pipe((map(D1);map(D2))

		if (map.getChildren().get(0) instanceof CompPatt) {
			CompPatt c = (CompPatt) map.getChildren().get(0);
			SkeletonPatt pat = map.getChildren().get(0);
			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
			for (SkeletonPatt sk : pat.getChildren()) {
				MapPatt m = new MapPatt("map", 0);
				m.setChild(sk);
				nodes.add(m);
			}
			pat.setChildren(nodes);
			pat.setReWritingRule(ReWritingRules.MAP_DIST);
			patterns.add(pat);
		}

		// farm intro
		ArrayList<SkeletonPatt> fc = new  ArrayList<SkeletonPatt>();

		FarmPatt farm = new FarmPatt("farm", 0);
		fc.add(map);
		farm.setChildren(fc);
		patterns.add(farm);
		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
		map.setPatterns(patterns);

		if (map.reWriteNodes()) {
			map.getChildren().get(0).setReWriteNodes(false);
			map.getChildren().get(0).refactor(reWriter);
		}
		if(map.getParent() != null) 
		map.setPatterns(Util.createTreeNode(map.getParent(), map));

		return map;
	}

}
