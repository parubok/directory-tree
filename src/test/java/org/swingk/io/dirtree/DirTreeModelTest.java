package org.swingk.io.dirtree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.tree.TreePath;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class DirTreeModelTest {
    @Test
    public void basicTest() {
        DirTreeModel<DefaultDirNode> model = new DirTreeModel<>(new DefaultNodeFactory());
        Assertions.assertNotNull(model.getRoot());
        Assertions.assertNull(model.getRoot().getDirectory());
        Assertions.assertNull(model.getRoot().getFileSystem());
        Assertions.assertEquals(1, model.getChildCount(model.getRoot()));
        Assertions.assertNotNull(model.getFileSystemNode());
        Assertions.assertNull(model.getFileSystemNode().getDirectory());
        Assertions.assertNotNull(model.getFileSystemNode().getFileSystem());

        List<DefaultDirNode> fileSystemRootNodes = model.getFileSystemRootNodes();
        Assertions.assertFalse(fileSystemRootNodes.isEmpty());
        Assertions.assertEquals(fileSystemRootNodes.size(), model.getChildCount(model.getFileSystemNode()));
        for (DefaultDirNode fileSystemRootNode : fileSystemRootNodes) {
            Assertions.assertEquals(0, fileSystemRootNode.getDirectory().getNameCount());
        }

        Path fileSystemRoot0 = fileSystemRootNodes.get(0).getDirectory();
        Assertions.assertNotNull(fileSystemRoot0);
        Optional<TreePath> p = model.getTreePath(fileSystemRoot0);
        Assertions.assertTrue(p.isPresent());
        Assertions.assertEquals(new TreePath(new Object[]{model.getRoot(), model.getFileSystemNode(),
                fileSystemRootNodes.get(0)}), p.get());
    }
}
