package tumbleweed;

import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;

public class Delivery {
	int warehouse, orderid;
	List<Integer> products;
	int numberOfProducts_save;
	List<Integer> prodInd;
	List<Integer> prodNum;
	int load = 0;
	
	boolean done;

	public Delivery(int warehouse, int orderid) {
		super();
		this.warehouse = warehouse;
		this.orderid = orderid;
		products = new LinkedList<Integer>();
		numberOfProducts_save = -1;
	}	
	
	int numberOfProducts() {
		if (numberOfProducts_save == -1) {
			HashSet<Integer> set = new HashSet<>(products);
			numberOfProducts_save = set.size();
		}
		return numberOfProducts_save;
		// 	prodInd = new ArrayList<>();
		// 	prodNum = new ArrayList<>();
		// 	for (int p = 0; p < products.size(); ++p) {
		// 		boolean found = false;
		// 		for (int j = 0; j < prodInd.size(); ++j) {
		// 			if (prodInd.get(j) == products.get(p)) {
		// 				prodNum.set(j, prodNum.get(j) + 1);
		// 				found = true;
		// 				break;
		// 			}
		// 		}
		// 		if (!found) {
		// 			prodInd.add(p);
		// 			prodNum.add(1);
		// 		}
		// 	}		
		// 	numberOfProducts_save = prodInd.size();
		// }
		// return numberOfProducts_save;
	}
}
