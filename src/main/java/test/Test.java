package test;

import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class Test {
	public static void main(String args[]) {
		cartesianProduct();
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
}
