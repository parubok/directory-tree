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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * Tree model populated with a hierarchy of directories in the local filesystem.
 *
 * @param <T> Type of nodes in this model.
 * @implNote The model is populated incrementally, as the user expands the tree nodes.
 * @see DirNodeFactory
 */
public class DirTreeModel<T extends DirNode<T>> implements TreeModel {

    /**
     * Default comparator - compares directory names in case insensitive fashion.
     */
    public static final Comparator<Path> NAME_COMPARATOR = Comparator
            .comparing(path -> DirNode.getName(path).toLowerCase());

    private final DirNodeFactory<T> nodeFactory;
    private final T root;
    private final DirectoryStream.Filter<Path> filter;
    private final Comparator<Path> pathComparator;
    private final Map<T, Boolean> leafStatus = new ConcurrentHashMap<>();
    private final Set<T> populated = ConcurrentHashMap.newKeySet();

    /**
     * Constructor.
     */
    public DirTreeModel(DirNodeFactory<T> nodeFactory) {
        this(NAME_COMPARATOR, false, false, nodeFactory);
    }

    /**
     * Constructor.
     */
    public DirTreeModel(Comparator<Path> pathComparator, boolean showHidden, boolean showSystem,
                        DirNodeFactory<T> nodeFactory) {
        this.nodeFactory = requireNonNull(nodeFactory);
        this.pathComparator = requireNonNull(pathComparator);
        this.root = nodeFactory.createRootNode();

        this.filter = path -> {
            if (path.getNameCount() == 0) {
                return true; // filesystem root always passes the filter
            }
            DosFileAttributes attrs;
            try {
                attrs = Files.readAttributes(path, DosFileAttributes.class);
            } catch (IOException ex) {
                return false;
            }
            return attrs.isDirectory() && (showHidden || !attrs.isHidden()) && (showSystem || !attrs.isSystem());
        };

        FileSystem fs = FileSystems.getDefault();
        var fsNode = nodeFactory.createFileSystemNode(fs);
        root.add(fsNode);
        var rootDirs = new ArrayList<Path>();
        fs.getRootDirectories().forEach(rootDirs::add);
        rootDirs.sort(pathComparator);
        rootDirs.forEach(rootDir -> fsNode.add(nodeFactory.createDirectoryNode(rootDir)));
        leafStatus.put(root, Boolean.FALSE);
        populated.add(root);
        leafStatus.put(fsNode, rootDirs.isEmpty());
        populated.add(fsNode);
    }

    /**
     * @return Filesystem node. Created via to call to {@link DirNodeFactory#createFileSystemNode(FileSystem)}.
     */
    public T getFileSystemNode() {
        return root.getChildAt(0);
    }

    /**
     * @see #getFileSystemNode()
     */
    public TreePath getFileSystemNodeTreePath() {
        return new TreePath(new Object[]{root, root.getChildAt(0)});
    }

    private static List<Path> getAllParents(Path directory) {
        List<Path> parents = new ArrayList<>();
        Path parent = directory;
        while (parent != null) {
            parents.add(parent);
            parent = parent.getParent();
        }
        Collections.reverse(parents); // so the root is first
        return parents;
    }

    /**
     * @param directory Local filesystem directory. Must be absolute as specified by {@link Path#isAbsolute()}.
     * @return {@link Optional} with the tree path of the specified directory or empty {@link Optional} if the
     * directory is not in the model (e.g. doesn't exist, doesn't pass the filter, etc.).
     */
    public Optional<TreePath> getTreePath(Path directory) {
        requireNonNull(directory);
        if (!directory.isAbsolute()) {
            throw new IllegalArgumentException("The directory path must be absolute.");
        }
        try {
            if (!filter.accept(directory)) {
                return Optional.empty();
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        final List<Path> parents = getAllParents(directory);
        final int size = parents.size();
        List<T> treePathNodes = new ArrayList<>(size + 2);
        treePathNodes.add(getRoot());
        treePathNodes.add(getFileSystemNode());
        T currentNode = getFileSystemNode();  // start with filesystem node
        for (int i = 0; i < size; i++) {
            T node = null;
            for (int j = 0; j < currentNode.getChildCount(); j++) {
                T childNode = currentNode.getChildAt(j);
                if (childNode.getDirectory().equals(parents.get(i))) {
                    node = childNode;
                    break;
                }
            }
            if (node == null) {
                return Optional.empty();
            }
            treePathNodes.add(node);
            if (i == (size - 1)) {
                return Optional.of(new TreePath(treePathNodes.toArray()));
            }
            ensurePopulated(node);
            currentNode = node;
        }
        return Optional.empty();
    }

    /**
     * @return Model root node. Created via to call to {@link DirNodeFactory#createRootNode()}.
     */
    @Override
    public T getRoot() {
        return root;
    }

    @Override
    public T getChild(Object parent, int index) {
        T dirNode = (T) parent;
        ensurePopulated(dirNode);
        return dirNode.getChildAt(index);
    }

    private DirectoryStream<Path> newDirectoryStream(Path dir) throws IOException {
        return Files.newDirectoryStream(dir, filter);
    }

    private void ensurePopulated(T node) {
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
        T dirNode = (T) parent;
        if (Boolean.TRUE.equals(leafStatus.get(dirNode))) {
            return 0;
        }
        ensurePopulated(dirNode);
        return dirNode.getChildCount();
    }

    private boolean computeLeafStatus(T node) {
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
        T dirNode = (T) node;
        return leafStatus.computeIfAbsent(dirNode, this::computeLeafStatus);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        T dirNode = (T) parent;
        ensurePopulated(dirNode);
        return dirNode.getIndex((T) child);
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
}
