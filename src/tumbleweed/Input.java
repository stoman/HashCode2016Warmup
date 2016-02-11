package tumbleweed;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Scanner;

public class Input {
	//TODO: add variables
	public int data;
	public Path src, dst, vis;
	
	public Input(Scanner s, Path p) {
	    this.src=p;
	    
		//TODO: read input
	}
	
	static LinkedList<Input> readAll() {
		LinkedList<Path> files = new LinkedList<>();
		Path path = FileSystems.getDefault().getPath("data");
		try {
			listFiles(path, files);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<Input> inputs = new LinkedList<>();
		for (Path p : files) {
			try {
				inputs.add(new Input(new Scanner(p),p));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return inputs;
	}

	static void listFiles(Path path, LinkedList<Path> files) throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					listFiles(entry, files);
				} else {
					if (entry.endsWith(".in")) {
						files.add(entry);
					}
				}
			}
		}
	}
}
