package visitor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pattern.skel3.Skel3BaseVisitor;
import pattern.skel3.Skel3Parser.AssignmentContext;
import pattern.skel3.Skel3Parser.BlockContext;
import pattern.skel3.Skel3Parser.CompositionContext;
import pattern.skel3.Skel3Parser.DataParallelPatternContext;
import pattern.skel3.Skel3Parser.FarmSkelContext;
import pattern.skel3.Skel3Parser.MainContext;
import pattern.skel3.Skel3Parser.MainExprContext;
import pattern.skel3.Skel3Parser.MapSkelContext;
import pattern.skel3.Skel3Parser.PatternExprContext;
import pattern.skel3.Skel3Parser.PipeSkelContext;
import pattern.skel3.Skel3Parser.ProgramPartContext;
import pattern.skel3.Skel3Parser.SequenceContext;
import pattern.skel3.Skel3Parser.SequentialContext;
import pattern.skel3.Skel3Parser.SkeletonProgramContext;
import pattern.skel3.Skel3Parser.StagesContext;
import pattern.skel3.Skel3Parser.StatementContext;
import pattern.skel3.Skel3Parser.StreamPatternContext;
import pattern.skel3.Skel3Parser.VarTypeContext;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;
import util.Util;

public class Visitor6 extends Skel3BaseVisitor<SkeletonPatt>{
	Map<String,SkeletonPatt> variables = new HashMap<>();

	@Override
	public SkeletonPatt visitSkeletonProgram(SkeletonProgramContext ctx) {
		SeqPatt s = new SeqPatt(0);
		ArrayList<SkeletonPatt> children = new ArrayList<>();
		ctx.programPart().forEach(p -> {children.add(visitProgramPart(p));});
		return children.get(0);
	}

	@Override
	public SkeletonPatt visitProgramPart(ProgramPartContext ctx) {
		SeqPatt s = new SeqPatt(0);
		ArrayList<SkeletonPatt> children = new ArrayList<>();
		ctx.statement().forEach(p -> {children.add(visit(p));});
		SkeletonPatt main = visit(ctx.mainExpr());
		children.add(main);
		s.setChildren(children);
//		System.out.println("main " + main);
		return main;
	}

	@Override
	public SkeletonPatt visitStatement(StatementContext ctx) {
		return visit(ctx.assignment());
		
	}

	@Override
	public SkeletonPatt visitMainExpr(MainExprContext ctx) {
//		SeqPatt root = new SeqPatt(0);
		SkeletonPatt  main = visit(ctx.expr);
//		root.setChild(main);
//		System.out.println("root main " + main.toString() );
		return main;
		
	}

	@Override
	public SkeletonPatt visitAssignment(AssignmentContext ctx) {
		variables.put(ctx.varName.getText(),Util.getType(ctx));
		return super.visitAssignment(ctx);
	}

	@Override
	public SkeletonPatt visitPatternExpr(PatternExprContext ctx) {
		if(ctx.varName != null){			
			if(variables.get(ctx.varName.getText()) == null){
				variables.entrySet().forEach(e -> {System.out.println(e.getKey() + " " +e.getValue());});
				System.out.println("Error undefined variable " + ctx.varName.getText() );
				System.exit(-1);
				return null;
			}else{
				SeqPatt s= (SeqPatt) variables.get(ctx.varName.getText());
//				s.setLable(ctx.varName.getText());
				return s;
			}
		}else{
			return super.visitPatternExpr(ctx);
		}
	}

	@Override
	public SkeletonPatt visitVarType(VarTypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitVarType(ctx);
	}

	@Override
	public SkeletonPatt visitStreamPattern(StreamPatternContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStreamPattern(ctx);
	}

	@Override
	public SkeletonPatt visitSequential(SequentialContext ctx) {
		// TODO Auto-generated method stub
		return super.visitSequential(ctx);
	}

	@Override
	public SkeletonPatt visitDataParallelPattern(DataParallelPatternContext ctx) {
		// TODO Auto-generated method stub
		return super.visitDataParallelPattern(ctx);
	}

	@Override
	public SkeletonPatt visitMain(MainContext ctx) {
		// TODO Auto-generated method stub
		return super.visitMain(ctx);
	}

	@Override
	public SkeletonPatt visitBlock(BlockContext ctx) {
		SkeletonPatt block = visit(ctx.expr);
		return block;
		
	}

	@Override
	public SkeletonPatt visitSequence(SequenceContext ctx) {
		return new SeqPatt(Integer.parseInt(ctx.ts.getText()));

	}

	@Override
	public SkeletonPatt visitComposition(CompositionContext ctx) {
		CompPatt comp = new CompPatt("comp",0);
		comp.setChild(visit(ctx.block()));
		return comp;
	}

	@Override
	public SkeletonPatt visitPipeSkel(PipeSkelContext ctx) {
		PipePatt pipe = new PipePatt("pipe",0);
		pipe.setChildren(visit(ctx.stages()).getChildren());
//		pipe.setChild(stages);
//		pipe.setChildren(stages.getChildren());
		return pipe;
	}

	@Override
	public SkeletonPatt visitFarmSkel(FarmSkelContext ctx) {
		FarmPatt farm = new FarmPatt("farm",0);
		farm.setChild(visit(ctx.block()));
		return farm;
	}

	@Override
	public SkeletonPatt visitMapSkel(MapSkelContext ctx) {
		MapPatt map = new MapPatt("map",0);
		map.setChild(visit(ctx.block()));
		return map;
	}

	@Override
	public SkeletonPatt visitStages(StagesContext ctx) {
		SeqPatt stages = new SeqPatt(0);
		ArrayList<SkeletonPatt> children = new ArrayList<>();
		ctx.expr.forEach(e -> {children.add(visit(e));});
		stages.setChildren(children);
		return stages;
	}

	

}
