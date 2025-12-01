package org.acme.dto;

import org.jboss.resteasy.annotations.providers.multipart.PartType;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

public class UploadImagemDTO {
    
    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    private String fileName;
    
    @FormParam("contentType")
    @PartType(MediaType.TEXT_PLAIN)
    private String contentType;
    
    @FormParam("fileData")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
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