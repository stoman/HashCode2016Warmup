package tumbleweed;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException {
        for(Input i: Input.readAll("data")) {
            State s = State.solve(i);
            BufferedWriter outDst = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(i.dst)));
            outDst.write(s.toString());
            outDst.close();
            BufferedWriter outVis = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(i.vis)));
            outVis.write(s.visualize());
            outVis.close();
        }
    }
}
