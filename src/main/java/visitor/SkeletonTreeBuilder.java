package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pattern.skel4.SkeletonBaseVisitor;
import pattern.skel4.SkeletonParser.AssignmentContext;
import pattern.skel4.SkeletonParser.BlockContext;
import pattern.skel4.SkeletonParser.CompositionContext;
import pattern.skel4.SkeletonParser.FarmSkelContext;
import pattern.skel4.SkeletonParser.MainContext;
import pattern.skel4.SkeletonParser.MainExprContext;
import pattern.skel4.SkeletonParser.MapSkelContext;
import pattern.skel4.SkeletonParser.PatternExprContext;
import pattern.skel4.SkeletonParser.PipeSkelContext;
import pattern.skel4.SkeletonParser.ProgramPartContext;
import pattern.skel4.SkeletonParser.SequenceContext;
import pattern.skel4.SkeletonParser.SkeletonProgramContext;
import pattern.skel4.SkeletonParser.StagesContext;
import pattern.skel4.SkeletonParser.StatementContext;
import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;
import util.Util;

public class SkeletonTreeBuilder extends SkeletonBaseVisitor<SkeletonPatt>{
	Logger log = LoggerFactory.getLogger(getClass());
	Map<String,SkeletonPatt> variables = new HashMap<>();
	private Util util = new Util();

	@Override
	public SkeletonPatt visitSkeletonProgram(SkeletonProgramContext ctx) {
		ArrayList<SkeletonPatt> children = new ArrayList<>();
		ctx.programPart().forEach(p -> {children.add(visitProgramPart(p));});
		return children.get(0);
	}

	@Override
	public SkeletonPatt visitProgramPart(ProgramPartContext ctx) {
		ArrayList<SkeletonPatt> children = new ArrayList<>();
		ctx.statement().forEach(p -> {children.add(visit(p));});
		SkeletonPatt main = visit(ctx.mainExpr());
		children.add(main);
		return main;
	}

	@Override
	public SkeletonPatt visitStatement(StatementContext ctx) {
		return super.visitStatement(ctx);
	}

	@Override
	public SkeletonPatt visitMainExpr(MainExprContext ctx) {
		SkeletonPatt  main = visit(ctx.expr);
		return main;
	}

	@Override
	public SkeletonPatt visitAssignment(AssignmentContext ctx) {
		SkeletonPatt v =util.getType(ctx);
		if(v instanceof SeqPatt) {
			if(variables.containsKey(ctx.varName.getText())){
				log.error("variable "+ ctx.varName.getText() + " already exist");
				System.exit(0);
			}
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
				log.error("Error undefined variable " + ctx.varName.getText() );
				System.exit(-1);
				return null;
			}else{
				return variables.get(ctx.varName.getText());
			}
		}else{
			return super.visitPatternExpr(ctx);
		}
	}

	

	@Override
	public SkeletonPatt visitMain(MainContext ctx) {
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
			log.error("Error Comp stages must not be less than 2");
			System.exit(1);}
		return comp;
	}

	@Override
	public SkeletonPatt visitPipeSkel(PipeSkelContext ctx) {
		PipePatt pipe = new PipePatt("pipe",0);
		pipe.setChildren(visit(ctx.stages()).getChildren());
		if(pipe.getChildren() == null || pipe.getChildren().size() < 2) {
			log.error("Error Pipe stages must not be less than 2");
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
		
		for(PatternExprContext exp: ctx.expr) {
			SkeletonPatt stage = visit(exp);
			children.add(stage);
		}
		stages.setChildren(children);
		return stages;
	}

	@Override
	public SkeletonPatt visitBlock(BlockContext ctx) {
		return visit(ctx.expr);
	}

	
}
