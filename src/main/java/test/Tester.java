package test;

import java.util.ArrayList;

public class Tester {
	public static void main(String[] args) {
		Tester t = new Tester();
		t.test();
	}
	public class Bean{
		String id;
		ArrayList<Bean> myBeans;
		public Bean(String id) {
			this.id = id;			
		}
		public ArrayList<Bean> getMyBeans() {
			return myBeans;
		}
		public void setMyBeans(ArrayList<Bean> myBeans) {
			this.myBeans = myBeans;
		}
		@Override
		public String toString() {
			return "Bean [id=" + id + ", myBeans=" + myBeans + "]";
		}	
		
	}
	 void test() {
		Bean first = new Bean("first");
		Bean second = new Bean("second");
		Bean third = new Bean("third");
		Bean fourth = new Bean("fourth");
		ArrayList<Bean> myBeans = new ArrayList<>();
		ArrayList<Bean> myBeans1 = new ArrayList<>();
		myBeans.add(third);
		myBeans.add(second);
		first.setMyBeans(myBeans);
		myBeans1.addAll(first.getMyBeans());
		fourth.setMyBeans(myBeans1);
		fourth.getMyBeans().set(0, new Bean("fivth"));
		
		System.out.println(first);
		System.out.println(fourth);
	}
}
