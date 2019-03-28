package FileLogging;

import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
    private String fileName;
    private String path;

    private FileWriter fileOutput;


    public FileHandler(String pramPath) {
        this.path = pramPath;
    }

    public void writeToFile(String logLine) throws IOException {
        fileOutput = new FileWriter(path, true);



    }
}
