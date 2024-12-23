package com.JavaJunkie.ImageProcessor.Services;
import com.JavaJunkie.ImageProcessor.Entity.ImageEntity;
import com.JavaJunkie.ImageProcessor.Respository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@Component
@Service
public class ImageService{
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

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
}