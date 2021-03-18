package org.swingk.io.dirtree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DirTreeUtilsTest {
    @Test
    public void NAME_COMPARATOR() {
        Path p1 = Path.of("m1");
        Path p2 = Path.of("a1");
        Path p3 = Path.of("Z1");
        Path p4 = Path.of("y1");
        List<Path> paths = new ArrayList<>(List.of(p1, p2, p3, p4));
        paths.sort(DirTreeUtils.NAME_COMPARATOR);
        Assertions.assertEquals(List.of(p2, p1, p4, p3), paths);
    }
}
