package org.swingk.io.dirtree;

import javax.swing.tree.TreeNode;
import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * @param <T> Type of this node and its children.
 */
public interface DirNode<T extends DirNode<T>> extends TreeNode {

    static String getName(Path path) {
        Path fileName = path.getFileName();
        return fileName != null ? fileName.toString() : path.toString();
    }

    /**
     * @return {@code null} for root or directory nodes.
     */
    FileSystem getFileSystem();

    /**
     * @return {@code null} for root or file system nodes.
     */
    Path getDirectory();

    @Override
    T getChildAt(int index);

    void add(T child);
}
