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
     * @param directory Absolute {@link Path} of a filesystem directory.
     * @param filesystemRoot {@code true} if the {@link Path} represents filesystem root directory (e.g. 'C:\' on Windows).
     * @return New node that represents the filesystem directory.
     */
    T createDirectoryNode(Path directory, boolean filesystemRoot);
}
