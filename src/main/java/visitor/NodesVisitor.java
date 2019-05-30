package visitor;

import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class NodesVisitor implements NodeVisitor {
	private int n = 4;
//	@Override
//	public void visit(SkeletonPatt s) {
//		System.out.println("sk " + s);
//		
//		
//	}

	@Override
	public void visit(SeqPatt s) {
		System.out.println("v " +s);
		SkeletonPatt c = s.getChild();
		if(c != null)
			c.accept(this);
		if(s.getChildren() != null)
			s.getChildren().forEach(cl -> { if(cl != null) { cl.accept(this);}});
		
	}
	@Override
	public void visit(CompPatt s) {
		System.out.println(s);
		SkeletonPatt c = s.getChild();
		if(c != null)
			c.accept(this);
		if(s.getChildren() != null)
			s.getChildren().forEach(cl -> { if(cl != null) { cl.accept(this);}});
//		get children
//		list<Node> 
//		for each Node
//			visit Node
//		end;
//		ts = sum{ts di figli}
		
	}
	@Override
	public void visit(FarmPatt s) {
		System.out.println("farm "+s);
		SkeletonPatt c = s.getChild();
		if(c != null){
			c.accept(this);
			s.setServiceTime(c.serviceTime()/n);
		}
	}
	@Override
	public void visit(PipePatt s) {
		System.out.println("pipe "+s);
		long serviceTime = 0;
		long sum=0;
		if(s.getChildren() != null){
			s.getChildren().forEach(cl -> {
				if(cl != null) { cl.accept(this); }});
//			sum = s.getChildren().parallelStream().reduce(serviceTime,(output,sk) -> output+ sk.serviceTime(),(a,b) -> a+b);
			serviceTime = s.getChildren().parallelStream().mapToLong(SkeletonPatt::serviceTime)
					.reduce(0 , (c1,c2) -> c1 > c2 ? c1:c2);

		}
		s.setServiceTime(serviceTime);
	}
	@Override
	public void visit(MapPatt s) {
		System.out.println(s);
	}	

}
