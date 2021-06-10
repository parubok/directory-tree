package org.swingk.io.dirtree;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

final class DirFilter implements DirectoryStream.Filter<Path> {

    /**
     * True for *NIX based, false for DOS based file system.
     */
    private final boolean isPosix;

    private final boolean showHidden;
    private final boolean showSystem;

    DirFilter(FileSystem fs, boolean showHidden, boolean showSystem) {
        // we need to determine whether we are on a posix system, else use DOS
        this.isPosix = fs.supportedFileAttributeViews().contains("posix");
        this.showHidden = showHidden;
        this.showSystem = showSystem;
    }

    @Override
    public boolean accept(Path path) {
        if (path.getNameCount() == 0) {
            return true; // filesystem root always passes the filter
        }
        if (isPosix) {
            // POSIX related filesystem checks (MacOS/*NIX)
            PosixFileAttributes attrs;
            try {
                attrs = Files.readAttributes(path, PosixFileAttributes.class);
            } catch (IOException ex) {
                return false;
            }
            return attrs.isDirectory() && (showHidden || !DirTreeUtils.getName(path).startsWith(".")) &&
                    checkPosixPermissions(attrs.permissions());
        } else {
            // DOS related filesystem permission checking
            DosFileAttributes attrs;
            try {
                attrs = Files.readAttributes(path, DosFileAttributes.class);
            } catch (IOException ex) {
                return false;
            }
            return attrs.isDirectory() && (showHidden || !attrs.isHidden()) && (showSystem || !attrs.isSystem());
        }
    }

    private boolean checkPosixPermissions(Set<PosixFilePermission> permissions) {
        // now we need to know whether there is read and execute permission on the directory
        // for us, our groups, and/or others
        return (permissions.contains(PosixFilePermission.OWNER_READ) && permissions.contains(PosixFilePermission.OWNER_EXECUTE)) ||
                (permissions.contains(PosixFilePermission.GROUP_READ) && permissions.contains(PosixFilePermission.GROUP_EXECUTE)) ||
                (permissions.contains(PosixFilePermission.OTHERS_READ) && permissions.contains(PosixFilePermission.OTHERS_EXECUTE));
    }
}
