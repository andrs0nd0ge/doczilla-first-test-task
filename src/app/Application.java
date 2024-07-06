package app;

import config.Configuration;
import exceptions.CycleException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {
    final Configuration config = new Configuration();

    @SuppressWarnings("FieldCanBeLocal")
    private final String REGEX_PATTERN = "require '([^']+)'"; // Match everything inside '' and after the word "require"

    public static void main(String[] args) throws IOException {
        Application application = new Application();
        application.run();
    }

    private void run() throws IOException {
        List<String> filePaths = config.FILE_PATHS;

        for (String filePath : filePaths) {
            if (!filePath.endsWith(".txt")) {
                System.err.println("Each specified file must be of .txt format!");
                return;
            }
        }

        try {
            Map<String, FileNode> fileNodes = new HashMap<>(); // Instantiate dictionary to store file paths as keys and information (app.FileNode class) about them as values

            for (String filePath : filePaths) {
                // Instantiate the app.FileNode class (for each file path): add dependencies of it and add content of each respective file path
                processFilesAndFillFileNodes(filePath, fileNodes);
            }

            List<String> sortedFilesList = sortFiles(fileNodes); // Get sorted files' content as list

            sortedFilesList.forEach(System.out::print); // Print out concatenated list of
            System.out.println();

        } catch (CycleException ex) {
            System.err.println("There is a dependency cycle within the files: " + ex.getCycleString());
        }
    }

    private void processFilesAndFillFileNodes(String filePath, Map<String, FileNode> fileNodes) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath)); // Read all lines using specified file path
        FileNode fileNode = new FileNode(filePath); // Instantiate a FileNode class using specified file path

        for (String line : lines) {
            Matcher matcher = Pattern.compile(REGEX_PATTERN).matcher(line);

            if (matcher.find()) { // Checks if there are dependencies within the given file path
                String dependency = matcher.group(1); // Retrieving dependency
                fileNode.addDependency(dependency);
            } else {
                fileNode.addContent(line);
            }
        }

        fileNodes.put(filePath, fileNode);
    }

    private List<String> sortFiles(Map<String, FileNode> files) throws CycleException {
        List<String> sortedContent = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();
        Stack<String> pathStack = new Stack<>();

        for (FileNode node : files.values()) {
            visitFiles(node, files, visited, visiting, sortedContent, pathStack);
        }

        return sortedContent;
    }

    private void visitFiles(FileNode node,
                            Map<String, FileNode> fileNodes,
                            Set<String> visited,
                            Set<String> visiting,
                            List<String> sortedFileContent,
                            Stack<String> pathStack) throws CycleException {
        if (visiting.contains(node.getFilePath())) {
            pathStack.push(node.getFilePath()); // Push file path into the file path stack to display it in the exception message
            throw new CycleException(new ArrayList<>(pathStack));
        }

        if (!visited.contains(node.getFilePath())) {
            visiting.add(node.getFilePath()); // Add currently visited file to a set
            pathStack.push(node.getFilePath()); // Push file path into the file path stack (to keep track of the visited files)

            for (String dependency : node.getDependencies()) {
                // Recursively call this method for each dependency (required directive)
                visitFiles(fileNodes.get(dependency), fileNodes, visited, visiting, sortedFileContent, pathStack);
            }

            visiting.remove(node.getFilePath()); // Remove the currently visited file from a set, because it's no longer "being visited"
            visited.add(node.getFilePath()); // Mark the just-visited file as visited, since it's no longer "being visited"

            sortedFileContent.add(node.getContent()); // Add sorted content to the list

            pathStack.pop(); // Pop file path from the file path stack, because everything has worked as expected: each dependency in each file was visited
        }
    }
}