package tumbleweed;

import java.util.LinkedList;
import java.util.List;

public class Delivery {
	int warehouse, orderid;
	List<Integer> products;
	
	boolean done;
	public Delivery(int warehouse, int orderid) {
		super();
		this.warehouse = warehouse;
		this.orderid = orderid;
		products = new LinkedList<Integer>();
	}	
}
