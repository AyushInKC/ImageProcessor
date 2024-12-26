package com.JavaJunkie.ImageProcessor.Services;
import com.JavaJunkie.ImageProcessor.Entity.ImageEntity;
import com.JavaJunkie.ImageProcessor.Respository.ImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class ImageService{
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public String uploadImage(MultipartFile file) throws IOException {
        ImageEntity image = new ImageEntity();
        image.setName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setImageData(file.getBytes());
        ImageEntity savedImage = imageRepository.save(image);
        return savedImage.getId();
    }
    public byte[] getImageById(String id){
        Optional<ImageEntity> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            return imageOptional.get().getImageData();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        }
    }
    public ImageEntity getImageMetadataById(String id) {
        return imageRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }

    public void deleteImage(String id){
         if(imageRepository.existsById(id)){
                imageRepository.deleteById(id);
                System.out.println("Image deleted Successfully!!!");
         }
         else{
             throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Image not found!!!");
         }
    }

    public ImageEntity updateImageData(String id, String newName, String newContentType){
     Optional<ImageEntity> imageEntityOptional =imageRepository.findById(id);
     if(imageEntityOptional.isPresent()){
         ImageEntity image=imageEntityOptional.get();
         image.setName(newName);
         image.setContentType(newContentType);
         return imageRepository.save(image);
     }
     else {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Image not Found!!!");
     }
    }

    public List<ImageEntity> getAllImages() {
        return imageRepository.findAll();
    }
    public boolean updateImageFile(String id, MultipartFile file) throws IOException {
          ImageEntity existingImage=imageRepository.findById(id).orElse(null);
          if(existingImage!=null){
              existingImage.setName(file.getOriginalFilename());
              existingImage.setContentType(file.getContentType());
              existingImage.setImageData(file.getBytes());
              imageRepository.save(existingImage);
              return true;
          }
          return false;
    }

    public ResponseEntity<byte[]> resizeImage(MultipartFile file, int width, int height) throws IOException {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive integers.");
        }

        InputStream inputStream = file.getInputStream();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(inputStream)
                .size(width, height)
                .outputFormat("png")
                .toOutputStream(outputStream);
        BufferedImage bufferedImage = Thumbnails.of(new ByteArrayInputStream(outputStream.toByteArray()))
                .size(width, height)
                .asBufferedImage();

        System.out.println("Resized width: " + bufferedImage.getWidth() + " , Resized height: " + bufferedImage.getHeight());

        byte[] resizedImageBytes = outputStream.toByteArray();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resizedImageBytes);
    }
    public byte[] rotateImage(MultipartFile file, double angle) throws IOException{
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = rotatedImage.createGraphics();
        graphics2D.rotate(Math.toRadians(angle), width / 2, height / 2);
        graphics2D.drawImage(originalImage, 0, 0, null);
        graphics2D.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(rotatedImage, "PNG", baos);
        return baos.toByteArray();
}

public byte[] cropImage(MultipartFile file,int x,int y,int height,int width) throws IOException {
    BufferedImage originalImage = ImageIO.read(file.getInputStream());

    BufferedImage croppedImage = originalImage.getSubimage(x, y, width, height);

    System.out.println("Cropped Image Size: " + croppedImage.getWidth() + "x" + croppedImage.getHeight());

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(croppedImage, "PNG", baos);
    return baos.toByteArray();
}
}
