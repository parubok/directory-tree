package org.swingk.io.dirtree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.swing.tree.TreePath;
import java.nio.file.Path;
import java.util.Optional;

public class DirectoryTreeModelTest {
    @Disabled
    @Test
    public void getTreePath() {
        DirectoryTreeModel model = DirectoryTreeModel.builder().build();
        Optional<TreePath> p = model.getTreePath(Path.of("C:\\workspace\\java\\directory-tree"));
        Assertions.assertFalse(p.isEmpty());
        Assertions.assertEquals(6, p.get().getPathCount());
        Assertions.assertEquals(model.getRoot(), p.get().getPathComponent(0));
    }

    @Disabled
    @Test
    public void getTreePath_rootOnly() {
        DirectoryTreeModel model = DirectoryTreeModel.builder().build();
        Optional<TreePath> p = model.getTreePath(Path.of("C:\\"));
        Assertions.assertFalse(p.isEmpty());
        Assertions.assertEquals(3, p.get().getPathCount());
        Assertions.assertEquals(model.getRoot(), p.get().getPathComponent(0));
    }
}
