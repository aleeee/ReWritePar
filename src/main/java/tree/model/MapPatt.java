package tree.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class MapPatt  implements SkeletonPatt {
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
	int numResource;
	public MapPatt() {
		this.lable= "map";
	}
	public MapPatt(String lable, double serviceTime) {
		super();
		this.lable = lable;
		this.idealServiceTime = serviceTime;
	}
	@Override
	public void refactor(ReWriter reWriter) {
		reWriter.reWrite(this);
	}

	@Override
	public void calculateIdealServiceTime() {
		this.idealServiceTime=Util.getServiceTime(this);

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
		return getLable() +" "+(this.getChildren() != null? " ( " +this.getChildren().toString() +" ) ":null)+ " nw: " +getOptParallelismDegree() ;
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
		MapPatt other = (MapPatt) obj;
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
//		if(rule == null) {
//			if(other.rule != null)
//				return false;
//		}else if(!rule.equals(other.rule))
//			return false;
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
//		return this.toString();
//	}
//	@Override
//	public AttributeType getType() {
//		return AttributeType.STRING;
//	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
		idealServiceTime=Util.getServiceTime(this);		
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
	public double getOptServiceTime() {
		return optServiceTime =Util.getOptimalServiceTime(this);
	}
	@Override
	public void setOptServiceTime(double ts) {
		this.optServiceTime=ts;
		
	}
	@Override
	public void setOptParallelismDegree(int p) {
		this.optParDegree=p;
	}
	@Override
	public double calculateOptimalServiceTime() {
		return this.optServiceTime = Util.getOptimalServiceTime(this);
		
	}
	
	@Override
	public int getNumberOfResources() {
		return this.numResource=(this.optParDegree*this.children.get(0).getNumberOfResources()) + 2;
	}
	
	@Override
	public void setNumberOfResources(int r) {
		this.numResource=r;
	}
	@Override
	public void addConstraint(SolverModel model)
			throws IloException {
		IloCP cplex = model.getCplex();
		Map<SkeletonPatt, List<IloNumVar>> variables = model.getVariables();
		List<IloNumVar> fVars = variables.get(this);
		IloIntVar pd = (IloIntVar) fVars.get(1);
		cplex.addLe(pd, model.getNumAvailableProcessors());
		cplex.addLe(pd, this.idealParDegree);
//		IloIntExpr c = cplex.constant(2);
//		IloIntExpr nw =  cplex.diff(pd,c);
		IloIntExpr res= model.getResourcesVars().get(this);
//		cplex.addEq(res, cplex.sum(pd,2));
		IloIntExpr childRes= model.getResourcesVars().get(this.children.get(0));
		cplex.addEq(res, cplex.sum(cplex.prod(childRes, pd),2));
//		cplex.addLe(cplex.sum(cplex.prod(pd ,variables.get(this.children.get(0)).get(1)),2), model.getNumAvailableProcessors());
		cplex.addLe(cplex.sum(cplex.prod(pd ,childRes),2), model.getNumAvailableProcessors());
		cplex.addLe(res, model.getNumAvailableProcessors());
		this.children.get(0).addConstraint(model);
		
	
	}
	@Override
	public SolverModel addObjective(SolverModel model) throws IloException {
		IloNumExpr obj = model.getObj();
		IloNumExpr pd_obj = model.getPd_obj();
		IloCP cplex = model.getCplex();
		Map<SkeletonPatt, List<IloNumVar>> variables = model.getVariables();
		List<IloNumVar> vars = variables.get(this);
		IloIntVar pd = (IloIntVar) vars.get(1);
//		IloIntExpr nw =  cplex.diff(pd,2);
		double ts = this.children.get(0).getIdealServiceTime();			
		cplex.addEq(vars.get(0) , cplex.div( (int)ts,pd));
		obj =  cplex.sum(obj, vars.get(0));
		pd_obj = cplex.sum(pd_obj,pd);
		model.setCplex(cplex);
		model.setObj(obj);
		model.setPd_obj(pd_obj);
		model = this.children.get(0).addObjective(model);			
		return model;
	}
//	@Override
//	public SkeletonPatt reWrite() {
//		refactor();
//		return this;
//	}
//	
//	private MapPatt refactor() {
//		Set<SkeletonPatt> patterns = new LinkedHashSet<SkeletonPatt>();
////		mapelim map(D)!D
//		SkeletonPatt p = Util.clone(getChildren().get(0));
//
//		p.setReWritingRule(ReWritingRules.MAP_ELIM);
//		p.calculateIdealServiceTime();
//		patterns.add(p);
//		
//		// compofmap map(comp(D1;D2)!comp((map(D1);map(D2)) and pipeofmap
//		// map(pipe(D1;D2) = pipe((map(D1);map(D2))
//
//		if (getChildren().get(0) instanceof CompPatt) {
//			CompPatt compPat = new CompPatt();
//			CompPatt c = (CompPatt) Util.clone(getChildren().get(0));
//			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
//			for (SkeletonPatt sk : c.getChildren()) {
//				MapPatt m = new MapPatt();
//				ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
//				mNodes.add(Util.clone(sk));
//				m.setChildren(mNodes);
//				m.calculateIdealServiceTime();
//				nodes.add(m);
//			}
//			compPat.setChildren(nodes);
//			compPat.calculateIdealServiceTime();
//			compPat.setReWritingRule(ReWritingRules.MAP_DIST);
//			patterns.add(compPat);
//		} else if (getChildren().get(0) instanceof PipePatt) {
//			PipePatt pipe = new PipePatt();
//			PipePatt pi = (PipePatt) Util.clone(getChildren().get(0));
//			ArrayList<SkeletonPatt> nodes = new ArrayList<SkeletonPatt>();
//			for (SkeletonPatt sk :pi.getChildren()) {
//				MapPatt m = new MapPatt();
//				ArrayList<SkeletonPatt> mNodes = new ArrayList<SkeletonPatt>();
//				mNodes.add(Util.clone(sk));
//				m.setChildren(mNodes);
//				m.calculateIdealServiceTime();
//				nodes.add(m);
//			}
//			pipe.setChildren(nodes);
//			pipe.calculateIdealServiceTime();
//			pipe.setReWritingRule(ReWritingRules.PIPE_OF_MAP);
//			patterns.add(pipe);
//		}
//		setPatterns(patterns);
//		if (getParent() != null)
//			setPatterns(Util.createTreeNode(getParent(), this));
//		calculateIdealServiceTime();
//		return this;
//	}
	
}
