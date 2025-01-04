package com.JavaJunkie.ImageProcessor.Services;

import com.JavaJunkie.ImageProcessor.DTO.UserLoginDTO;
import com.JavaJunkie.ImageProcessor.DTO.UserRegisterRequestDTO;
import com.JavaJunkie.ImageProcessor.Entity.UserEntity;
import com.JavaJunkie.ImageProcessor.Respository.UserRepository;
import com.JavaJunkie.ImageProcessor.Utils.JWTUtility;
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
    @Autowired
    private JWTUtility jwtUtility;
    public boolean usernameExists(String username) {
        UserEntity existingUser = userRepository.findByUsername(username);
        return existingUser != null;
    }

    // Register a new user
    public UserEntity registerUser(UserRegisterRequestDTO userRegisterRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegisterRequestDTO.getUsername());
        userEntity.setPassword(userRegisterRequestDTO.getPassword());
        return userRepository.save(userEntity);
    }

    public String loginUser(UserLoginDTO userLoginDTO) {
        try {
            UserEntity user = userRepository.findByUsername(userLoginDTO.getUsername());
            if (user == null) {
                return "User not found!";
            }

            if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
                return "Invalid password!";
            }

            String accessToken = jwtUtility.generateAccessToken(user.getUsername());
            String refreshToken = jwtUtility.generateRefreshToken(user.getUsername());

            return "Login successful! \n Access Token: "+accessToken + " \nRefresh Token: " + refreshToken;
        } catch (Exception e) {
            e.printStackTrace();
            return "Login failed due to an error.";
        }
    }


}
