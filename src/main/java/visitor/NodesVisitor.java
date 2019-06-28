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
//		System.out.println("v " +s);		
		
	}
	@Override
	public void visit(CompPatt s) {
//		System.out.println("comp");
		SkeletonPatt c = s.getChild();
		double sum=0;
		double ts=0;
		if(c != null)
			c.accept(this);
		if(s.getChildren() != null) {
			s.getChildren().forEach(cl -> { if(cl != null) { cl.accept(this);}});
			sum = s.getChildren().parallelStream().reduce(ts,(output,sk) -> output+ sk.getServiceTime(),(a,b) -> a+b);
		}
		s.setServiceTime(sum);
//		get children
//		list<Node> 
//		for each Node
//			visit Node
//		end;
//		ts = sum{ts di figli}
		
	}
	@Override
	public void visit(FarmPatt s) {
//		System.out.println("farm "+s);
		SkeletonPatt c = s.getChild();
		if(c != null){
			c.accept(this);
			s.setServiceTime(c.getServiceTime()/n);
		}
	}
	@Override
	public void visit(PipePatt s) {
//		System.out.println("pipe "+s);
		double serviceTime = 0;
		double sum=0;
		if(s.getChildren() != null){
			s.getChildren().forEach(cl -> {
				if(cl != null) { cl.accept(this); }});
//			sum = s.getChildren().parallelStream().reduce(serviceTime,(output,sk) -> output+ sk.serviceTime(),(a,b) -> a+b);
			serviceTime = s.getChildren().parallelStream().mapToDouble(SkeletonPatt::getServiceTime)
					.reduce(0 , (c1,c2) -> c1 > c2 ? c1:c2);

		}
		s.setServiceTime(serviceTime);
	}
	@Override
	public void visit(MapPatt s) {
		SkeletonPatt c = s.getChild();
		if(c != null){
			c.accept(this);
			s.setServiceTime(c.getServiceTime()/n);
		}
	}	

}
