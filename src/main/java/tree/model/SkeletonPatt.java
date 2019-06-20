package tree.model;

import java.util.ArrayList;

import rewriter.ReWriter;
import rewriter.SkelReWriter;
import visitor.NodeVisitor;



public interface SkeletonPatt {
	int parallelismDegree();
	long serviceTime();
	long completionTime();
	void setServiceTime(long ts);
	public ArrayList<SkeletonPatt> getChildren();
	public String getLable() ;
	public SkeletonPatt getChild() ;
	public void accept(NodeVisitor visitor);
	public void refactor(ReWriter reWriter);
	public ArrayList<SkeletonPatt> getPatterns();
}
