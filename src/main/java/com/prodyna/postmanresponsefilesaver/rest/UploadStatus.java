package com.prodyna.postmanresponsefilesaver.rest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadStatus {
    private String filename;
}
