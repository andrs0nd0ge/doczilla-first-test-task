import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private final List<String> filePaths = Arrays.asList(
            "src/test1.txt",
            "src/test2.txt",
            "src/test3.txt"
    );

    @SuppressWarnings("FieldCanBeLocal")
    private final String REGEX_PATTERN = "require '([^']+)'"; // match everything inside '' and after the word "require"

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException {
        for (String filePath : filePaths) {
            processFiles(filePath);
        }
    }

    private void processFiles(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        FileNode fileNode = new FileNode(filePath);

        for (String line : lines) {
            Matcher matcher = Pattern.compile(REGEX_PATTERN).matcher(line);

            if (matcher.find()) {
                String dependency = matcher.group(1);
                fileNode.addDependency(dependency);
            } else {
                fileNode.addContent(line);
            }
            System.out.print(fileNode.getContent());
        }

        System.out.println(fileNode.getDependencies());
    }

    private void visit(FileNode node,
                       Set<String> visited,
                       Set<String> visiting,
                       List<String> sortedFileContent,
                       Stack<String> pathStack) {
        if (visiting.contains(node.getFilePath())) {
            pathStack.push(node.getFilePath());
        }

        if (!visited.contains(node.getFilePath())) {
            visiting.add(node.getFilePath());
            pathStack.push(node.getFilePath());

            for (String dependency : node.getDependencies()) {
                if (!visited.contains(node.getFilePath())) {
                    visit(node, visited, visiting, sortedFileContent, pathStack);
                }
            }

            visiting.remove(node.getFilePath());
            visited.add(node.getFilePath());

            sortedFileContent.add(node.getContent());

            pathStack.pop();
        }
    }
}