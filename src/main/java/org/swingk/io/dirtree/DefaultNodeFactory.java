package org.swingk.io.dirtree;

import java.nio.file.FileSystem;
import java.nio.file.Path;

public class DefaultNodeFactory implements DirNodeFactory<DefaultDirNode> {
    @Override
    public DefaultDirNode createRootNode() {
        return new DefaultDirNode();
    }

    @Override
    public DefaultDirNode createFileSystemNode(FileSystem fs) {
        return new DefaultDirNode(fs);
    }

    @Override
    public DefaultDirNode createDirectoryNode(Path dir) {
        return new DefaultDirNode(dir);
    }
}
