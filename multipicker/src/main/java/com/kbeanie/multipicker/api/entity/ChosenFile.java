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

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
