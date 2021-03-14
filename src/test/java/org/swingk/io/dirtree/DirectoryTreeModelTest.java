package org.swingk.io.dirtree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DirectoryTreeModelTest {
    @Test
    public void basicTest() {
        DirectoryTreeModel model = DirectoryTreeModel.builder().build();
        Assertions.assertNotNull(model.getRoot());
        Assertions.assertEquals(1, model.getChildCount(model.getRoot()));
        Assertions.assertNotNull(model.getFileSystemNode());

        List<DirectoryNode> fileSystemRootNodes = model.getFileSystemRootNodes();
        Assertions.assertFalse(fileSystemRootNodes.isEmpty());
        Assertions.assertEquals(fileSystemRootNodes.size(), model.getChildCount(model.getFileSystemNode()));
        for (DirectoryNode fileSystemRootNode : fileSystemRootNodes) {
            Assertions.assertEquals(0, fileSystemRootNode.getDirectory().getNameCount());
        }
    }
}
