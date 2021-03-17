![Java CI with Maven](https://github.com/parubok/directory-tree/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/parubok/directory-tree/blob/master/LICENSE)

# directory-tree

The goal of this project is to provide an efficient and convenient implementation of Java Swing JTree model with
a hierarchy of a local filesystem directories (e.g. for directory chooser component).

Note: Though the model itself does not provide any builtin support for asynchronous operations, it can be built and 
prepopulated (via `DirTreeModel.getTreePath`) on any thread and then passed to the EDT. 

Example:
```java
import org.swingk.io.dirtree.DirTreeModel;
import org.swingk.io.dirtree.DefaultNodeFactory;
import org.swingk.io.dirtree.DirTreeUtils;

import javax.swing.JTree;

var model = new DirTreeModel<>(DirTreeUtils.NAME_COMPARATOR, true, true, new DefaultNodeFactory());
JTree tree = ...;
DirTreeUtils.configureTree(tree, model);
```

A demo application is provided. See `org.swingk.io.dirtree.demo.Demo`.

This library is packaged as a Java 9 module `org.swingk.io.dirtree` (with a single dependency on a system module `java.desktop`).

This project has no external dependencies (except JUnit 5, for testing).

Requires Java 11 or later.
