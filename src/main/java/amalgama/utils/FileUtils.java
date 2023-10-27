package amalgama.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static byte[] GetFileBytes(String path) throws IOException {
        Path p = Paths.get(path);
        return Files.readAllBytes(p);
    }
}
