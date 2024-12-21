//package com.JavaJunkie.ImageProcessor.Controller;
//
//
//import com.JavaJunkie.ImageProcessor.DTO.UserLoginRequestDTO;
//import com.JavaJunkie.ImageProcessor.DTO.UserRegisterRequestDTO;
//import com.JavaJunkie.ImageProcessor.Entity.UserEntity;
//import com.JavaJunkie.ImageProcessor.Services.UserService;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/user")
//public class UserController {
//
//    private  UserService userService;
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDTO userRequestDTO){
//     try{
//         UserEntity createdUser=userService.registerUser(userRequestDTO);
//       return new ResponseEntity<>(createdUser, HttpStatus.OK);
//     }
//     catch (RuntimeException e){
//      return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
//     }
//     catch (Exception e){
//        return new ResponseEntity<>("An error occurred during the registration!",HttpStatus.INTERNAL_SERVER_ERROR);
//     }
//    }
//
//
//    @PostMapping("/login")
//     public ResponseEntity<?> loginUser(@RequestBody UserLoginRequestDTO userLoginRequestDTO){
//
//    return null;
//    }
//}
