//package rewriter;
//
//import java.util.ArrayList;
//import java.util.LinkedHashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import tree.model.CompPatt;
//import tree.model.FarmPatt;
//import tree.model.MapPatt;
//import tree.model.PipePatt;
//import tree.model.SeqPatt;
//import tree.model.SkeletonPatt;
//import util.ReWritingRules;
//import util.Util;
//
//public class Refactor {
//	private static RW reWriter = new RW();
//
//	/**
//	 * refactor Seqential pattern
//	 * 
//	 * @param seq
//	 * @return Seq with its possible refactoring options (list of patterns)
//	 */
//	public static SeqPatt refactor(SeqPatt seq) {
//		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
//		ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
//		SkeletonPatt s = Util.clone(seq);
//		fc.add(s);
//		// farm intro
//		FarmPatt farm = new FarmPatt();
//		farm.setChildren(fc);
//		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
//		farm.calculateIdealServiceTime();
//		patterns.add(farm);
//		seq.setPatterns(patterns);
//		if (seq.getParent() != null)
//			seq.setPatterns(Util.createTreeNode(seq.getParent(), seq));
//		return seq;
//	}
//
//	/**
//	 * reWrite Comp pattern
//	 * 
//	 * @param comp
//	 * @return CompPatt with its possible refactoring options (list of patterns)
//	 */
//	public static CompPatt refactor(CompPatt comp) {
//		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
//		
//
//		// pipe intro
//		PipePatt pipe = new PipePatt();
//		pipe.setChildren(comp.getChildren());
//		pipe.setReWritingRule(ReWritingRules.PIPE_INTRO);
//		pipe.setDepth(comp.getDepth());
//		pipe.calculateIdealServiceTime();
//		pipe.setReWriteNodes(false);
//		patterns.add(pipe);
//
//		// farm intro
//		if(!Util.detectLoop(comp, FarmPatt.class)) {
//		FarmPatt farm = new FarmPatt();
//		CompPatt compStage = new CompPatt();
//		ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
//		compStage.setChildren(comp.getChildren());
//		compStage.setPatterns(comp.getPatterns());
////		compStage.setDepth(comp.getDepth()+1);
//		compStage.setRule(comp.getRule());
//		compStage.calculateIdealServiceTime();
//		fc.add(compStage);
//		farm.setChildren(fc);
////		farm.setDepth(comp.getDepth());
//		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
//		farm.calculateIdealServiceTime();
////		farm.setReWriteNodes(false);
//		patterns.add(farm);
//		}
//		// map intro
//
//		// for each stage
//		// rewrite
//		for (SkeletonPatt skel : comp.getChildren()) {
////			if (skel.reWriteNodes()) {
////				if (skel.getParent() == null)
//				skel.setParent(comp);
//				skel.refactor(reWriter);
////				for (SkeletonPatt p : skel.getPatterns()) {
////					System.out.println("creating tree "+ p +" with " + p.getPatterns());
////					patterns.addAll(Util.createTreeNode(comp, skel));
//				patterns.addAll(skel.getPatterns());
////				}
////			}
//
//		}
//		// creat map of the pipe
//		if (comp.getChildren().stream().allMatch(sk -> sk instanceof MapPatt)) {
//			ArrayList<SkeletonPatt> listOfChildrens = (ArrayList<SkeletonPatt>) comp.getChildren().stream()
//					.map(p -> p.getChildren().get(0)).collect(Collectors.toList());
//			MapPatt map = new MapPatt();
//
//			CompPatt compMap = new CompPatt();
//
//			compMap.setChildren(listOfChildrens);
//			compMap.setDepth(comp.getDepth() + 1);
//			ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
//			compMap.calculateIdealServiceTime();
//			mNodes.add(compMap);
//			map.setChildren(mNodes);
////			map.setDepth(comp.getDepth());
//			map.calculateIdealServiceTime();
//			map.setReWritingRule(ReWritingRules.MAP_OF_COMP);
////			map.setReWriteNodes(false);
//			patterns.add(map);
//		}
//
//		comp.setPatterns(patterns);
//		if (comp.getParent() != null)
//			comp.setPatterns(Util.createTreeNode(comp.getParent(), comp));
//		comp.calculateIdealServiceTime();
//		return comp;
//	}
//
//	/**
//	 * reWrites Farm pattern
//	 * 
//	 * @param farm
//	 * @return Farm pattern with its possible refactoring options (list of patterns)
//	 */
//	public static FarmPatt refactor(FarmPatt farm) {
//		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
//
//		// farm elim
//		SkeletonPatt c = Util.clone(farm.getChildren().get(0));
//		
//		c.setDepth(farm.getDepth());
//		c.setReWritingRule(ReWritingRules.FARM_ELIM);
//		c.calculateIdealServiceTime();
//		patterns.add(c);
//		// farm intro
//		
////		FarmPatt farmPat = new FarmPatt();
////		ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
////		FarmPatt fStage = (FarmPatt) Util.clone(farm);
////		fStage.setDepth(farm.getDepth()+1);
////		fc.add(fStage);
////		farmPat.setChildren(fc);
////		farmPat.setReWritingRule(ReWritingRules.FARM_INTRO);
////		farmPat.setDepth(farm.getDepth());
////		farmPat.calculateIdealServiceTime();
////		farmPat.setReWriteNodes(false);
////		patterns.add(farmPat);
//
//		if (farm.reWriteNodes()) {
//
//			SkeletonPatt skel = farm.getChildren().get(0);
////		skel.setReWriteNodes(false);
////			if (skel.getParent() == null)
//				skel.setParent(farm);
//			skel.refactor(reWriter);
//			for (SkeletonPatt p : skel.getPatterns()) {
//				FarmPatt fp = new FarmPatt();
//				ArrayList<SkeletonPatt> fc1 = new ArrayList<SkeletonPatt>();
//				fc1.add(p);
//				fp.setChildren(fc1);
//				fp.setReWritingRule(ReWritingRules.FARM_INTRO);
//				fp.setDepth(farm.getDepth() + 1);
//				fp.calculateIdealServiceTime();
//				fp.setReWriteNodes(false);
//				patterns.add(fp);
//			}
//		}
//		farm.setPatterns(patterns);
//		if (farm.getParent() != null)
//			farm.setPatterns(Util.createTreeNode(farm.getParent(), farm));
//		farm.calculateIdealServiceTime();
//		return farm;
//	}
//
//	/**
//	 * rewrites Pipe pattern
//	 * 
//	 * @param pipe
//	 * @return Pipeline pattern with its possible refactoring options (list of
//	 *         patterns)
//	 */
//	public static PipePatt refactor(PipePatt pipe) {
//		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
//
//		// farm intro
//		if(!Util.detectLoop(pipe, FarmPatt.class)) {
//			
//			FarmPatt farm = new FarmPatt();
//			ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
//			PipePatt fStage = (PipePatt) Util.clone(pipe);
//			fStage.setDepth(pipe.getDepth()+1);
//			fc.add(fStage);
//			farm.setChildren(fc);
//			farm.setReWritingRule(ReWritingRules.FARM_INTRO);
//			farm.setDepth(pipe.getDepth());
//			farm.calculateIdealServiceTime();
//			farm.setReWriteNodes(false);
//			patterns.add(farm);
//		}
//		// pipe elim
//		CompPatt comp = new CompPatt();
//		comp.setChildren(pipe.getChildren());
//		comp.setReWritingRule(ReWritingRules.PIPE_ELIM);
//		comp.calculateIdealServiceTime();
//		comp.setReWriteNodes(false);
//		comp.setDepth(pipe.getDepth());
//		patterns.add(comp);
//
//		// pipeassoc pipe(D1; pipe(D2;D3)) = pipe(pipe(D1;D2);D3)
//		if (pipe.getChildren() != null && pipe.getChildren().stream().anyMatch(sk -> sk instanceof PipePatt)) {
//
//			PipePatt p0 = (PipePatt) pipe.getChildren().stream().filter(e -> e instanceof PipePatt).findFirst()
//					.orElse(null);
//			if (p0 != null) {
//				int index = pipe.getChildren().indexOf(p0);
//				if (index == 0) {
//
//					PipePatt pipe0 = (PipePatt) pipe.getChildren().get(index);
//					SkeletonPatt pat = pipe0.getChildren().get(0);
//					ArrayList<SkeletonPatt> innerPipeNodes = new ArrayList<SkeletonPatt>();
//					ArrayList<SkeletonPatt> outerPipeNodes = new ArrayList<SkeletonPatt>();
//
//					PipePatt outerPipe = new PipePatt();
//					PipePatt innerPipe = new PipePatt();
//					for (int i = 1; i < pipe0.getChildren().size(); i++) { // start i at 1 because we took the first
//																			// element to form associative pipe
//						innerPipeNodes.add(pipe0.getChildren().get(i));
//					}
//					innerPipeNodes.addAll(pipe.getChildren().subList(1, pipe.getChildren().size()));
//					innerPipe.setChildren(innerPipeNodes);
//					innerPipe.calculateIdealServiceTime();
//					innerPipe.setDepth(pipe.getDepth()+1);
//					outerPipeNodes.add(pat);
//					outerPipeNodes.add(innerPipe);
//					outerPipe.setChildren(outerPipeNodes);
//					outerPipe.setReWritingRule(ReWritingRules.PIPE_ASSOC);
//					outerPipe.calculateIdealServiceTime();
//					outerPipe.setDepth(pipe.getDepth());
//					outerPipe.setReWriteNodes(false);
//					patterns.add(outerPipe);
//				} else {
//					PipePatt pipei = (PipePatt) pipe.getChildren().get(index);
//					SkeletonPatt pat = pipei.getChildren().get(pipei.getChildren().size() - 1); // get the last element
//																								// of the inner pipe
//					ArrayList<SkeletonPatt> innerPipeNodes = new ArrayList<SkeletonPatt>();
//					ArrayList<SkeletonPatt> outerPipeNodes = new ArrayList<SkeletonPatt>();
//
//					PipePatt outerPipe = new PipePatt();
//					PipePatt innerPipe = new PipePatt();
//					innerPipeNodes.addAll(pipe.getChildren().subList(0, index));
//					innerPipeNodes.addAll(pipei.getChildren().subList(0, pipei.getChildren().size() - 1));
//					innerPipe.setChildren(innerPipeNodes);
//					innerPipe.setDepth(pipe.getDepth()+1);
//					innerPipe.calculateIdealServiceTime();
//					// eg . pipe(a, pipe(b,c), d) ----> pipe(pipe(a,b),c,d)
//
//					outerPipeNodes.add(innerPipe);
//					outerPipeNodes.add(pat);
//					outerPipeNodes.addAll(pipe.getChildren().subList(index + 1, pipe.getChildren().size())); // if there
//																												// are
//																												// elements
//																												// after
//																												// inner
//																												// pipe
//					
//
//					outerPipe.setChildren(outerPipeNodes);
//					outerPipe.setReWritingRule(ReWritingRules.PIPE_ASSOC);
//					outerPipe.calculateIdealServiceTime();
//					outerPipe.setDepth(pipe.getDepth());
//					outerPipe.setReWriteNodes(false);
//					patterns.add(outerPipe);
//
//				}
//			}
//		}
//		// mapofpipe pipe(map(D1);map(D2))= map(pipe((D1;D2))
//		// if pipe has map stages
//		// remove the map as map.getchild
//		// create pipe of map.getchild for all
//		// creat map of the pipe
//		if (pipe.getChildren().stream().allMatch(sk -> sk instanceof MapPatt)) {
//			ArrayList<SkeletonPatt> listOfChildrens = (ArrayList<SkeletonPatt>) pipe.getChildren().stream()
//					.map(p -> p.getChildren().get(0)).collect(Collectors.toList());
//			MapPatt map = new MapPatt();
//
//			PipePatt piMap = new PipePatt();
//
//			piMap.setChildren(listOfChildrens);
//			piMap.setDepth(pipe.getDepth() + 1);
//			ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
//			piMap.calculateIdealServiceTime();
//			mNodes.add(piMap);
//			map.setChildren(mNodes);
//			map.setDepth(pipe.getDepth());
//			map.calculateIdealServiceTime();
//			map.setReWritingRule(ReWritingRules.MAP_OF_PIPE);
//			map.setReWriteNodes(false);
//			patterns.add(map);
//		}
//		// refactor stages
////		if (pipe.reWriteNodes()) {
////		for (SkeletonPatt skel : pipe.getChildren()) {
////			if (!DiGraphGen3.g.containsVertex(skel)) {
////				System.out.println(skel.getDepth() + " " + skel);
////				DiGraphGen3.g.addVertex(skel);
////				skel.refactor(reWriter);
////			}
//
////			List<List<SkeletonPatt>> stages = Util.getStagesPatterns(pipe);
////			for (List<SkeletonPatt> stage : stages) {
////				CompPatt compPat = new CompPatt("comp", 0);
////				compPat.setChildren((ArrayList<SkeletonPatt>) stage);
////				compPat.setReWritingRule(ReWritingRules.PIPE_ELIM);
////				compPat.setDepth(pipe.getDepth());
////				patterns.add(compPat);
////
////				PipePatt pipePat = new PipePatt("pipe", 0);
////				pipePat.setChildren((ArrayList<SkeletonPatt>) stage);
////				pipePat.setReWritingRule(ReWritingRules.PIPE_INTRO);
////				pipePat.setDepth(pipe.getDepth());
////				patterns.add(pipePat);
////				 fc = new  ArrayList<SkeletonPatt>();
////
////				FarmPatt farmPat = new FarmPatt("farm", 0);
////				fc.add(pipePat);
////				farmPat.setChildren(fc);
////				farmPat.setReWritingRule(ReWritingRules.FARM_INTRO);
////				farmPat.setDepth(pipe.getDepth());
////				patterns.add(farmPat);
////			}
////		}
//		pipe.setPatterns(patterns);
//		if (pipe.getParent() != null)
//			pipe.setPatterns(Util.createTreeNode(pipe.getParent(), pipe));
//		pipe.calculateIdealServiceTime();
//		return pipe;
//	}
//
//	/**
//	 * rewrites Map pattern
//	 * 
//	 * @param map
//	 * @return Map pattern with its possible refactoring options (list of patterns)
//	 */
//	public static MapPatt refactor(MapPatt map) {
//		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
////		mapelim map(D)!D
//		SkeletonPatt p = Util.clone(map.getChildren().get(0));
////		SkeletonPatt p = map.getChildren().get(0);
//
//		ArrayList<SkeletonPatt> sc = new ArrayList<SkeletonPatt>();
//
//		
//		if (p.getChildren() != null)
//			sc.addAll(p.getChildren());
////		newP.setParent(parent);
//		p.setDepth(p.getDepth());
//		p.setChildren(sc);
//		p.setReWritingRule(ReWritingRules.MAP_ELIM);
//		p.calculateIdealServiceTime();
//		p.setReWriteNodes(false);
//		patterns.add(p);
//		// compofmap map(comp(D1;D2)!comp((map(D1);map(D2)) and pipeofmap
//		// map(pipe(D1;D2) = pipe((map(D1);map(D2))
//
//		if (map.getChildren().get(0) instanceof CompPatt) {
//			CompPatt compPat = new CompPatt();
//			CompPatt c = (CompPatt) Util.clone(map.getChildren().get(0));
//			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
//			for (SkeletonPatt sk : c.getChildren()) {
//				MapPatt m = new MapPatt();
//				ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
//				mNodes.add(sk);
//				m.setChildren(mNodes);
//				m.calculateIdealServiceTime();
//				nodes.add(m);
//			}
//			compPat.setChildren(nodes);
//			compPat.calculateIdealServiceTime();
//			compPat.setReWritingRule(ReWritingRules.MAP_DIST);
//			compPat.setReWriteNodes(false);
//			patterns.add(compPat);
//		} else if (map.getChildren().get(0) instanceof PipePatt) {
//			PipePatt pipe = new PipePatt();
//			PipePatt pi = (PipePatt) Util.clone(map.getChildren().get(0));
//			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
//			for (SkeletonPatt sk :pi.getChildren()) {
//				MapPatt m = new MapPatt();
//				ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
//				mNodes.add(sk);
//				m.setChildren(mNodes);
//				m.calculateIdealServiceTime();
//				nodes.add(m);
//			}
//			pipe.setChildren(nodes);
//			pipe.calculateIdealServiceTime();
//			pipe.setReWritingRule(ReWritingRules.PIPE_OF_MAP);
//			pipe.setReWriteNodes(false);
//			patterns.add(pipe);
//		}
//		// farm intro
//		ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
//
//		FarmPatt farm = new FarmPatt();
//		fc.add(map);
//		farm.setChildren(fc);
//		farm.setReWriteNodes(false);
//		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
//		farm.setDepth(map.getDepth() + 1);
//		farm.calculateIdealServiceTime();
//		patterns.add(farm);
//
//		map.setPatterns(patterns);
//
//		
//		if (map.getParent() != null)
//			map.setPatterns(Util.createTreeNode(map.getParent(), map));
//		map.calculateIdealServiceTime();
//		return map;
//	}
//
//}
