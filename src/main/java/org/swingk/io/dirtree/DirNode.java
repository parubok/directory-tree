package org.swingk.io.dirtree;

import javax.swing.tree.TreeNode;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface DirNode<T extends DirNode<T>> extends TreeNode {

    static String getName(Path path) {
        Path fileName = path.getFileName();
        return fileName != null ? fileName.toString() : path.toString();
    }

    default List<T> getChildren() {
        final int c = getChildCount();
        if (c == 0) {
            return Collections.emptyList();
        }
        var list = new ArrayList<T>(c);
        for (int i = 0; i < c; i++) {
            list.add(getChildAt(i));
        }
        return list;
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
