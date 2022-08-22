package com.prodyna.postmanresponsefilesaver.rest;

import com.prodyna.postmanresponsefilesaver.config.ApplicationConfigProperties;
import com.prodyna.postmanresponsefilesaver.model.FileNotAvailableException;
import com.prodyna.postmanresponsefilesaver.model.InvalidFileTypeException;
import com.prodyna.postmanresponsefilesaver.model.UploadStatus;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@org.springframework.web.bind.annotation.RestController
@AllArgsConstructor
@Slf4j
@Validated
public class RestController {



    private final ApplicationConfigProperties config;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }


    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_PDF_VALUE)
    @SneakyThrows
    public UploadStatus upload(@RequestParam("filename") @NotEmpty String filename, @RequestParam(defaultValue = "false") boolean withTimestamp,  @RequestBody @NotEmpty String body) {

        if(!filename.endsWith(".pdf")){
            log.warn("File is not a PDF: {}", filename);
            throw new InvalidFileTypeException("Filename is not postfixed with .pdf");
        }

        File file2Upload = getFile(filename, withTimestamp);
        saveFile(body, file2Upload);
        return UploadStatus.builder().filename(file2Upload.getAbsolutePath()).build();
    }

    private void saveFile(String body, File file2Upload) throws IOException {
        String base64 = body.substring(body.indexOf(",") + 2);
        byte[] decodedData = java.util.Base64.getDecoder().decode(base64);
        try (FileOutputStream fos = new FileOutputStream(file2Upload)) {
            fos.write(decodedData);
            fos.flush();
        }
        log.info("Saved to File: {}", file2Upload.getAbsolutePath());
    }

    private File getFile(final String filename, boolean withTimestamp) {
        String name = filename.replaceAll("[^a-zA-Z0-9]", "-");

        String timestamp = withTimestamp ? LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + "_" : "";
        return new File(config.getContentDir() + FileSystems.getDefault().getSeparator() + timestamp + name);
    }


    @GetMapping(path="/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public  ResponseEntity<InputStreamResource> downloadDocument(@NotEmpty String filename) throws IOException {
        if(!filename.endsWith(".pdf")){
            log.warn("File is not a PDF: {}", filename);
            throw new InvalidFileTypeException("Filename is not postfixed with .pdf");
        }
        File file2Download = new File(config.getContentDir() + FileSystems.getDefault().getSeparator()+ filename);
        if(!file2Download.exists()){
            log.warn("File not found: {}", file2Download.getAbsolutePath());
            throw new FileNotAvailableException(filename);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file2Download));

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
    }


}
