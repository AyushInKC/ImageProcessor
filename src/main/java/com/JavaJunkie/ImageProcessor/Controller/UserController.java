package com.JavaJunkie.ImageProcessor.Controller;


import com.JavaJunkie.ImageProcessor.DTO.UserRequestDTO;
import com.JavaJunkie.ImageProcessor.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO userRequestDTO){
    }

}
