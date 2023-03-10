package com.tcf.sma.Survey.model;

public class MediaModel {

    int project_id;
    String filename, filecode, fileType, uploadedon;

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilecode() {
        return filecode;
    }

    public void setFilecode(String filecode) {
        this.filecode = filecode;
    }

    public String getUploadedon() {
        return uploadedon;
    }

    public void setUploadedon(String uploadedon) {
        this.uploadedon = uploadedon;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


}
