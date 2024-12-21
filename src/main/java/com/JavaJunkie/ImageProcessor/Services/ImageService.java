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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
//        String filePath = "C:/uploads" + file.getOriginalFilename();
//        File uploadDir = new File("uploads/");
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }
//        file.transferTo(new java.io.File(filePath));
//        ImageEntity image = new ImageEntity();
//        image.setName(file.getOriginalFilename());
//        image.setContentType(file.getContentType());
//        image.setImageData(file.getBytes());
//        imageRepository.save(image);
//
////        return filePath;
//
//        InputStream inputStream = file.getInputStream(); // Read file content as InputStream
//
//        ObjectId fileId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType());
//
//        return fileId.toString();

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
}