package org.acme.dto;

public class UploadImagemDTO {
    private String fileName;
    private String contentType;
    private byte[] fileData;

    // Construtores
    public UploadImagemDTO() {}

    public UploadImagemDTO(String fileName, String contentType, byte[] fileData) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileData = fileData;
    }

    // Getters e Setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
}