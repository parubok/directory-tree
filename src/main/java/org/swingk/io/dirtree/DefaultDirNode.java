package org.swingk.io.dirtree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Default implementation of {@link DirNode}. Based on Swing standard {@link DefaultMutableTreeNode}.
 */
public class DefaultDirNode extends DefaultMutableTreeNode implements DirNode<DefaultDirNode> {

    private final FileSystem fileSystem;
    private final Path directory;
    private final String str;

    public DefaultDirNode() {
        super();
        this.fileSystem = null;
        this.directory = null;
        this.str = "root";
    }

    public DefaultDirNode(FileSystem fileSystem) {
        super();
        this.fileSystem = Objects.requireNonNull(fileSystem);
        this.directory = null;
        this.str = "Computer";
    }

    public DefaultDirNode(Path directory) {
        super();
        this.fileSystem = null;
        this.directory = Objects.requireNonNull(directory);
        this.str = DirTreeUtils.getName(getDirectory());
    }

    /**
     * @return {@code null} for root or directory nodes.
     */
    public FileSystem getFileSystem() {
        return fileSystem;
    }

    /**
     * @return {@link Path} instance that represents the node's directory or {@code null} for root or file system
     * nodes.
     */
    @Override
    public Path getDirectory() {
        return directory;
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public DefaultDirNode getChildAt(int index) {
        return (DefaultDirNode) super.getChildAt(index);
    }

    @Override
    public void add(DefaultDirNode child) {
        super.add(child);
    }
}
