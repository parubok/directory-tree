package org.swingk.io.dirtree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DirectoryTreeModel implements TreeModel {

    public static final Comparator<Path> NAME_COMPARATOR = Comparator
            .comparing(path -> DirectoryNode.getName(path).toLowerCase());

    // private static final Executor executor = Executors.newSingleThreadExecutor();

    private final DirectoryNodeFactory nodeFactory;
    private final DirectoryNode root;
    private final boolean showHidden;
    private final boolean showSystem;
    private final Comparator<Path> pathComparator;
    private final Map<DirectoryNode, Boolean> leafStatus = new ConcurrentHashMap<>();
    private final Set<DirectoryNode> populated = ConcurrentHashMap.newKeySet();

    public DirectoryTreeModel() {
        this(NAME_COMPARATOR, false, false, new DefaultNodeFactory());
    }

    public DirectoryTreeModel(Comparator<Path> pathComparator, boolean showHidden, boolean showSystem,
                              DirectoryNodeFactory nodeFactory) {
        this.nodeFactory = Objects.requireNonNull(nodeFactory);
        this.pathComparator = Objects.requireNonNull(pathComparator);
        this.showHidden = showHidden;
        this.showSystem = showSystem;
        this.root = Objects.requireNonNull(nodeFactory.createRootNode());
        init();
    }

    private void init() {
        FileSystem fs = FileSystems.getDefault();
        var fsNode = nodeFactory.createFileSystemNode(fs);
        root.add(fsNode);
        var rootDirs = new ArrayList<Path>();
        fs.getRootDirectories().forEach(rootDirs::add);
        rootDirs.sort(getPathComparator());
        rootDirs.forEach(rootDir -> fsNode.add(nodeFactory.createDirectoryNode(rootDir)));
        leafStatus.put(root, Boolean.FALSE);
        populated.add(root);
        leafStatus.put(fsNode, rootDirs.isEmpty());
        populated.add(fsNode);
    }

    public Comparator<Path> getPathComparator() {
        return pathComparator;
    }

    @Override
    public DirectoryNode getRoot() {
        return root;
    }

    @Override
    public DirectoryNode getChild(Object parent, int index) {
        DirectoryNode dirNode = (DirectoryNode) parent;
        ensurePopulated(dirNode);
        return dirNode.getChildAt(index);
    }

    private DirectoryStream<Path> newDirectoryStream(Path dir) throws IOException {
        return Files.newDirectoryStream(dir, path -> {
            DosFileAttributes attrs;
            try {
                attrs = Files.readAttributes(path, DosFileAttributes.class);
            } catch (IOException ex) {
                return false;
            }
            return attrs.isDirectory() && (isShowHidden() || !attrs.isHidden()) && (isShowSystem() || !attrs.isSystem());
        });
    }

    private void ensurePopulated(DirectoryNode node) {
        if (!populated.contains(node)) {
            Path dir = node.getDirectory();
            List<Path> children;
            try (DirectoryStream<Path> dirStream = newDirectoryStream(dir)) {
                children = new ArrayList<>();
                dirStream.forEach(children::add);
                children.sort(pathComparator);
            } catch (IOException e) {
                children = Collections.emptyList();
            }
            leafStatus.put(node, children.isEmpty());
            children.forEach(childPath -> node.add(nodeFactory.createDirectoryNode(childPath)));
            populated.add(node);
        }
    }

    @Override
    public int getChildCount(Object parent) {
        DirectoryNode dirNode = (DirectoryNode) parent;
        if (Boolean.TRUE.equals(leafStatus.get(dirNode))) {
            return 0;
        }
        ensurePopulated(dirNode);
        return dirNode.getChildCount();
    }

    private boolean computeLeafStatus(DirectoryNode node) {
        boolean leaf;
        try (DirectoryStream<Path> dirStream = newDirectoryStream(node.getDirectory())) {
            leaf = !dirStream.iterator().hasNext();
        } catch (IOException e) {
            leaf = true;
        }
        return leaf;
    }

    @Override
    public boolean isLeaf(Object node) {
        DirectoryNode dirNode = (DirectoryNode) node;
        return leafStatus.computeIfAbsent(dirNode, this::computeLeafStatus);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        DirectoryNode dirNode = (DirectoryNode) parent;
        ensurePopulated(dirNode);
        return dirNode.getIndex((DirectoryNode) child);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }

    public boolean isShowHidden() {
        return showHidden;
    }

    public boolean isShowSystem() {
        return showSystem;
    }
}
