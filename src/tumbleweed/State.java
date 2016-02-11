package tumbleweed;

import java.util.List;

public class State {
	public Input in;
	List<List<Delivery>> droneSchedules;
	
	//TODO: add variables
	public int data;
	
	public State(Input in) {
		this.in = in;
		//TODO: generate empty state
	}
	
	public String toString() {
		//TODO: print me as given in the problem statement 
		return "fill me";
	}
	
	public String visualize() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < in.C; i++) {
			sb.append("\"(Order #" + i + ": " + in.c_l[i] + " products\" ");
			sb.append(in.c_x[i]).append(" ").append(in.c_y[i]).append("\n");
		}
		for (int i = 0; i < in.W; i++) {
			sb.append("\"Warehouse #").append(i).append("\" ");
			sb.append(in.w_x[i]).append(" ").append(in.w_y[i]).append("\n");
		}
		return sb.toString();
	}
	
	public String visualizeFreq() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < in.C; i++) {
			int sum = 0;
			for (int j = 0; j < in.c_l[i]; j++) {
				sum += in.weights[in.c_products[i][j]];
			}
			sb.append(sum).append('\n');
		}
		return sb.toString();
	}
	
	public static State solve(Input i) {
		List<Delivery> deliveries = i.planDeliveries();
		//TODO: solve me
		return new State(i);
	}
	
	public List<Input> readAll() {
		//TODO: read all inputs from data dir 
		return null;
	}
}
