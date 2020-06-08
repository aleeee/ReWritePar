package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import edu.emory.mathcs.backport.java.util.Arrays;
import rewriter.RW;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class ReWrite {
	private static RW reWriter = new RW();

	/**
	 * refactor Seqential pattern
	 * 
	 * @param seq
	 * @return Seq with its possible refactoring options (list of patterns)
	 */
	public static SeqPatt refactor(SeqPatt seq) {
		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
		ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
		SkeletonPatt s = Util.clone(seq);
		fc.add(s);
		// farm intro
		FarmPatt farm = new FarmPatt();
		farm.setChildren(fc);
		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
		farm.calculateIdealServiceTime();
		patterns.add(farm);
		seq.setPatterns(patterns);
		if (seq.getParent() != null)
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
Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
		
		// pipe intro
		PipePatt pipe = new PipePatt();
		pipe.setChildren((ArrayList<SkeletonPatt>) comp.getChildren().stream().map(o -> Util.clone(o)).collect(Collectors.toList()));
		pipe.setReWritingRule(ReWritingRules.PIPE_INTRO);
		pipe.calculateIdealServiceTime();

		patterns.add(pipe);

		FarmPatt farm = new FarmPatt();
		CompPatt compStage = new CompPatt();
		ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
		compStage.setChildren((ArrayList<SkeletonPatt>) comp.getChildren().stream().map(o -> Util.clone(o)).collect(Collectors.toList()));
		compStage.setRule(comp.getRule());
		compStage.calculateIdealServiceTime();
		fc.add(compStage);
		farm.setChildren(fc);
		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
		farm.calculateIdealServiceTime();
		patterns.add(farm);
		// map intro

		// for each stage rewrite
		for (SkeletonPatt stage : comp.getChildren()) {
				stage.setParent(pipe);
				stage.refactor(reWriter);
				patterns.addAll(stage.getPatterns());

		}
		// creat map of the comp
		if (comp.getChildren().stream().allMatch(sk -> sk instanceof MapPatt)) {
			ArrayList<SkeletonPatt> compStages = (ArrayList<SkeletonPatt>) comp.getChildren().stream()
					.map(p -> p.getChildren().get(0)).collect(Collectors.toList());
			ArrayList<SkeletonPatt> mapStages =  (ArrayList<SkeletonPatt>) compStages.stream().map(o -> Util.clone(o)).collect(Collectors.toList());

			MapPatt map = new MapPatt();
			CompPatt compMap = new CompPatt();

			compMap.setChildren(mapStages);
			
			ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
			compMap.calculateIdealServiceTime();
			mNodes.add(compMap);
			map.setChildren(mNodes);
			map.calculateIdealServiceTime();
			map.setReWritingRule(ReWritingRules.MAP_OF_COMP);
			patterns.add(map);
		}

		comp.setPatterns(patterns);
		if (comp.getParent() != null)
			comp.setPatterns(Util.createTreeNode(comp.getParent(), comp));
		comp.calculateIdealServiceTime();
		
		return comp;
	}

	/**
	 * reWrites Farm pattern
	 * 
	 * @param farm
	 * @return Farm pattern with its possible refactoring options (list of patterns)
	 */
	public static FarmPatt refactor(FarmPatt farm) {
		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
		// farm elim
		SkeletonPatt c = Util.clone(farm.getChildren().get(0));
		
		c.setReWritingRule(ReWritingRules.FARM_ELIM);
		c.calculateIdealServiceTime();
		patterns.add(c);

		SkeletonPatt stage = farm.getChildren().get(0);
		stage.setParent(farm);
		stage.refactor(reWriter);
		
		patterns.addAll(stage.getPatterns().stream().filter(p -> !p.getRule().equals(ReWritingRules.FARM_INTRO)).collect(Collectors.toList()));
		
		farm.setPatterns(patterns);
		if (farm.getParent() != null)
			farm.setPatterns(Util.createTreeNode(farm.getParent(), farm));
		farm.calculateIdealServiceTime();
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
	  boolean	isCoarseReWrite = false;
		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();

		FarmPatt farm = new FarmPatt();
		ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
		PipePatt fStage = (PipePatt) Util.clone(pipe);
		fc.add(fStage);
		farm.setChildren(fc);
		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
		farm.calculateIdealServiceTime();
		farm.setReWriteNodes(false);
		patterns.add(farm);

		// pipe elim
		CompPatt comp = new CompPatt();
		
		ArrayList<SkeletonPatt> compStages = (ArrayList<SkeletonPatt>) pipe.getChildren().stream()
				.map(pn -> Util.clone(pn)).collect(Collectors.toList());
		ArrayList<SkeletonPatt> farmWorker = new  ArrayList<>();
		comp.setChildren(compStages);
		comp.setReWritingRule(ReWritingRules.PIPE_ELIM);
		comp.calculateIdealServiceTime();
		if(isCoarseReWrite) {
			FarmPatt farmSkel = new FarmPatt();
			farmWorker.add(comp);
			farmSkel.setChildren(farmWorker);
			farmSkel.setReWritingRule(ReWritingRules.FARM_INTRO);
			patterns.add(farmSkel);
		}else {
			patterns.add(comp);
		}
		// pipeassoc pipe(D1; pipe(D2;D3)) = pipe(pipe(D1;D2);D3)
		if (pipe.getChildren() != null && pipe.getChildren().stream().anyMatch(sk -> sk instanceof PipePatt)) {

			PipePatt p0 =  (PipePatt) pipe.getChildren().stream().filter(e -> e instanceof PipePatt).findFirst().get();
				int index = pipe.getChildren().indexOf(p0);
				if (index == 0) {

					PipePatt pipe0 = (PipePatt) pipe.getChildren().get(index);
					SkeletonPatt pat = Util.clone(pipe0.getChildren().get(0));
					ArrayList<SkeletonPatt> innerPipeNodes = new ArrayList<SkeletonPatt>();
					ArrayList<SkeletonPatt> outerPipeNodes = new ArrayList<SkeletonPatt>();

					PipePatt outerPipe = new PipePatt();
					PipePatt innerPipe = new PipePatt();
					// start i at 1 because we took the first element to form associative pipe
					for (int i = 1; i < pipe0.getChildren().size(); i++) { 
																			
						innerPipeNodes.add(Util.clone(pipe0.getChildren().get(i)));
					}
					innerPipeNodes.addAll(pipe.getChildren().subList(1,pipe.getChildren().size()).stream().map(o -> Util.clone(o)).collect(Collectors.toList()));
					innerPipe.setChildren(innerPipeNodes);
					innerPipe.calculateIdealServiceTime();
					
					outerPipeNodes.add(pat);
					outerPipeNodes.add(innerPipe);
					outerPipe.setChildren(outerPipeNodes);
					outerPipe.setReWritingRule(ReWritingRules.PIPE_ASSOC);
					outerPipe.calculateIdealServiceTime();
					outerPipe.setReWriteNodes(false);
					patterns.add(outerPipe);
				} else {
					PipePatt pipei = (PipePatt) Util.clone(pipe.getChildren().get(index));
					// get the last element of the inner pipe
					SkeletonPatt pat = Util.clone(pipei.getChildren().get(pipei.getChildren().size() - 1)); 
																								
					ArrayList<SkeletonPatt> innerPipeNodes = new ArrayList<SkeletonPatt>();
					ArrayList<SkeletonPatt> outerPipeNodes = new ArrayList<SkeletonPatt>();

					PipePatt outerPipe = new PipePatt();
					PipePatt innerPipe = new PipePatt();
					innerPipeNodes.addAll(pipe.getChildren().subList(0, index).stream().map(o -> Util.clone(o)).collect(Collectors.toList()));
					innerPipeNodes.addAll(pipei.getChildren().subList(0, pipei.getChildren().size() - 1).stream().map(o -> Util.clone(o)).collect(Collectors.toList()));
					innerPipe.setChildren(innerPipeNodes);
					innerPipe.calculateIdealServiceTime();
					// eg . pipe(a, pipe(b,c), d) ----> pipe(pipe(a,b),c,d)

					outerPipeNodes.add(innerPipe);
					outerPipeNodes.add(pat);
				//	if there are elements after inner pipe
					outerPipeNodes.addAll(pipe.getChildren().subList(index + 1, pipe.getChildren().size()).stream().map(o-> Util.clone(o)).collect(Collectors.toList())); 
						
					outerPipe.setChildren(outerPipeNodes);
					outerPipe.setReWritingRule(ReWritingRules.PIPE_ASSOC);
					outerPipe.calculateIdealServiceTime();
					outerPipe.setReWriteNodes(false);
					patterns.add(outerPipe);

				}
			
		}
		// mapofpipe pipe(map(D1);map(D2))= map(pipe((D1;D2))
		// if pipe has map stages
		// remove the map as map.getchild
		// create pipe of map.getchild for all
		// creat map of the pipe
		if (pipe.getChildren().stream().allMatch(sk -> sk instanceof MapPatt)) {
			ArrayList<SkeletonPatt> mapStages = (ArrayList<SkeletonPatt>) pipe.getChildren().stream()
					.map(p -> Util.clone(p.getChildren().get(0))).collect(Collectors.toList());
			MapPatt map = new MapPatt();

			PipePatt piMap = new PipePatt();

			piMap.setChildren(mapStages);
			ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
			piMap.calculateIdealServiceTime();
			mNodes.add(piMap);
			map.setChildren(mNodes);
			map.calculateIdealServiceTime();
			map.setReWritingRule(ReWritingRules.MAP_OF_PIPE);
			map.setReWriteNodes(false);
			patterns.add(map);
		}
		// refactor stages
			for (SkeletonPatt stage : pipe.getChildren()) {
				stage.setParent(pipe);
				stage.refactor(reWriter);
				patterns.addAll(stage.getPatterns());

		}

			pipe.setPatterns(patterns);
		if (pipe.getParent() != null)
			pipe.setPatterns(Util.createTreeNode(pipe.getParent(), pipe));
		pipe.calculateIdealServiceTime();
		
		return pipe;
	}

	/**
	 * rewrites Map pattern
	 * 
	 * @param map
	 * @return Map pattern with its possible refactoring options (list of patterns)
	 */
	public static MapPatt refactor(MapPatt map) {
		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
//		mapelim map(D)!D
		SkeletonPatt p = Util.clone(map.getChildren().get(0));

		p.setReWritingRule(ReWritingRules.MAP_ELIM);
		p.calculateIdealServiceTime();
		patterns.add(p);
		
		// compofmap map(comp(D1;D2)!comp((map(D1);map(D2)) and pipeofmap
		// map(pipe(D1;D2) = pipe((map(D1);map(D2))

		if (map.getChildren().get(0) instanceof CompPatt) {
			CompPatt compPat = new CompPatt();
			CompPatt c = (CompPatt) Util.clone(map.getChildren().get(0));
			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
			for (SkeletonPatt sk : c.getChildren()) {
				MapPatt m = new MapPatt();
				ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
				mNodes.add(Util.clone(sk));
				m.setChildren(mNodes);
				m.calculateIdealServiceTime();
				nodes.add(m);
			}
			compPat.setChildren(nodes);
			compPat.calculateIdealServiceTime();
			compPat.setReWritingRule(ReWritingRules.MAP_DIST);
			patterns.add(compPat);
		} else if (map.getChildren().get(0) instanceof PipePatt) {
			PipePatt pipe = new PipePatt();
			PipePatt pi = (PipePatt) Util.clone(map.getChildren().get(0));
			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
			for (SkeletonPatt sk :pi.getChildren()) {
				MapPatt m = new MapPatt();
				ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
				mNodes.add(Util.clone(sk));
				m.setChildren(mNodes);
				m.calculateIdealServiceTime();
				nodes.add(m);
			}
			pipe.setChildren(nodes);
			pipe.calculateIdealServiceTime();
			pipe.setReWritingRule(ReWritingRules.PIPE_OF_MAP);
			patterns.add(pipe);
		}
		map.setPatterns(patterns);
		if (map.getParent() != null)
			map.setPatterns(Util.createTreeNode(map.getParent(), map));
		map.calculateIdealServiceTime();
		
		return map;
	}

}
