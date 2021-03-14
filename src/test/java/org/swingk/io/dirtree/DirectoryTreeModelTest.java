package org.swingk.io.dirtree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DirectoryTreeModelTest {
    @Test
    public void basicTest() {
        DirectoryTreeModel model = DirectoryTreeModel.builder().build();
        Assertions.assertNotNull(model.getRoot());
        Assertions.assertEquals(1, model.getChildCount(model.getRoot()));
        Assertions.assertNotNull(model.getFileSystemNode());
        Assertions.assertFalse(model.getFileSystemRootNodes().isEmpty());
        Assertions.assertEquals(model.getFileSystemRootNodes().size(), model.getChildCount(model.getFileSystemNode()));
    }
}
