package org.swingk.io.dirtree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.FileSystem;
import java.nio.file.Path;

public class DirectoryNode extends DefaultMutableTreeNode {

    public static String getName(Path path) {
        Path fileName = path.getFileName();
        return fileName != null ? fileName.toString() : path.toString();
    }

    private final FileSystem fileSystem;
    private final Path directory;
    private final String str;

    public DirectoryNode() {
        super();
        this.fileSystem = null;
        this.directory = null;
        this.str = "root";
    }

    public DirectoryNode(FileSystem fileSystem) {
        super();
        this.fileSystem = fileSystem;
        this.directory = null;
        this.str = "Computer"; // getFileSystem().toString();
    }

    public DirectoryNode(Path directory) {
        super();
        this.fileSystem = null;
        this.directory = directory;
        this.str = getName(getDirectory());
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public Path getDirectory() {
        return directory;
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public DirectoryNode getChildAt(int index) {
        return (DirectoryNode) super.getChildAt(index);
    }
}
