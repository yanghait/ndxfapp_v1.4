package com.ynzhxf.nd.xyfirecontrolapp.bean.share;

public class FileUpdateProgressBean {

    private int status;
    private int progress;
    private String fileName;
    private int fileLength;

    private String url;
    private Object task;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getTask() {
        return task;
    }

    public void setTask(Object task) {
        this.task = task;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    @Override
    public String toString() {
        return "FileUpdateProgressBean{" +
                "status=" + status +
                ", progress=" + progress +
                ", fileName='" + fileName + '\'' +
                ", fileLength=" + fileLength +
                ", url='" + url + '\'' +
                ", task=" + task +
                '}';
    }
}
