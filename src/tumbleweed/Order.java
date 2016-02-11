package tumbleweed;

import java.util.List;

public class Order {
	int x, y;
	int[] products;

	public String toString() {
		String s = "order to " + x + "|" + y + ", products:";
		for(int p: products) {
			s += " " + p;
		}
		return s;
	}
}
