public class Main {

	static int calls;

	static class A {
		int a;
	}
	static class Dummy {
		int x;
		A a;
		Dummy me;
	}

	public static int add(Dummy d, int a, int b) {
		calls++;
		return a + b;
	}
	public static void main(String... args) {
		System.out.println("Testing");
		Dummy d = new Dummy();
		d.a = new A();
		d.me = d;
		d.x = 3;
		d.a.a = 5;
		Dummy d2 = new Dummy();
		d2.a = new A();
		d2.me = d;
		d2.x = 3;
		d2.a.a = 5;
		Dummy d3 = new Dummy();
		d3.a = new A();
		d3.me = d;
		d3.x = 3;
		d3.a.a = 0;
		Dummy d4 = new Dummy();
		d4.a = new A();
		d4.me = d;
		d4.x = 1;
		d4.a.a = 5;
		Dummy d5 = new Dummy();
		d5.a = new A();
		d5.me = d5;
		d5.x = 3;
		d5.a.a = 5;
		System.out.println(add(d, 5, 3));
		System.out.println(add(d2, 5, 3));
		System.out.println(add(d3, 5, 3));
		System.out.println(add(d4, 5, 3));
		System.out.println(add(d5, 5, 3));
		System.out.println("Number of calls: " + calls);
	}

}
