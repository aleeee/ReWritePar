package util;


import pattern.skel3.Skel3Parser.AssignmentContext;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class Util {

	public static SkeletonPatt getType(AssignmentContext ctx) {
		
		switch(ctx.varType().getText()) {
		case "Seq":
//			return new Seq(ctx.expr.se);
			SeqPatt s= new SeqPatt(Long.parseLong(ctx.expr.seq.sec.ts.getText()));
			s.setLable(ctx.varName.getText());
			return s;
			
		case "Comp":
			return new CompPatt("comp",0);
		case "Farm":
			return new FarmPatt("farm",0);
		case "Pipe":
			return new PipePatt("pipe",0);
		case "Map":
			return new MapPatt("map",0);
		}
		return null;
	}

	
//	public static long computeServiceTime(Node n,long ts) {
//		System.out.println(n + " sk " + ts);
//		if(n.getChild() != null) {
//			ts = ts+ computeServiceTime(n.getChild(),ts);
//		}else if(n.getChildren() != null) {
//			
//			for(Node c : n.getChildren()) {
//				ts = computeServiceTime(c, ts);
//			}
//		}else {
//			ts= ts+n.getSkeleton().serviceTime();
//		}
//		return ts;
//	}

}
