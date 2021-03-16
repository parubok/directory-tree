package org.swingk.io.dirtree;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class DirTreeUtils {
    public static void configureTree(JTree tree, DirTreeModel<?> model) {
        tree.clearSelection();
        tree.setModel(model);
        tree.expandPath(new TreePath(model.getRoot()));
        tree.expandPath(model.getFileSystemNodeTreePath());
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
    }
}
