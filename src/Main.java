import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    @SuppressWarnings("FieldCanBeLocal")
    private final String REGEX_PATTERN = "require '([^']+)'";

    private final List<String> files = Arrays.asList(
            "src/test1.txt",
            "src/test2.txt",
            "src/test3.txt"
    );

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException {
        String filePath = files.get(0);
        String txtFile1 = Files.readString(Paths.get(filePath));

        Matcher matcher = Pattern.compile(REGEX_PATTERN).matcher(txtFile1);

        if (matcher.find()) {
            String requiredTxt = matcher.group(1);
            String txtFile2 = Files.readString(Paths.get(requiredTxt));
            System.out.println(txtFile2);
        }
    }
}