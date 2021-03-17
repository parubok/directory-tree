package org.swingk.io.dirtree;

import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * Provides instances of {@link DirNode} for {@link DirTreeModel}.
 */
public interface DirNodeFactory<T extends DirNode<T>> {

    /**
     * @return New root node for the model.
     */
    T createRootNode();

    /**
     * @return New node that represents the model's filesystem (will be added as a child to the model root).
     */
    T createFileSystemNode(FileSystem fs);

    /**
     * @return New node that represents a filesystem directory.
     */
    T createDirectoryNode(Path dir);
}
