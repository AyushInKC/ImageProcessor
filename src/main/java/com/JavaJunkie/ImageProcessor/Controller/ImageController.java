package com.JavaJunkie.ImageProcessor.Controller;

import com.JavaJunkie.ImageProcessor.Entity.ImageEntity;
import com.JavaJunkie.ImageProcessor.Respository.ImageRepository;
import com.JavaJunkie.ImageProcessor.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/img/")
public class ImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;
    @PostMapping("/uploadImg")

    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            String fileId = imageService.uploadImage(file);
            return ResponseEntity.ok("File uploaded successfully: " + fileId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }

    @GetMapping("getImg/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        byte[] imageData = imageService.getImageById(id);


        ImageEntity metadata = imageService.getImageMetadataById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(metadata.getContentType()))
                .body(imageData);
    }
    @GetMapping("/getMetadata/{id}")
    public ResponseEntity<?> getImageMetadata(@PathVariable String id) {
        ImageEntity metadata = imageService.getImageMetadataById(id);
        return ResponseEntity.ok(metadata);
    }
}
