package org.swingk.io.dirtree;

import javax.swing.tree.TreeNode;
import java.nio.file.Path;

/**
 * Node of {@link DirTreeModel}.
 *
 * @param <T> Type of this node and its children.
 */
public interface DirNode<T extends DirNode<T>> extends TreeNode {

	/**
	 * @return The directory {@link Path} object if the node represents a filesystem directory or
	 * {@code null} for root or file system nodes.
	 */
	Path getDirectory();

	@Override
	T getChildAt(int index);

	/**
	 * Adds child to this node.
	 */
	void add(T child);
}
