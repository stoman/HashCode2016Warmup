package tumbleweed;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Input {
	// General
	int rows, cols;
	int D; // Drones Available
	int deadline; // deadline der Simulation
	int maxload; // maxload of a drone
	
	// Products
	int P; // Number of products (<= 10000)
	int weights[]; // weights of products
	
	// Warehouses
	int W; // Number of Warehouses (<= 10000)
	int w_x[]; // X-Pos of Warehouses
	int w_y[]; // Y-Pos of Warehouses
	int w_stock[][]; // w_stock[w][p]: Stock of product p in warehouse w
	
	// Orders
	int C; // Number of Orders (<= 10000)
	int c_x[]; // X-Pos of Orders
	int c_y[]; // Y-Pos of Orders
	int c_l[]; // c_l[c]: Number of ordered products for order c
	int c_products[][]; // c_products[c][l]: Product Number of the l-th product in the c-th order
	
	//TODO: replace me
	Order[] orders;

	public int data;
	public Path src, dst, vis, visfrq;
	
	public Input(Path p) throws IOException {
	    src=p;
	    dst=getPath(src.toString().replace(".in", ".ans"));
	    vis=getPath(src.toString().replace(".in", ".vis"));
	    visfrq=getPath(src.toString().replace(".in", ".visfrq"));
		Scanner sc = new Scanner(src);
		
		//First Section
		rows = sc.nextInt();
		cols = sc.nextInt();
		D = sc.nextInt();
		deadline = sc.nextInt();
		maxload = sc.nextInt();
		
		// Second Section
		P = sc.nextInt();
		weights = new int[P];
		for (int i = 0; i < P; ++i)
            weights[i] = sc.nextInt();
            
        // Third Section
		W = sc.nextInt();
		w_x = new int[W];
		w_y = new int[W];
		w_stock = new int[W][P];
		for (int i = 0; i < W; ++i) {
            w_x[i] = sc.nextInt();
            w_y[i] = sc.nextInt();
            for (int j = 0; j < P; ++j)
                w_stock[i][j] = sc.nextInt();
		}
        
        // Fourth Section
        C = sc.nextInt();
        orders = new Order[C];
        c_x = new int[C];
        c_y = new int[C];
        c_l = new int[C];
        c_products = new int[C][];
        for (int i = 0; i < C; ++i) {
            orders[i] = new Order();
            orders[i].x = sc.nextInt();
            c_x[i] = orders[i].x;
            orders[i].y = sc.nextInt();
            c_y[i] = orders[i].y;
            int l  = sc.nextInt();
            c_l[i] = l;
            c_products[i] = new int[l];
            orders[i].products = new int[l];
            for (int j = 0; j < l; ++j) {
                orders[i].products[j] = sc.nextInt();
                c_products[i][j] = orders[i].products[j];
            }
        }
	}
	
	static LinkedList<Input> readAll(String folder) {
		LinkedList<Path> files = new LinkedList<>();
		Path path = getPath(folder);
		try {
			listFiles(path, files);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<Input> inputs = new LinkedList<>();
		for (Path p : files) {
			try {
				inputs.add(new Input(p));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return inputs;
	}

	static Path getPath(String file) {
		return FileSystems.getDefault().getPath(file);
	}
	
	static void listFiles(Path path, LinkedList<Path> files) throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					listFiles(entry, files);
				} else {
					if (entry.toString().endsWith(".in")) {
						files.add(entry);
					}
				}
			}
		}
	}
	
	public List<List<Delivery>> planDeliveries() {
		List<List<Delivery>> r = new ArrayList<List<Delivery>>();
		for (int i = 0; i < orders.length; i++) {
			r.add(null);
		}
		
		//determine order to fulfill orders
		ArrayList<Integer> sort = new ArrayList<Integer>();
		for (int i = 0; i < orders.length; i++) {
			sort.add(i);
		}
		sort.sort(new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				int dist0 = 0;
				dist0 += (orders[o1].x - cols/2)*(orders[o1].x - cols/2);
				dist0 += (orders[o1].y - rows/2)*(orders[o1].y - rows/2);
				int dist1 = 0;
				dist1 += (orders[o2].x - cols/2)*(orders[o2].x - cols/2);
				dist1 += (orders[o2].y - rows/2)*(orders[o2].y - rows/2);
				return dist1 - dist0;
			}
		});
		
		//copy stock array
		int[][] stock = new int[w_stock.length][w_stock[0].length];
		for (int i = 0; i < stock.length; i++) {
			for (int j = 0; j < stock[i].length; j++) {
				stock[i][j] = w_stock[i][j];
			}
		}
		
		//fulfill each order step by step
		for(int o: sort) {
			List<Delivery> deliveriesOrder = new LinkedList<Delivery>();
			
			//order warehouses by dist
			ArrayList<Integer> warehousesByDist = new ArrayList<Integer>();
			for(int i = 0; i < W; i++) {
				warehousesByDist.add(i);
			}
			warehousesByDist.sort(new Comparator<Integer>() {
				public int compare(Integer arg0, Integer arg1) {
					int dist0 = 0;
					dist0 += (orders[o].x - w_x[arg0])*(orders[o].x - w_x[arg0]);
					dist0 += (orders[o].y - w_y[arg0])*(orders[o].y - w_y[arg0]);
					int dist1 = 0;
					dist1 += (orders[o].x - w_x[arg1])*(orders[o].x - w_x[arg1]);
					dist1 += (orders[o].y - w_y[arg1])*(orders[o].y - w_y[arg1]);
					return dist0 - dist1;
				}
			});
			
			//copy products contained in this order
			int[] ps = new int[orders[o].products.length];
			for (int i = 0; i < ps.length; i++) {
				ps[i] = orders[o].products[i];
			}
			
			for(int w: warehousesByDist) {
				//check available goods
				for (int i = 0; i < ps.length; i++) {
					if(ps[i] != -1 && stock[w][ps[i]] > 0) {
						//choose first delivery
						boolean done = false;
						for(Delivery d: deliveriesOrder) {
							if(!done && d.load + weights[ps[i]] <= maxload) {
								done = true;
								d.load += weights[ps[i]];
								d.products.add(ps[i]);
								break;
							}
						}
						if(!done) {
							Delivery d = new Delivery(w, o);
							d.load += weights[ps[i]];
							d.products.add(ps[i]);
							deliveriesOrder.add(d);
						}
						stock[w][ps[i]]--;
						ps[i] = -1;
					}
				}
			}
			r.set(o, deliveriesOrder);
		}
		
		System.out.println("====================\n");
		for(int i = 0; i < orders.length; i++) {
			System.out.println(orders[i].toString());
			for(Delivery d: r.get(i)) {
				System.out.println(d.toString());
			}
			System.out.println();
		}
		return r;
	}
}
