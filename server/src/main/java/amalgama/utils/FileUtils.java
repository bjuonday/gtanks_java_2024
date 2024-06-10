package amalgama.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static byte[] GetFileBytes(String path) throws IOException {
        Path p = Paths.get(path);
        return Files.readAllBytes(p);
    }

    public static String readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String str = String.join("", reader.lines().toList());
        reader.close();
        return str;
    }
}
