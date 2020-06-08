package tree.model;

import java.util.Map;

public class Solution {
	Map<SkeletonPatt, Integer> solutionList;
	SkeletonPatt bestSolution;
	
	public Map<SkeletonPatt, Integer> getSolutionList() {
		return solutionList;
	}
	public void setSolutionList(Map<SkeletonPatt, Integer> solutionList) {
		this.solutionList = solutionList;
	}
	public SkeletonPatt getBestSolution() {
		return bestSolution;
	}
	public void setBestSolution(SkeletonPatt bestSolution) {
		this.bestSolution = bestSolution;
	}
	
	
}
