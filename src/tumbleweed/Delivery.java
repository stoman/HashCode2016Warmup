package tumbleweed;

import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;

public class Delivery {
	int warehouse, orderid;
	List<Integer> products;
	int numberOfProducts_save;
	int load = 0;
	
	boolean done;

	public Delivery(int warehouse, int orderid) {
		super();
		this.warehouse = warehouse;
		this.orderid = orderid;
		products = new LinkedList<Integer>();
		numberOfProducts_save = -1;
	}	
	
	public int numberOfProducts() {
		if (numberOfProducts_save == -1) {
			HashSet<Integer> set = new HashSet<>(products);
			numberOfProducts_save = set.size();
		}
		return numberOfProducts_save;
	}
	
	public String toString() {
		return "Delivery, order: " + orderid + ", warehouse: " + warehouse + ", products: " + products;
	}
}
