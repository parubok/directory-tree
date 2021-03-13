package org.swingk.io.dirtree;

import java.nio.file.FileSystem;
import java.nio.file.Path;

public class DefaultNodeFactory implements DirectoryNodeFactory {
    @Override
    public DirectoryNode createRootNode() {
        return new DirectoryNode();
    }

    @Override
    public DirectoryNode createFileSystemNode(FileSystem fs) {
        return new DirectoryNode(fs);
    }

    @Override
    public DirectoryNode createDirectoryNode(Path dir) {
        return new DirectoryNode(dir);
    }
}
