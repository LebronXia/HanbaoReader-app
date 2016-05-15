package com.example.riane.hanbaoreader_app.util;

import android.support.annotation.Nullable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Dimorinny on 24.10.15.
 */
public class FileUtils {
    public static List<File> getFileListByDirPath(String path,
                                                  @Nullable Pattern fileFilter,
                                                  boolean directoriesFilter,
                                                  boolean showHidden) {
        File directory = new File(path);
        List<File> resultFiles = new ArrayList<>();

        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {

                if (f.isDirectory() && !directoriesFilter) {
                    if (!f.isHidden() || (f.isHidden() && showHidden)) {
                        resultFiles.add(f);
                    }
                    continue;
                }

                if (fileFilter != null) {
                    if (fileFilter.matcher(f.getName()).matches())
                        if (!f.isHidden() || (f.isHidden() && showHidden)) {
                            resultFiles.add(f);
                        }
                } else {
                    resultFiles.add(f);
                }
            }
            Collections.sort(resultFiles, new FileComparator());
        }

        return resultFiles;
    }

    public static String cutLastSegmentOfPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
