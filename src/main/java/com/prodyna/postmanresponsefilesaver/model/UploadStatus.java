package com.prodyna.postmanresponsefilesaver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadStatus {
    private String filename;
}
