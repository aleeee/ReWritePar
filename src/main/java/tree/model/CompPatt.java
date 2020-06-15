package tree.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.io.AttributeType;

import cpo.SolverModel;
import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cp.IloCP;
import rewriter.ReWriter;
import util.ReWritingRules;
import util.Util;
public class CompPatt implements SkeletonPatt {
	
	private static final long serialVersionUID = 1L;
	ArrayList<SkeletonPatt> children;
	SkeletonPatt parent;
	String lable;
	SkeletonPatt child;
	Set<SkeletonPatt> patterns;
	boolean reWriteNodes;
	ReWritingRules rule;
	int depth;
	int id;
	int idealParDegree;
	double idealServiceTime;
	int optParDegree;
	double optServiceTime;
	private int numResource;
	private Util util;
	public CompPatt() {
		this.lable = "comp";
		this.util = new Util();
	}
	public CompPatt(String lable, int serviceTime) {
		super();
		this.lable = lable;
		this.idealServiceTime = serviceTime;
		this.idealParDegree=1;
		this.util=new Util();
	}


	@Override
	public void refactor(ReWriter reWriter) {
		reWriter.reWrite(this);
	}
	
	

	@Override
	public ReWritingRules getRule() {
		return rule;
	}

	@Override
	public void setReWritingRule(ReWritingRules rule) {
		this.rule = rule;
		
	}
	
	@Override
	public void calculateIdealServiceTime() {
		this.idealServiceTime=util.getServiceTime(this);
		
	}	

	@Override
	public ArrayList<SkeletonPatt> getChildren() {
		return children;
	}

	@Override
	public String getLable() {
		return lable;
	}


	public SkeletonPatt getParent() {
		return parent;
	}

	public void setParent(SkeletonPatt parent) {
		this.parent = parent;
	}

	public void setChildren(ArrayList<SkeletonPatt> children) {
		this.children = children;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public void setChild(SkeletonPatt child) {
		this.child = child;
	}

	public Set<SkeletonPatt> getPatterns() {
		return patterns;
	}

	public void setPatterns(Set<SkeletonPatt> patterns) {
		this.patterns = patterns;
	}
	

	public boolean reWriteNodes() {
		return reWriteNodes;
	}

	public void setReWriteNodes(boolean reWriteNodes) {
		this.reWriteNodes = reWriteNodes;
	}
	
	@Override
	public String toString() {
//		return " C ( "+( this.getChildren() != null? this.getChildren().toString().replace("[", "").replace("]", "")  + " ) ":null );

		return getLable() +" ( "+( this.getChildren() != null? this.getChildren().toString().replace("[", "").replace("]", "")
				+ " ) ":null );
		//+  "ts::+  ["+getOptimizedTs()+"]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((lable == null) ? 0 : lable.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompPatt other = (CompPatt) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (lable == null) {
			if (other.lable != null)
				return false;
		} else if (!lable.equals(other.lable))
			return false;
		return true;
	}
	@Override
	public void setDepth(int depth) {
		this.depth=depth;
	}
	@Override
	public int getDepth() {
		return depth;
	}
	
//	@Override
//	public String getValue() {
//		return null;
//	}
//	@Override
//	public AttributeType getType() {
//		return AttributeType.STRING;
//	}
	
	
	@Override
	public void setIdealServiceTime(double ts) {
		this.idealServiceTime = ts;		
	}

	@Override
	public void setIdealParDegree(int n) {
		this.idealParDegree = n;
	}

	@Override
	public double getIdealServiceTime() {
		idealServiceTime=util.getServiceTime(this);		
		return idealServiceTime;
	}

	@Override
	public int getIdealParDegree() {
		return idealParDegree;
	}
	@Override
	public int getOptParallelismDegree() {
		return optParDegree;
	}
	@Override
	public void setOptParallelismDegree(int p) {
		this.optParDegree=p;
	}
	@Override
	public double calculateOptimalServiceTime() {
		return this.optServiceTime = util.getOptimalServiceTime(this);
		
	}
	public int getOptParDegree() {
		return optParDegree;
	}
	public void setOptParDegree(int optParDegree) {
		this.optParDegree = optParDegree;
	}
	
	
	public void setRule(ReWritingRules rule) {
		this.rule = rule;
	}
	@Override
	public double getOptServiceTime() {
		return this.optServiceTime = util.getOptimalServiceTime(this);
		
	}
	@Override
	public void setOptServiceTime(double ts) {
	   this.optServiceTime = util.getOptimalServiceTime(this);
		
		
	}
	@Override
	public int getNumberOfResources() {
		 return this.numResource=this.children.stream().mapToInt(c -> c.getNumberOfResources()).max().getAsInt();

	}
	@Override
	public void setNumberOfResources(int r) {
		this.numResource=r;
	}
	@Override
	public void addConstraint(SolverModel model) throws IloException {
		IloCP cplex = model.getCplex();
		Map<SkeletonPatt, List<IloNumVar>> variables = model.getVariables();
		
		List<IloNumVar> vars = variables.get(this);	
//		List<IloIntVar> stageVars = new ArrayList<>();	
		List<IloNumVar> stageVars = new ArrayList<>();	
		IloIntVar n_i = (IloIntVar) vars.get(1);
		cplex.addLe(n_i, model.getNumAvailableProcessors());	
		IloIntExpr res= model.getResourcesVars().get(this);
		cplex.addEq(res, n_i);
		Collection<IloIntExpr> sv=new ArrayList<IloIntExpr>();
		for (SkeletonPatt v : children) {
//			if(v instanceof FarmPatt || v instanceof MapPatt) {
//				IloIntExpr nw =    cplex.sum((IloIntVar)variables.get(v).get(1), 2);
////				stageVars.addAll( (Collection<? extends IloNumVar>) nw);
//				sv.add(nw);
//			}
//			else {
				sv.add((IloIntExpr) model.getResourcesVars().get(v));
//			}
		}
		cplex.addEq(n_i, cplex.max(sv.stream().collect(Collectors.toList())));
		cplex.addLe(res, model.getNumAvailableProcessors());
		for (SkeletonPatt stage : children) {
			model.setCplex(cplex);
			stage.addConstraint(model);
		}
		
		}

	@Override
	public SolverModel addObjective(SolverModel model) throws IloException {
		IloNumExpr obj = model.getObj();
		IloNumExpr pd_obj = model.getPd_obj();
		IloCP cplex = model.getCplex();
		Map<SkeletonPatt, List<IloNumVar>> variables = model.getVariables();
		List<IloNumVar> vars = variables.get(this);
		IloIntVar pd = (IloIntVar) vars.get(1);
		
		obj =  cplex.sum(obj, vars.get(0));
		pd_obj = cplex.sum(pd_obj,pd);
		model.setCplex(cplex);
		model.setObj(obj);
		model.setPd_obj(pd_obj);
		if(children != null) {
		for (SkeletonPatt c : children) {
			model = c.addObjective(model);
		}
		
		return model;
	}
		return model;
	}
	@Override
	public int getId() {
		return this.id;
	}
	@Override
	public void setId(int id) {
		this.id=id;
		
	}
//	@Override
//	public SkeletonPatt reWrite() {
//		refactor();
//		return this;
//	}
//	
//	private CompPatt refactor() {
//		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
//		
//		// pipe intro
//		PipePatt pipe = new PipePatt();
//		pipe.setChildren((ArrayList<SkeletonPatt>) getChildren().stream().map(o -> util.clone(o)).collect(Collectors.toList()));
//		pipe.setReWritingRule(ReWritingRules.PIPE_INTRO);
//		pipe.calculateIdealServiceTime();
////		if(isCoarseReWrite) {
////			FarmPatt farm = new FarmPatt();
////			ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
////			fc.add(pipe);
////			farm.setChildren(fc);
////			farm.setReWritingRule(ReWritingRules.FARM_INTRO);
////			farm.calculateIdealServiceTime();
////			patterns.add(farm);
////		}else {
//			patterns.add(pipe);
////		}
//		// farm intro
////		if(!util.detectLoop(this, FarmPatt.class)) {
//		FarmPatt farm = new FarmPatt();
//		CompPatt compStage = new CompPatt();
//		ArrayList<SkeletonPatt> fc = new ArrayList<SkeletonPatt>();
//		compStage.setChildren((ArrayList<SkeletonPatt>) getChildren().stream().map(o -> util.clone(o)).collect(Collectors.toList()));
//		compStage.setRule(getRule());
//		compStage.calculateIdealServiceTime();
//		fc.add(compStage);
//		farm.setChildren(fc);
//		farm.setReWritingRule(ReWritingRules.FARM_INTRO);
//		farm.calculateIdealServiceTime();
//		patterns.add(farm);
//		// map intro
//
//		// for each stage rewrite
//		for (SkeletonPatt stage : getChildren()) {
//				stage.setParent(this);
//				stage.reWrite();
//				patterns.addAll(stage.getPatterns());
//
//		}
//		// creat map of the comp
//		if (getChildren().stream().allMatch(sk -> sk instanceof MapPatt)) {
//			ArrayList<SkeletonPatt> compStages = (ArrayList<SkeletonPatt>) getChildren().stream()
//					.map(p -> p.getChildren().get(0)).collect(Collectors.toList());
//			ArrayList<SkeletonPatt> mapStages =  (ArrayList<SkeletonPatt>) compStages.stream().map(o -> util.clone(o)).collect(Collectors.toList());
//
//			MapPatt map = new MapPatt();
//			CompPatt compMap = new CompPatt();
//
//			compMap.setChildren(mapStages);
//			
//			ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
//			compMap.calculateIdealServiceTime();
//			mNodes.add(compMap);
//			map.setChildren(mNodes);
//			map.calculateIdealServiceTime();
//			map.setReWritingRule(ReWritingRules.MAP_OF_COMP);
//			patterns.add(map);
//		}
//
//		setPatterns(patterns);
//		if (getParent() != null)
//			setPatterns(util.createTreeNode(getParent(), this));
//		calculateIdealServiceTime();
//		return this;
//	}

}
