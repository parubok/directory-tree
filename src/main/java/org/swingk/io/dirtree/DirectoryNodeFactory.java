package org.swingk.io.dirtree;

import java.nio.file.FileSystem;
import java.nio.file.Path;

public interface DirectoryNodeFactory {
    DirectoryNode createRootNode();

    DirectoryNode createFileSystemNode(FileSystem fs);

    DirectoryNode createDirectoryNode(Path dir);
}
