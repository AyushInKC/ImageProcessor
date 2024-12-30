package com.JavaJunkie.ImageProcessor.Services;

import com.JavaJunkie.ImageProcessor.DTO.UserLoginDTO;
import com.JavaJunkie.ImageProcessor.DTO.UserRegisterRequestDTO;
import com.JavaJunkie.ImageProcessor.Entity.UserEntity;
import com.JavaJunkie.ImageProcessor.Respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserEntity registerUser(UserRegisterRequestDTO userRegisterRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegisterRequestDTO.getUsername());
        userEntity.setPassword(userRegisterRequestDTO.getPassword());
        return userRepository.save(userEntity);
    }

    public String loginUser(UserLoginDTO userLoginDTO) {

        UserEntity user = userRepository.findByUsername(userLoginDTO.getUsername());
        if (user == null) {
            return "User not found!";
        }
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            return "Invalid password!";
        }
        return "Login successful!";
    }
}
