package app;

import java.util.ArrayList;
import java.util.List;

public class FileNode {
    private final String filePath;
    private final List<String> dependencies = new ArrayList<>();
    private String content = "";

    public FileNode(String filePath) {
        this.filePath = filePath;
    }

    public void addDependency(String dependency) {
        dependencies.add(dependency);
    }

    public void addContent(String content) {
        this.content += content;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public String getContent() {
        return content;
    }
}