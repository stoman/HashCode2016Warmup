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
	
	public Input(Path p) throws IOException {
	    src=p;
	    dst=getPath(src.toString().replace(".in", ".ans"));
	    vis=getPath(src.toString().replace(".in", ".vis"));
		Scanner sc = new Scanner(src);
		//TODO: read input
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
