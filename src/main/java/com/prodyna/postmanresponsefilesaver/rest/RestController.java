package com.prodyna.postmanresponsefilesaver.rest;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
public class RestController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }


    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public UploadStatus upload(@RequestParam("filename") String filename, @RequestBody String body) {
        File file = new File(System.getProperty("user.home") + "/Downloads/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + "_" + filename);

        String base64 = body.substring(body.indexOf(",") + 2);
        byte[] decodedData = java.util.Base64.getDecoder().decode(base64);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedData);
            fos.flush();
        }
        log.warn("Saved to File: {}", file.getAbsolutePath());
        return UploadStatus.builder().filename(file.getAbsolutePath()).build();

    }


}
