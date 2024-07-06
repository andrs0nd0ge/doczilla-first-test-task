import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private final List<String> filePaths = Arrays.asList(
            "src/test1.txt",
            "src/test2.txt",
            "src/test3.txt"
    );

    @SuppressWarnings("FieldCanBeLocal")
    private final String REGEX_PATTERN = "require '([^']+)'"; // Match everything inside '' and after the word "require"

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException {
        Map<String, FileNode> fileNodes = new HashMap<>(); // Initialise dictionary to store file paths as keys and information (FileNode class) about them as values
        for (String filePath : filePaths) {
            processFilesAndFillFileNodes(filePath, fileNodes);
        }

        List<String> sortedFilesList = sortFiles(fileNodes);

        for (String sortedFile : sortedFilesList) {
            System.out.print(sortedFile);
        }
    }

    private void processFilesAndFillFileNodes(String filePath, Map<String, FileNode> fileNodes) throws IOException {
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
        }

        fileNodes.put(filePath, fileNode);
    }

    private List<String> sortFiles(Map<String, FileNode> files) {
        List<String> sortedContent = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();
        Stack<String> pathStack = new Stack<>();

        for (FileNode node : files.values()) {
            if (!visited.contains(node.getFilePath())) {
                visitFiles(node, files, visited, visiting, sortedContent, pathStack);
            }
        }

        return sortedContent;
    }

    private void visitFiles(FileNode node,
                            Map<String, FileNode> fileNodes,
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
                    visitFiles(fileNodes.get(dependency), fileNodes, visited, visiting, sortedFileContent, pathStack);
                }
            }

            visiting.remove(node.getFilePath());
            visited.add(node.getFilePath());

            sortedFileContent.add(node.getContent());

            pathStack.pop();
        }
    }
}