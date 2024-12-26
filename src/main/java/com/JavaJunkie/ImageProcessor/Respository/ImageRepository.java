package com.JavaJunkie.ImageProcessor.Respository;
import com.JavaJunkie.ImageProcessor.Entity.ImageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ImageRepository extends MongoRepository<ImageEntity,String> {

}
