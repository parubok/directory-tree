package org.swingk.io.dirtree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DirectoryTreeModelTest {
    @Test
    public void basicTest() {
        DirectoryTreeModel model = DirectoryTreeModel.builder().build();
        Assertions.assertNotNull(model.getRoot());
        Assertions.assertNotNull(model.getFileSystemNode());
    }
}
