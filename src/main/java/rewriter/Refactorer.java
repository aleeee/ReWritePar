package rewriter;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.naming.RefAddr;

import org.antlr.runtime.misc.IntArray;

import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

/**
 * 
 * @author me
 *
 */
public class Refactorer implements ReWriter {
	private int n = 4;

	@Override
	public void reWrite(SeqPatt s) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();
		// farm intro
		FarmPatt farm = new FarmPatt("farm", s.serviceTime() / n);
		farm.setChild(s);
		patterns.add(farm);
		// map intro
		MapPatt map = new MapPatt("map", s.serviceTime() / n);
		map.setChild(s);
		patterns.add(map);
		s.setPatterns(patterns);

	}

	@Override
	public void reWrite(CompPatt s) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();
		// pipe intro
		PipePatt pipe = new PipePatt("pipe",
				s.getChildren() != null
						? (s.getChildren().stream().mapToLong(SkeletonPatt::serviceTime).reduce(0,
								(c1, c2) -> c1 > c2 ? c1 : c2))
						: 0);
		pipe.setChildren(s.getChildren());
		patterns.add(pipe);

		// farm intro
		FarmPatt farm = new FarmPatt("farm", s.getServiceTime() / n);
		farm.setChild(s);
		patterns.add(farm);

		// map intro

		// for each stage
		// rewrite
//		ArrayList<SkeletonPatt> stages = new ArrayList<SkeletonPatt>();		
//		for(SkeletonPatt skel: s.getChildren()) {
//			 skel.refactor(this);
//			 stages.add(skel);
//		}
//		stages.forEach(st -> System.out.println(" comp stages" + st.getPatterns()));
	}

	@Override
	public void reWrite(FarmPatt s) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();

		// farm elim
		patterns.add(s.getChild());
		//farm intro
		FarmPatt farm = new FarmPatt("farm",s.getServiceTime()/n);
		farm.setChild(s);
		patterns.add(farm);
		// rewrite child
//		SkeletonPatt c = s.getChild();
//		 c.refactor(this);
//		c.getPatterns().forEach(p-> {farm.setChild(p); patterns.add(farm);}); 
//		System.out.println("farm stages" + c.getPatterns());
		
	}

	@Override
	public void reWrite(PipePatt s) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();

		// pipe elim
//		pipe(D1;D2) = comp(D1;D2)
		CompPatt comp = new CompPatt("comp",
				(s.getChildren() != null
						? s.getChildren().stream().mapToLong(SkeletonPatt::serviceTime).reduce(0, (c1, c2) -> c1 + c2)
						: 0));
		comp.setChildren(s.getChildren());
		patterns.add(comp);

//		pipeassoc pipe(D1; pipe(D2;D3)) = pipe(pipe(D1;D2);D3)
				if (s.getChildren() != null
						&& s.getChildren().stream().anyMatch(sk -> sk instanceof PipePatt)) {

					PipePatt p0 = (PipePatt) s.getChildren().stream().filter(e -> e instanceof PipePatt).findFirst()
							.orElse(null);
					if (p0 != null) {
						int index = s.getChildren().indexOf(p0);
						if(index == 0) {
							
							PipePatt pipe0= (PipePatt) s.getChildren().get(index);  
							SkeletonPatt pat = pipe0.getChildren().get(0);
							ArrayList<SkeletonPatt> innerPipeNodes = new ArrayList<SkeletonPatt>();
							ArrayList<SkeletonPatt> outerPipeNodes = new ArrayList<SkeletonPatt>();

							PipePatt outerPipe = new PipePatt("pipe",0);
							PipePatt innerPipe = new PipePatt("pipe",0);
							for(int i =1 ; i < pipe0.getChildren().size(); i++) { //start i at 1 because we took the first element to form associative pipe
								innerPipeNodes.add(pipe0.getChildren().get(i));
							}
							innerPipeNodes.addAll(s.getChildren().subList(1, s.getChildren().size()));
							innerPipe.setChildren(innerPipeNodes);
							outerPipeNodes.add(pat);
							outerPipeNodes.add(innerPipe);
							outerPipe.setChildren(outerPipeNodes);
							patterns.add(outerPipe);
						}else {
								PipePatt pipei= (PipePatt) s.getChildren().get(index);  
								SkeletonPatt pat = pipei.getChildren().get(pipei.getChildren().size()-1); //get the last element of the inner pipe
								ArrayList<SkeletonPatt> innerPipeNodes = new ArrayList<SkeletonPatt>();
								ArrayList<SkeletonPatt> outerPipeNodes = new ArrayList<SkeletonPatt>();

								PipePatt outerPipe = new PipePatt("pipe",0);
								PipePatt innerPipe = new PipePatt("pipe",0);
								innerPipeNodes.addAll(s.getChildren().subList(0, index));
								innerPipeNodes.addAll(pipei.getChildren().subList(0, pipei.getChildren().size()-1));
								innerPipe.setChildren(innerPipeNodes);
								// eg . pipe(a, pipe(b,c), d) ----> pipe(pipe(a,b),c,d)
								
								outerPipeNodes.add(innerPipe);
								outerPipeNodes.addAll(s.getChildren().subList(index+1, s.getChildren().size())); //if there are elements after inner pipe 
								outerPipeNodes.add(pat);

								outerPipe.setChildren(outerPipeNodes);
								patterns.add(outerPipe);
							}
					}
			// find index of pipe
			// if at 0,
			// get first child of pipe0 as patt0
			// create pipe (patt0, pipe of the remaining patterns),
			// if index > 0
			// create pipe (patt@0, .. 1st element of inner pipe), remaining patterns)
			
		}
		// farm intro
		FarmPatt farm = new FarmPatt("farm", s.getServiceTime() / n);
		farm.setChild(s);
		patterns.add(farm);

		// mapofpipe pipe(map(D1);map(D2))= map(pipe((D1;D2))
		// if pipe has map stages
		// remove the map as map.getchild
		// create pipe of map.getchild for all
		// creat map of the pipe
		if (s.getChildren().stream().allMatch(sk -> sk instanceof MapPatt)) {

			ArrayList<SkeletonPatt> listOfChildrens = (ArrayList<SkeletonPatt>) s.getChildren().stream()
					.map(p -> p.getChild()).collect(Collectors.toList());
			MapPatt map = new MapPatt("map", s.getServiceTime() / n);

			PipePatt piMap = new PipePatt(s.getLable(), listOfChildrens.stream().mapToLong(SkeletonPatt::serviceTime)
					.reduce(0, (c1, c2) -> c1 > c2 ? c1 : c2));

			piMap.setChildren(listOfChildrens);
			map.setChild(piMap);
			patterns.add(map);
		}
		// for each stage
		// rewrite
		s.setPatterns(patterns);
		ArrayList<SkeletonPatt> stages = new ArrayList<SkeletonPatt>();		
//		PipePatt pPat = new PipePatt("pipe",0);
		for(SkeletonPatt skel: s.getChildren()) {
			 skel.refactor(this);
			 System.out.println("skel patterns "+skel+" p " + skel.getPatterns());
//			 comp.setChildren(skel.getPatterns());
//			 patterns.add(comp);
		}
//		s.setPatterns(patterns);
//		stages.forEach(st -> st.getPatterns());
	}

	@Override
	public void reWrite(MapPatt s) {
		ArrayList<SkeletonPatt> patterns = new ArrayList<>();
//		mapelim map(D)!D
		patterns.add(s.getChild());
		
		// compofmap map(comp(D1;D2)!comp((map(D1);map(D2))
		CompPatt comp = new CompPatt("comp", 0);
		if (s.getChild() instanceof CompPatt) {
			CompPatt c = (CompPatt) s.getChild();
			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
			for (SkeletonPatt sk : c.getChildren()) {
				MapPatt m = new MapPatt("map", sk.serviceTime() / n);
				m.setChild(sk);
				nodes.add(m);
			}
			comp.setChildren(nodes);
			patterns.add(comp);
		}
//         pipeofmap map(pipe(D1;D2) = pipe((map(D1);map(D2))
		PipePatt pipe = new PipePatt("pipe", 0);
		if (s.getChild() instanceof PipePatt) {
			PipePatt p = (PipePatt) s.getChild();
			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
			for (SkeletonPatt sk : p.getChildren()) {
				MapPatt m = new MapPatt("map", sk.serviceTime() / n);
				m.setChild(sk);
				nodes.add(m);
			}
			pipe.setChildren(nodes);
			patterns.add(pipe);
		}
		// farm intro
		FarmPatt farm = new FarmPatt("farm",s.getServiceTime()/n);
		farm.setChild(s);
		patterns.add(farm);
		
		s.setPatterns(patterns);
	}

}
//
//refactor root;
//list<skeleton> patterns = refactor(root);
//list<skeleton> nodes = root.getChildren();
//for each skeleton@i: nodes
//	list<skeleton> patterns[i] = refactor(skeleton)
//	merge nodes;
//		for each pattern patterns[i]
//
//			nodes[0-i].merge(pattern).merge(nodes[i-n-1])   
//				eg. pipe(farm(a),b)
//					refactor(farm(a)) will give list of patterns
//					then we merge the list of the remaining child nodes of pipe with the patterns
//					i.e [farm(a).merege(b) = {farm(a),b} //// then add to patterns(or set them as child nodes of the parent skeleton patterns),
//											{a, b} /// add to patterns or we can pass the parent node,
//											{farm(farm(a),b)}].
//				then we repeat for the remaining nodes
//				refactor(b) = list<skeleton> patternsOfb
