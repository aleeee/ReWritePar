package starter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import graph.SimulatedAnnealing;
import graph.SimulatedAnnealing__;
import tree.model.SkeletonPatt;
import tree.model.Solution;

public class SimRunner_ implements Callable<List<Solution>> {
	
	int size, iter, res;
	SkeletonPatt pattern;
	public SimRunner_(int size, int iter, int res, SkeletonPatt p) {
		this.size=size;
		this.iter=iter;
		this.res=res;
		this.pattern=p;
	}
	@Override
	public List<Solution> call()  {
		List<Solution> solution = new ArrayList<Solution>();
		
		IntStream.range(0, size).forEach( s -> {
			SimulatedAnnealing t = new SimulatedAnnealing(pattern, iter, res);
			 Instant st = Instant.now();
			solution.add(t.expandAndSearch());
//			System.gc();
			 Instant et = Instant.now();
			 Duration d = Duration.between( st , et ) ;
//			 System.out.println("duration " + d.toMillis());
		});
//		for(int k=0; k< size; k++) {
////			solution.add(t.search());
//		}
		return solution;
	}


}
