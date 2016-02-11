package tumbleweed;

import java.util.List;

public class State {
	public Input i;
	
	//TODO: add variables
	public int data;
	
	public State(Input i) {
		this.i = i;
		//TODO: generate empty state
	}
	
	public String toString() {
		//TODO: print me as given in the problem statement 
		return "fill me";
	}
	
	public String visualize() {
		//TODO: visualize 
		return "visualization";
	}
	
	public static State solve(Input i) {
		//TODO: solve me
		return new State(i);
	}
	
	public List<Input> readAll() {
		//TODO: read all inputs from data dir 
		return null;
	}
}
