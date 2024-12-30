package com.JavaJunkie.ImageProcessor.Controller;

import com.JavaJunkie.ImageProcessor.DTO.UserLoginDTO;
import com.JavaJunkie.ImageProcessor.DTO.UserRegisterRequestDTO;
import com.JavaJunkie.ImageProcessor.Entity.UserEntity;
import com.JavaJunkie.ImageProcessor.Respository.UserRepository;
import com.JavaJunkie.ImageProcessor.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/auth/")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        try {
            userRegisterRequestDTO.setPassword(passwordEncoder.encode(userRegisterRequestDTO.getPassword()));
            UserEntity createdUser = userService.registerUser(userRegisterRequestDTO);

            log.info("User created successfully: {}", createdUser);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            log.error("User registration failed - Duplicate entry: {}", e.getMessage());
            return new ResponseEntity<>("Username already registered!", HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("User registration failed: {}", e.getMessage());
            return new ResponseEntity<>("Registration failed due to an error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO){
        try {
            String loginResponse = userService.loginUser(userLoginDTO);
            if (loginResponse.equals("Login successful!")) {
                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>("Login failed due to an error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
