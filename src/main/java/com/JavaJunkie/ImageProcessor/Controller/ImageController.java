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
import java.util.List;

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
    @GetMapping("/getImgData/{id}")
    public ResponseEntity<?> getImageMetadata(@PathVariable String id) {
        ImageEntity metadata = imageService.getImageMetadataById(id);
        return ResponseEntity.ok(metadata);
    }

    @DeleteMapping("/deleteImg/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable String id){
     imageService.deleteImage(id);
     return ResponseEntity.ok("Image deleted Successfully");
    }

    @PutMapping("/updateImgData/{id}")
     public ResponseEntity<ImageEntity> updateImageData(@PathVariable String id,@RequestBody ImageEntity updatedImg){
       ImageEntity updated=imageService.updateImageData(id,updatedImg.getName(),updatedImg.getContentType());
      return ResponseEntity.ok(updated);
    }

    @PutMapping("/updateImgFile/{id}")
    public ResponseEntity<String> updateImageFile(@PathVariable String id,@RequestParam("file") MultipartFile file){
        try{
            boolean isUpdated=imageService.updateImageFile(id,file);
         if(isUpdated){
             return ResponseEntity.ok("Image updated successfully!!!");
         }
         else {
             return ResponseEntity.notFound().build();
         }
        }catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error updating image: " + e.getMessage());
        }
    }
    @GetMapping("/listAll")
    public ResponseEntity<List<ImageEntity>> listImages(){
    List<ImageEntity> images=imageService.getAllImages();
    return ResponseEntity.ok(images);
    }
    @PostMapping(value = "/resizeImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> resizeImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("width") int width,
                                              @RequestParam("height") int height) {
        try {
            return imageService.resizeImage(file, width, height);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to resize image: " + e.getMessage());
        }
    }
}
