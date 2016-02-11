package tumbleweed;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        for(Input i: Input.readAll("data/official")) {
            
            
            
            State s = State.solve(i);
            writeToFile(s.toString(), i.dst);
            writeToFile(s.visualizeFreq(), i.visfrq);
            writeToFile(s.visualize(), i.vis);
        }
    }
    
    public static void writeToFile(String s, Path p) throws IOException {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(p)));
            out.write(s);
            out.close();
    }
}
