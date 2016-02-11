package tumbleweed;

import java.util.Scanner;

public class Input {
	//TODO: add variables
	public int data;
	public Path source;
	
	public Input(Scanner s, Path p) {
		source = p;
		//TODO: read input
	}
	
	static LinkedList<Input> readAll(String folder) {
		LinkedList<Path> files = new LinkedList<>();
		Path path = FileSystems.getDefault().getPath(folder);
		try {
			listFiles(path, files);
		} catch (IOException e) {
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