//package com.JavaJunkie.ImageProcessor.Services;
//import com.JavaJunkie.ImageProcessor.DTO.UserLoginRequestDTO;
//import com.JavaJunkie.ImageProcessor.DTO.UserLoginResponseDTO;
//import com.JavaJunkie.ImageProcessor.DTO.UserRegisterRequestDTO;
//import com.JavaJunkie.ImageProcessor.Entity.UserEntity;
//import com.JavaJunkie.ImageProcessor.Respository.UserRepository;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//@Service
//@RequiredArgsConstructor
//@AllArgsConstructor
//@NoArgsConstructor
//public class UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//    public UserEntity  registerUser(UserRegisterRequestDTO userRequestDTO){
//        Optional<UserEntity> existingUsername=userRepository.findByUsername(userRequestDTO.getUsername());
//        if(existingUsername.isPresent()){
//            throw new RuntimeException("Enter a unique Username!!!");
//        }
//        Optional<UserEntity> existingEmail=userRepository.findByGmail(userRequestDTO.getGmail());
//        if(existingEmail.isPresent()){
//            throw new RuntimeException("User with these email already exists!!!");
//        }
//        UserEntity userEntity=new UserEntity();
//        userEntity.setGmail(userRequestDTO.getGmail());
//        userEntity.setUsername(userRequestDTO.getUsername());
//        userEntity.setPassword(userRequestDTO.getPassword());
//        return userRepository.save(userEntity);
//    }
//
//
//    public UserLoginResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO){
//        Optional<UserEntity>  user= userRepository.findByUsername(userLoginRequestDTO.getUsername());
//
//        if(user.isPresent()){
//
//        }
//        return null;
//    }
//}
