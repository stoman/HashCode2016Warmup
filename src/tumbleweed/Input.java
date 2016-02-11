package tumbleweed;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
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
        c_x = new int[C];
        c_y = new int[C];
        c_l = new int[C];
        c_products = new int[C][];
        for (int i = 0; i < C; ++i) {
            c_x[i] = sc.nextInt();
            c_y[i] = sc.nextInt();
            c_l[i] = sc.nextInt();
            c_products[i] = new int[c_l[i]];
            for (int j = 0; j < c_l[i]; ++j)
                c_products[i][j] = sc.nextInt();
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
}
