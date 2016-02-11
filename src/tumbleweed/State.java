package tumbleweed;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

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
	
	static void getTime(List<Delivery> d, Drohne[] drohnes) {
		
	}
	
	public static State solve(Input in) {
		List<List<Delivery>> deliveries = in.planDeliveries();
		TreeSet<Pair> drohneFinished = new TreeSet<>();
		for (int i=0; i < in.D; i++ ) {
			drohneFinished.add(new Pair(new Drohne(i,0,0,0),0));
		}
		
		while (drohneFinished.first().time < in.deadline) {
			List<Drohne> freeDrohnes = new LinkedList<Drohne>();
			int time = drohneFinished.first().time;
			for (Pair p : drohneFinished) {
				if (p.time == time) {
					freeDrohnes.add(p.drohne);
				} else {
					break;
				}
			}
			for (List<Delivery> order : deliveries) {
				
			}
			
		}
		
		//TODO: solve me
		return new State(in);
	}
	
	static class Drohne {
		int id;
		int posX;
		int posY;
		int time;
		public Drohne(int id, int posX, int posY, int time) {
			super();
			this.id = id;
			this.posX = posX;
			this.posY = posY;
			this.time = time;
		}
	}
	
	static class Pair implements Comparable<Pair>{
		Drohne drohne;
		int time;
		
		Pair(Drohne drohne, int time) {
			this.drohne = drohne;
			this.time = time;
		}
		
		@Override
		public int compareTo(Pair o) {
			return time - o.time;
		}
	}
}
