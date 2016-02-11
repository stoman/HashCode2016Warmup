package tumbleweed;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class State {
	public Input in;
	ArrayList<List<Delivery>> droneSchedules = new ArrayList<>();
	
	public State(Input in) {
		this.in = in;
		//TODO: generate empty state
		for (int i = 0; i < in.D; i++) {
			droneSchedules.add(new LinkedList<>());
		}
	}
	
	public String toString() {
		//TODO: print me as given in the problem statement 
		return "fill me";
	}
	
	public String visualize() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < in.C; i++) {
			sb.append("\"O" + i + "-" + in.c_l[i] + "\" ");
			sb.append(in.c_x[i]).append(" ").append(in.c_y[i]).append(" ").append(0).append("\n");
		}
		for (int i = 0; i < in.W; i++) {
			sb.append("\"W").append(i).append("\" ");
			sb.append(in.w_x[i]).append(" ").append(in.w_y[i]).append(" ").append(1).append("\n");
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
	
	static List<List<Delivery>> currentSchedule = null;
	
	static int getTime(List<Delivery> d, List<Drone> drohnes) {
		return 0;
	}
	
	public static State solve(Input in) {
		State solved = new State(in);
		List<List<Delivery>> deliveries = in.planDeliveries();
		TreeSet<Pair> droneFinished = new TreeSet<>();
		for (int i=0; i < in.D; i++ ) {
			droneFinished.add(new Pair(new Drone(i,0,0,0),0));
		}
		
		while (droneFinished.first().time < in.deadline) {
			List<Drone> freeDrones = new LinkedList<Drone>();
			int best = Integer.MAX_VALUE;
			List<List<Delivery>> bestSchedule = null;
			for (Pair p : droneFinished) {
				// try for every subset of the drohnes
				freeDrones.add(p.drohne);
				for (List<Delivery> order : deliveries) {
					int timeForOrder = getTime(order, freeDrones);
					if (timeForOrder < best) {
						bestSchedule = currentSchedule;
						best = timeForOrder;
					}
	 			}
			}
			int i = 0;
			for (List<Delivery> droneSchedule : bestSchedule) {
				solved.droneSchedules.get(i).addAll(droneSchedule);
				i++;
			}
		}
		return solved;
	}
	
	static class Drone {
		int id;
		int posX;
		int posY;
		int time;
		public Drone(int id, int posX, int posY, int time) {
			super();
			this.id = id;
			this.posX = posX;
			this.posY = posY;
			this.time = time;
		}
	}
	
	static class Pair implements Comparable<Pair>{
		Drone drohne;
		int time;
		
		Pair(Drone drohne, int time) {
			this.drohne = drohne;
			this.time = time;
		}
		
		@Override
		public int compareTo(Pair o) {
			return time - o.time;
		}
	}
}
