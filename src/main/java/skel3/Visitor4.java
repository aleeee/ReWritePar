//package skel3;
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.antlr.v4.runtime.tree.ParseTree;
//
//import model.Comp;
//import model.Farm;
//import model.MapSkel;
//import model.Pipeline;
//import model.Seq;
//import model.Skeleton;
//import pattern.skel3.Skel3BaseVisitor;
//import pattern.skel3.Skel3Parser.AssignmentContext;
//import pattern.skel3.Skel3Parser.BlockContext;
//import pattern.skel3.Skel3Parser.CompositionContext;
//import pattern.skel3.Skel3Parser.DataParallelPatternContext;
//import pattern.skel3.Skel3Parser.FarmSkelContext;
//import pattern.skel3.Skel3Parser.MainContext;
//import pattern.skel3.Skel3Parser.MainExprContext;
//import pattern.skel3.Skel3Parser.MapSkelContext;
//import pattern.skel3.Skel3Parser.PatternExprContext;
//import pattern.skel3.Skel3Parser.PipeSkelContext;
//import pattern.skel3.Skel3Parser.ProgramPartContext;
//import pattern.skel3.Skel3Parser.SequenceContext;
//import pattern.skel3.Skel3Parser.SequentialContext;
//import pattern.skel3.Skel3Parser.SkeletonProgramContext;
//import pattern.skel3.Skel3Parser.StagesContext;
//import pattern.skel3.Skel3Parser.StatementContext;
//import pattern.skel3.Skel3Parser.StreamPatternContext;
//import pattern.skel3.Skel3Parser.VarTypeContext;
//import tree.Node;
//import util.Util;
//
//public class Visitor4 extends Skel3BaseVisitor<Node>{
//	Map<String,Skeleton> variables = new HashMap<>();
//
//	@Override
//	public Node visitSkeletonProgram(SkeletonProgramContext ctx) {
//		Node n = new Node("",null);
//		ArrayList<Node> children = new ArrayList<>();
//		ctx.programPart().forEach(p -> {children.add(visit(p));});
//		n.setChildren(children);
//		return n;
//	}
//
//	@Override
//	public Node visitProgramPart(ProgramPartContext ctx) {
//		Node n = new Node("", null);
//		ArrayList<Node> children = new ArrayList<>();
//		ctx.statement().forEach(p -> {children.add(visit(p));});
//		Node main = visit(ctx.mainExpr());
//		children.add(main);
//		n.setChildren(children);
//		return n;
//	}
//
//	@Override
//	public Node visitStatement(StatementContext ctx) {
//		Node n = visit(ctx.assignment());
//		
//		return n;
//	}
//
//	@Override
//	public Node visitMainExpr(MainExprContext ctx) {
//		Node root = new Node("main",null);
//		Node child = visit(ctx.expr);
//		root.setChild(child);
//		System.out.println("root " + root.toString() );
////		System.out.println("child " + child);
////		variables.entrySet().forEach(e -> {System.out.println(e.getKey() + " s " + e.getValue());});
//		return root;
//		
//	}
//
//	@Override
//	public Node visitAssignment(AssignmentContext ctx) {
//		variables.put(ctx.varName.getText(),Util.getType1(ctx));
//
//		return super.visitAssignment(ctx);
//	}
//
//	@Override
//	public Node visitPatternExpr(PatternExprContext ctx) {
////		System.out.println(ctx.getText() + " " +(ctx.varName!= null?  " vname " +ctx.varName.getText() : ctx.getText()));
//		if(ctx.varName != null){
//			return new Node(ctx.varName.getText(),variables.get(ctx.varName.getText()));
//		}else{
//			return super.visitPatternExpr(ctx);
//		}
//	}
//
//	@Override
//	public Node visitVarType(VarTypeContext ctx) {
//		// TODO Auto-generated method stub
//		return super.visitVarType(ctx);
//	}
//
//	@Override
//	public Node visitStreamPattern(StreamPatternContext ctx) {
//		// TODO Auto-generated method stub
//		return super.visitStreamPattern(ctx);
//	}
//
//	@Override
//	public Node visitSequential(SequentialContext ctx) {
//		// TODO Auto-generated method stub
//		return super.visitSequential(ctx);
//	}
//
//	@Override
//	public Node visitDataParallelPattern(DataParallelPatternContext ctx) {
//		// TODO Auto-generated method stub
//		return super.visitDataParallelPattern(ctx);
//	}
//
//	@Override
//	public Node visitMain(MainContext ctx) {
//		// TODO Auto-generated method stub
//		return super.visitMain(ctx);
//	}
//
//	@Override
//	public Node visitBlock(BlockContext ctx) {
//		Node block = visit(ctx.expr);
//		return block;
//	}
//
//	@Override
//	public Node visitSequence(SequenceContext ctx) {
//		Node seq = new Node("seq",new Seq(Integer.parseInt(ctx.ts.getText())));
//		
//		return seq;
//	}
//
//	@Override
//	public Node visitComposition(CompositionContext ctx) {
//		Node comp = new Node("comp",new Comp(0));
//		Node child= visit(ctx.block());
//		comp.setChild(child);
//		return comp;
//	}
//
//	@Override
//	public Node visitPipeSkel(PipeSkelContext ctx) {
//		Node pipe = new Node("pipe",new Pipeline(0));
//		Node stages = visit(ctx.stages());
////		pipe.setChild(stages);
//		pipe.setChildren(stages.getChildren());
//		return pipe;
////		return super.visitPipeSkel(ctx);
//	}
//
//	@Override
//	public Node visitFarmSkel(FarmSkelContext ctx) {
//		Node farm = new Node("farm",new Farm(0));
//		Node child = visit(ctx.block());
//		farm.setChild(child);
//		return farm;
//		
//	}
//
//	@Override
//	public Node visitMapSkel(MapSkelContext ctx) {
//		Node map = new Node("map",new MapSkel(0));
//		Node child = visit(ctx.block());
//		map.setChild(child);
//		return map;
//	}
//
//	@Override
//	public Node visitStages(StagesContext ctx) {
//		Node stages = new Node("stages",null);
//		ArrayList<Node> children = new ArrayList<>();
//		ctx.expr.forEach(e -> {children.add(visit(e));});
//		stages.setChildren(children);
//		return stages;
//	}
//
//
//	
//
//}
