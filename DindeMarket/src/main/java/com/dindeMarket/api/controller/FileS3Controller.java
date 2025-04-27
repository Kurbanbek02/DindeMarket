package com.dindeMarket.api.controller;
import com.dindeMarket.config.s3service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dindemarket/file")
@CrossOrigin
public class FileS3Controller {

    @Autowired
    private FileService fileService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file){
        String URL = fileService.uploadFile(file);
        Map<String,String> response = new HashMap<>();
        response.put("URL",URL);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    @GetMapping("/download/{fileName}")
//    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName){
//        byte[] data = fileService.downloadFile(fileName);
//        ByteArrayResource resource = new ByteArrayResource(data);
//        return ResponseEntity
//                .ok()
//                .contentLength(data.length)
//                .header("Content-type", "application/octet-stream")
//                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
//                .body(resource);
//    }
//
//    @DeleteMapping("/delete/{fileName}")
//    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
//        return new ResponseEntity<>(fileService.deleteFile(fileName), HttpStatus.OK);
//    }
}
