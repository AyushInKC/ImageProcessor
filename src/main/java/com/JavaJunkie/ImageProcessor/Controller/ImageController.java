package com.JavaJunkie.ImageProcessor.Controller;
import com.JavaJunkie.ImageProcessor.Entity.ImageEntity;
import com.JavaJunkie.ImageProcessor.Respository.ImageRepository;
import com.JavaJunkie.ImageProcessor.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @PostMapping("/rotateImg")
    public ResponseEntity<byte[]> rotateImage(@RequestParam("file") MultipartFile file,@RequestParam("angle") double angle) throws IOException {
        byte[] rotatedImage = imageService.rotateImage(file, angle);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/png");
        return new ResponseEntity<>(rotatedImage, headers, HttpStatus.OK);
    }

    @PostMapping("/cropImg")
    public ResponseEntity<byte[]> cropImage(@RequestParam("file") MultipartFile file,@RequestParam("x") int x,@RequestParam("x") int y,@RequestParam("height") int height,@RequestParam("width") int width) throws IOException {
        byte[] croppedImage = imageService.cropImage(file, x, y, height, width);
       return  ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(croppedImage);
    }

    @PostMapping("/addWaterMark")
    public ResponseEntity<byte[]> addWaterMark(@RequestParam("file") MultipartFile file,@RequestParam("title") String title){
        byte[] waterMarkedImage=imageService.addWaterMark(file,title);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(waterMarkedImage);
    }


    @PostMapping("/convertToJPG")
    public ResponseEntity<byte[]> convertToJPG(@RequestParam("file") MultipartFile file){
        byte[] JPGImg=imageService.convertToJPG(file);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(JPGImg);
    }

    @PostMapping("/convertImageToPDF")
    public ResponseEntity<byte[]> convertImageToPDF(MultipartFile file) throws IOException {
        byte[] pdfBytes = imageService.convertImageToPDF(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=image.pdf");
        headers.add("Content-Type", "application/pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @PostMapping("/addGrayScaleFilter")
    public ResponseEntity<byte[]> addGrayScaleFilter(MultipartFile file) throws IOException {
        byte [] filteredImg=imageService.addGrayScaleFilter(file);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(filteredImg);
    }

    @PostMapping("/addSepiaFilter")
    public ResponseEntity<byte[]> addSepiaFilter(MultipartFile file) throws IOException {
        byte [] filteredImg=imageService.addSepiaFilter(file);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(filteredImg);
    }

    @PostMapping("/mirrorImage")
    public ResponseEntity<byte[]> mirrorImage(@RequestParam("file") MultipartFile file,@RequestParam("direction") String direction) throws IOException{
        byte[] mirroredImg = imageService.mirrorImage(file, direction);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(mirroredImg);
    }

    @PostMapping("/compressImage")
    public ResponseEntity<byte[]> compressImage(@RequestParam("file") MultipartFile file,@RequestParam("size") float size) throws IOException {
        byte[] compressedImg = imageService.compressImage(file,size);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(compressedImg);
    }

    @PostMapping("/blurImage")
    public ResponseEntity<byte[]> blurImage(@RequestParam("file") MultipartFile file) throws IOException{
        byte[] blurredImage=imageService.blurImage(file);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(blurredImage);
    }

    @PostMapping("/tintImage")

    public ResponseEntity<byte[]> tintImage(@RequestParam("file") MultipartFile file ,@RequestParam("r") String red,
                                           @RequestParam("g") String green,@RequestParam("b") String blue){
        byte[] tintedImage= imageService.tintImage(file,red,green,blue);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(tintedImage);
    }

}

