//package mcts;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//
//import rewriter.RW;
//import rewriter.ReWriter;
//import tree.model.SkeletonPatt;
//
//public class MCTS {
//	    static Random r = new Random();
//	    static int nActions = 5;
//	    static double epsilon = 1e-6;
//
////	    SkeletonPatt[] children;
//	    double nVisits, totValue;
//	    RW rw = new RW();
//	    public void selectAction(SkeletonPatt root) {
//	        List<SkeletonPatt> visited = new LinkedList<SkeletonPatt>();
//	        SkeletonPatt cur = root;
//	        visited.add(cur);
//	        while (!isLeaf(cur)) {
////	            cur = cur.select();
//	             System.out.println("Adding: " + cur);
//	            visited.addAll(cur.getChildren());
//	        }
//	        expand(cur);
////	        SkeletonPatt newNode = cur.select();
////	        visited.add(newNode);
////	        double value = rollOut(newNode);
//	        for (SkeletonPatt node : visited) {
//	            // would need extra logic for n-player game
//	             System.out.println("updateStatus " +node);
////	            node.updateStats(value);
//	        }
//	    }
//
//	    public void expand(SkeletonPatt pat) {
//	    	pat.refactor(rw);
////	        children = new SkeletonPatt[nActions];
////	        for (int i=0; i<nActions; i++) {
////	            children[i] = new SkeletonPatt();
////	        }
//	    }
//
////	    private SkeletonPatt select() {
////	        SkeletonPatt selected = null;
////	        double bestValue = Double.MIN_VALUE;
////	        for (SkeletonPatt c : children) {
////	            double uctValue =
////	                    c.totValue / (c.nVisits + epsilon) +
////	                            Math.sqrt(Math.log(nVisits+1) / (c.nVisits + epsilon)) +
////	                            r.nextDouble() * epsilon;
////	            // small random number to break ties randomly in unexpanded nodes
////	            // System.out.println("UCT value = " + uctValue);
////	            if (uctValue > bestValue) {
////	                selected = c;
////	                bestValue = uctValue;
////	            }
////	        }
////	         System.out.println("Returning: " + selected);
////	        return selected;
////	    }
//
//	    public boolean isLeaf(SkeletonPatt p) {
//	        return p.getChildren() == null;
//	    }
//
//	    public double rollOut(SkeletonPatt tn) {
//	        // ultimately a roll out will end in some value
//	        // assume for now that it ends in a win or a loss
//	        // and just return this at random
//	        return r.nextInt(2);
//	    }
//
//	    public void updateStats(double value) {
//	        nVisits++;
//	        totValue += value;
//	    }
//
////	    public int arity() {
////	        return children == null ? 0 : children.length;
////	    }
//}
