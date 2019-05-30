package rewriter;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.antlr.runtime.misc.IntArray;

import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

/**

 * pipeintro comp(D1;D2)! pipe(D1;D2)
pipeelim pipe(D1;D2)!comp(D1;D2)
compassoc comp(D1;comp(D2;D3))$comp(comp(D1;D2);D3)
pipeassoc pipe(D1; pipe(D2;D3))$ pipe(pipe(D1;D2);D3)
mapofcomp comp(map(D1);map(D2))!map(comp((D1;D2))
compofmap map(comp(D1;D2)!comp((map(D1);map(D2))
mapofpipe pipe(map(D1);map(D2))!map(pipe((D1;D2))
pipeofmap map(pipe(D1;D2)! pipe((map(D1);map(D2))
mapelim map(D)!D
farmelim f arm(D)!D
farmintro D! f arm(D)
/***
 * X -> <- farm(x)
map(pipe(X1,....Xk) -> <- pipe(map(X1),....map(Xk))
map(comp(X1,....Xk) -> <- comp(map(X1),....map(Xk))
map(pipe(X1,....Xk) -> <- map(comp(X1,....Xk))
pipe(map(X1),....map(Xk)) -> <- comp(map(X1),....map(Xk))
 
=======
 * pipeintro comp(D1;D2)! pipe(D1;D2) 
 * pipeelim pipe(D1;D2)!comp(D1;D2)
 *  compassoc * comp(D1;comp(D2;D3))=comp(comp(D1;D2);D3) 
 *  pipeassoc pipe(D1; pipe(D2;D3))= pipe(pipe(D1;D2);D3)
 *   mapofcomp comp(map(D1);map(D2))=map(comp((D1;D2))
 * compofmap map(comp(D1;D2)=comp((map(D1);map(D2)) 
 * mapofpipe pipe(map(D1);map(D2))=map(pipe((D1;D2))
 *  pipeofmap map(pipe(D1;D2)=  pipe((map(D1);map(D2)) 
 *  mapelim map(D)!D
 *   farmelim farm(D)!D 
 *   farmintro D! f * arm(D)
 * 
>>>>>>> branch 'master' of https://github.com/aleeee/DslSkelReWriting.git
 * @author me
 *
 */
public class SkelReWriter implements ReWriter {
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
		if (s.getChildren() != null && s.getChildren().size() > 2
				&& s.getChildren().stream().anyMatch(sk -> sk instanceof PipePatt)) {

			PipePatt p0 = (PipePatt) s.getChildren().stream().filter(e -> e instanceof PipePatt).findFirst()
					.orElse(null);
			if (p0 != null) {
				int index = s.getChildren().indexOf(p0);

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
		System.out.println("patterns " + patterns);
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
