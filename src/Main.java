import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        Path path1 = Paths.get("src/test1.txt");
        Path path2 = Paths.get("src/test2.txt");

        String fileStr1 = Files.readString(path1);
        String fileStr2 = Files.readString(path2);

        System.out.println(fileStr1 + fileStr2);
    }
}