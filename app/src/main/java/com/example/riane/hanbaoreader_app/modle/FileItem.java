package com.example.riane.hanbaoreader_app.modle;

import android.graphics.drawable.Drawable;

/**
 * Created by Xiamu on 2016/3/31.
 */
public class FileItem {

    String fileName;
    Drawable fileIcon;
    String filepath;
    String fileSize;
    boolean isChecked;
    boolean isImport;

    public FileItem(String fileName, Drawable fileIcon, String filepath, String fileSize) {
        this.fileName = fileName;
        this.fileIcon = fileIcon;
        this.filepath = filepath;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Drawable getFileIcon() {
        return fileIcon;
    }

    public void setFileIcon(Drawable fileIcon) {
        this.fileIcon = fileIcon;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isImport() {
        return isImport;
    }

    public void setIsImport(boolean isImport) {
        this.isImport = isImport;
    }
}
