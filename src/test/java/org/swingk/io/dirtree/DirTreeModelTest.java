package org.swingk.io.dirtree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.tree.TreePath;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

public class DirTreeModelTest {
    @Test
    public void basicTest() {
        var model = new DirTreeModel<>(DirTreeModel.NAME_COMPARATOR, true, true, new DefaultNodeFactory());
        Assertions.assertNotNull(model.getRoot());
        Assertions.assertFalse(model.isLeaf(model.getRoot()));
        Assertions.assertNull(model.getRoot().getDirectory());
        Assertions.assertNull(model.getRoot().getFileSystem());
        Assertions.assertEquals(1, model.getChildCount(model.getRoot()));
        Assertions.assertNotNull(model.getFileSystemNode());
        Assertions.assertNull(model.getFileSystemNode().getDirectory());
        Assertions.assertNotNull(model.getFileSystemNode().getFileSystem());
        Assertions.assertFalse(model.isLeaf(model.getFileSystemNode()));

        var fsRoots = new ArrayList<DefaultDirNode>();
        for (int i = 0; i < model.getChildCount(model.getFileSystemNode()); i++) {
            fsRoots.add(model.getChild(model.getFileSystemNode(), i));
        }
        Assertions.assertFalse(fsRoots.isEmpty());
        for (int i = 0; i < fsRoots.size(); i++) {
            Assertions.assertEquals(0, fsRoots.get(i).getDirectory().getNameCount());
            Assertions.assertEquals(i, model.getIndexOfChild(model.getFileSystemNode(), fsRoots.get(i)));
        }

        Path fsRoot0 = fsRoots.get(0).getDirectory();
        Assertions.assertNotNull(fsRoot0);
        Optional<TreePath> p = model.getTreePath(fsRoot0);
        Assertions.assertTrue(p.isPresent());
        Assertions.assertEquals(new TreePath(new Object[]{model.getRoot(), model.getFileSystemNode(),
                fsRoots.get(0)}), p.get());

        Assertions.assertEquals(new TreePath(new Object[]{model.getRoot(), model.getFileSystemNode()}),
                model.getFileSystemNodeTreePath());
    }
}
