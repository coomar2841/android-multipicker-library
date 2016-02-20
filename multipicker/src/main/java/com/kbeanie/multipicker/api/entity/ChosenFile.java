package com.kbeanie.multipicker.api.entity;

import java.util.Date;

/**
 * Created by kbibek on 2/20/16.
 */
public class ChosenFile {
    private long id;
    private String queryUri;
    private String originalPath;
    private String mimeType;
    private long size;
    private String extension;
    private Date createdAt;
    private String type;

    private String directoryType;

    public long getId() {
        return id;
    }

    public String getDirectoryType() {
        return directoryType;
    }

    public void setDirectoryType(String directoryType) {
        this.directoryType = directoryType;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQueryUri() {
        return queryUri;
    }

    public void setQueryUri(String queryUri) {
        this.queryUri = queryUri;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileExtensionFromMimeType() {
        String extension = "";
        if (mimeType != null) {
            String[] parts = mimeType.split("//");
            if (parts.length >= 2) {
                if (parts[1] != "*") {
                    extension = "." + parts[1];
                }
            }
        }
        return extension;
    }

    private final static String STRING_FORMAT = "Type: %s, QueryUri: %s, Original Path: %s, MimeType: %s";

    @Override
    public String toString() {
        return String.format(STRING_FORMAT, directoryType, queryUri, originalPath, mimeType);
    }
}
