public class Main {

	static class A {
		int a;
	}
	static class Dummy {
		int x;
		A a;
	}

	public static int add(Dummy d, int a, int b) {
		return a + b;
	}
	public static void main(String... args) {
		System.out.println("Testing");
		Dummy d = new Dummy();
		d.a = new A();
		d.x = 3;
		d.a.a = 5;
		Dummy d2 = new Dummy();
		d2.a = new A();
		d2.x = 3;
		d2.a.a = 5;
		System.out.println(add(d, 5, 3));
		System.out.println(add(d2, 5, 3));
	}

}
