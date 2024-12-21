package com.JavaJunkie.ImageProcessor.Respository;

import com.JavaJunkie.ImageProcessor.Entity.ImageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.net.UnknownServiceException;
import java.util.Optional;

public interface ImageRepository extends MongoRepository<ImageEntity,String> {
}
