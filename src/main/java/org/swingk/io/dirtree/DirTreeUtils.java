package org.swingk.io.dirtree;

import javax.swing.JTree;
import java.nio.file.Path;
import java.util.Comparator;

public class DirTreeUtils {

    private DirTreeUtils() {
    }

    /**
     * Default comparator - compares directory names in case insensitive fashion.
     */
    public static final Comparator<Path> NAME_COMPARATOR = Comparator.comparing(path -> getName(path).toLowerCase());

    /**
     * @return File name of the specified {@link Path}.
     */
    public static String getName(Path path) {
        Path fileName = path.getFileName();
        return fileName != null ? fileName.toString() : path.toString();
    }

    /**
     * Does basic configuration for {@link JTree}.
     */
    public static void configureTree(JTree tree, DirTreeModel<?> model) {
        tree.clearSelection();
        tree.setModel(model);
        tree.expandPath(model.getFileSystemNodeTreePath());
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
    }
}
