package org.swingk.io.dirtree.demo;

import org.swingk.io.dirtree.DefaultNodeFactory;
import org.swingk.io.dirtree.DirTreeModel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class Demo {

    private Demo() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTree tree = new JTree();
        tree.setModel(new DirTreeModel<>(new DefaultNodeFactory()));
        JScrollPane sp = new JScrollPane();
        sp.setViewportView(tree);
        contentPanel.add(sp, BorderLayout.CENTER);

        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(contentPanel);
        frame.setSize(1200, 600);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Demo::new);
    }
}
