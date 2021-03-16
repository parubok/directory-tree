package org.swingk.io.dirtree;

import javax.swing.JTree;

public class DirTreeUtils {
    public static void configureTree(JTree tree, DirTreeModel<?> model) {
        tree.clearSelection();
        tree.setModel(model);
        tree.expandPath(model.getFileSystemNodeTreePath());
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
    }
}
