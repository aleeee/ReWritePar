package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import pattern.skel4.Skel4BaseVisitor;
import pattern.skel4.Skel4Parser.AssignmentContext;
import pattern.skel4.Skel4Parser.BlockContext;
import pattern.skel4.Skel4Parser.CompositionContext;
import pattern.skel4.Skel4Parser.FarmSkelContext;
import pattern.skel4.Skel4Parser.MainContext;
import pattern.skel4.Skel4Parser.MainExprContext;
import pattern.skel4.Skel4Parser.MapSkelContext;
import pattern.skel4.Skel4Parser.PatternExprContext;
import pattern.skel4.Skel4Parser.PipeSkelContext;
import pattern.skel4.Skel4Parser.ProgramPartContext;
import pattern.skel4.Skel4Parser.SequenceContext;
import pattern.skel4.Skel4Parser.SkeletonProgramContext;
import pattern.skel4.Skel4Parser.StagesContext;
import pattern.skel4.Skel4Parser.StatementContext;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;
import util.Util;

public class TBuilder2 extends Skel4BaseVisitor<SkeletonPatt>{
	Map<String,SkeletonPatt> variables = new HashMap<>();

	@Override
	public SkeletonPatt visitSkeletonProgram(SkeletonProgramContext ctx) {
		ArrayList<SkeletonPatt> children = new ArrayList<>();
		ctx.programPart().forEach(p -> {children.add(visitProgramPart(p));});
		return children.get(0);
	}

	@Override
	public SkeletonPatt visitProgramPart(ProgramPartContext ctx) {
//		SeqPatt s = new SeqPatt();
		ArrayList<SkeletonPatt> children = new ArrayList<>();
		ctx.statement().forEach(p -> {children.add(visit(p));});
		SkeletonPatt main = visit(ctx.mainExpr());
		children.add(main);
//		s.setChildren(children);
//		System.out.println("main " + main);
		return main;
	}

	@Override
	public SkeletonPatt visitStatement(StatementContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement(ctx);
	}

	@Override
	public SkeletonPatt visitMainExpr(MainExprContext ctx) {
		SkeletonPatt  main = visit(ctx.expr);
		return main;
	}

	@Override
	public SkeletonPatt visitAssignment(AssignmentContext ctx) {
		SkeletonPatt v =Util.getType(ctx);
		if(v instanceof SeqPatt) {
			variables.put(ctx.varName.getText(), v);
		}else {
			variables.put(ctx.varName.getText(), super.visitAssignment(ctx));
		}
		
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
//				SeqPatt s= (SeqPatt) variables.get(ctx.varName.getText());
//				s.setLable(ctx.varName.getText());
				return variables.get(ctx.varName.getText());
//				return s;
			}
		}else{
			return super.visitPatternExpr(ctx);
		}
	}

	

	@Override
	public SkeletonPatt visitMain(MainContext ctx) {
		// TODO Auto-generated method stub
		return super.visitMain(ctx);
	}

	

	@Override
	public SkeletonPatt visitSequence(SequenceContext ctx) {
		return new SeqPatt(Integer.parseInt(ctx.ts.getText()));

	}

	@Override
	public SkeletonPatt visitComposition(CompositionContext ctx) {
		CompPatt comp = new CompPatt("comp",0);
		comp.setChildren(visit(ctx.stages()).getChildren());
		if(comp.getChildren() == null || comp.getChildren().size() < 2) {
			System.out.println("Error Comp stages must not be less than 2");
			System.exit(1);}
		return comp;
	}

	@Override
	public SkeletonPatt visitPipeSkel(PipeSkelContext ctx) {
		PipePatt pipe = new PipePatt("pipe",0);
		pipe.setChildren(visit(ctx.stages()).getChildren());
		if(pipe.getChildren() == null || pipe.getChildren().size() < 2) {
			System.out.println("Error Pipe stages must not be less than 2");
			System.exit(1);}
		return pipe;
	}

	@Override
	public SkeletonPatt visitFarmSkel(FarmSkelContext ctx) {
		FarmPatt farm = new FarmPatt("farm",0);
		ArrayList<SkeletonPatt> s = new ArrayList<SkeletonPatt>();
		SkeletonPatt p =visit(ctx.block()); 
		s.add(p);
		farm.setChildren(s);
		farm.setChild(p);
		return farm;
	}

	@Override
	public SkeletonPatt visitMapSkel(MapSkelContext ctx) {
		MapPatt map = new MapPatt("map",0);
		ArrayList<SkeletonPatt> s = new ArrayList<SkeletonPatt>();
		SkeletonPatt p =visit(ctx.block()); 
		s.add(p);
		map.setChildren(s);
		return map;
	}

	@Override
	public SkeletonPatt visitStages(StagesContext ctx) {
		SeqPatt stages = new SeqPatt(0);
		ArrayList<SkeletonPatt> children = new ArrayList<>();
//		ctx.expr.forEach(e -> {children.add(visit(e));});
		
		for(PatternExprContext exp: ctx.expr) {
			SkeletonPatt stage = visit(exp);
			children.add(stage);
		}
		stages.setChildren(children);
		return stages;
	}

	@Override
	public SkeletonPatt visitBlock(BlockContext ctx) {
		// TODO Auto-generated method stub
		return visit(ctx.expr);
	}

	
}
