package org.swingk.io.dirtree;

import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * Provides instances of {@link DirNode} for {@link DirTreeModel}.
 */
public interface DirNodeFactory<T extends DirNode<T>> {
    T createRootNode();

    T createFileSystemNode(FileSystem fs);

    T createDirectoryNode(Path dir);
}
