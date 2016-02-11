package tumbleweed;

import java.util.Scanner;

public class Input {
	//TODO: add variables
	public int data;
	
	public Input(Scanner s) {
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
				inputs.add(new Input(new Scanner(p)));
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