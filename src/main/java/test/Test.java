package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import tree.model.CompPatt;
import tree.model.FarmPatt;
import tree.model.MapPatt;
import tree.model.PipePatt;
import tree.model.SeqPatt;
import tree.model.SkeletonPatt;

public class Test {
	public static void main(String args[]) {
		cartesianProduct();
		cp();
	}
	private static void cartesianProduct() {
		Set<Character> first = ImmutableSet.of('a', 'b');
	    Set<Character> second = ImmutableSet.of('c', 'd');
	    Set<Character> third = ImmutableSet.of('a', 'd');
	    Set<List<Character>> result =
	      Sets.cartesianProduct(ImmutableList.of(first, second,third));
	 
	    Function<List<Character>, String> func =
	      new Function<List<Character>, String>() {
	        public String apply(List<Character> input) {
	            return Joiner.on(",").join(input);
	        }
	    };
	    Iterable<String> joined = Iterables.transform(result, func);
	    joined.forEach(s -> System.out.println(s));
	}
	
	private static void cp() {
		List<SkeletonPatt> stage1 = new ArrayList<SkeletonPatt>();
		List<SkeletonPatt> stage2 = new ArrayList<SkeletonPatt>();
		List<SkeletonPatt> stage3 = new ArrayList<SkeletonPatt>();
		stage1.add(new FarmPatt("farm", 0));
		SeqPatt sp = new SeqPatt(0);
		sp.setLable("seq");
		stage1.add(sp);
		stage2.add(new PipePatt("pipe", 0));
		stage2.add(new CompPatt("comp", 0));
		stage3.add(new MapPatt("map", 0));
		
		List<SkeletonPatt> merged = new ArrayList<SkeletonPatt>();
		Set<?> s =  ImmutableSet.builder().addAll(stage1.iterator()).build();
		Set<?> s2 =  ImmutableSet.builder().addAll(stage2.iterator()).build();
		
		Set<?> mergedSet = Sets.cartesianProduct(ImmutableList.of(s,s2));
		stage1 = (List<SkeletonPatt>) mergedSet.iterator().next();
		System.out.println(stage1);
		
		
	}
}
