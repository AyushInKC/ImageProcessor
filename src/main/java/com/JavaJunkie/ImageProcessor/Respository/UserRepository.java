package com.JavaJunkie.ImageProcessor.Respository;

import com.JavaJunkie.ImageProcessor.Entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
}
