package com.decagon.queuepay.controller;

import com.decagon.queuepay.apiresponse.ApiResponse;
import com.decagon.queuepay.models.user.User;
import com.decagon.queuepay.payload.LoginRequest;
import com.decagon.queuepay.payload.SignupRequest;
import com.decagon.queuepay.response.JwtResponse;
import com.decagon.queuepay.service.MapValidationErrorService;
import com.decagon.queuepay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private UserService userService;
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    public UserController(UserService userService, MapValidationErrorService mapValidationErrorService) {
        this.userService = userService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignupRequest signupRequest, BindingResult bindingResult) throws Exception {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
        if (errorMap != null){
            return errorMap;
        }
        User createdUser = userService.registration(signupRequest);
        ApiResponse<User> response = new ApiResponse<>(HttpStatus.CREATED);
        response.setData(createdUser);
        response.setMessage("Registration successful!");
        return new ResponseEntity<>(response, response.getStatus());
    }

//    @PatchMapping("verifyEmail/{token}")
//    public ResponseEntity<Response<String>> verifyRegistration(@PathVariable String token) throws Exception {
//        userService.verifyRegistration(token);
//        Response<String> response = new Response<>(HttpStatus.ACCEPTED);
//        response.setMessage("You are now a verified user");
//        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) throws Exception {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
        if (errorMap != null){
            return errorMap;
        }
        JwtResponse loginResponse = userService.authenticate(loginRequest);
        ApiResponse<JwtResponse> response = new ApiResponse<>(HttpStatus.OK);
        response.setData(loginResponse);
        response.setMessage("Login successful!");
        return new ResponseEntity<>(response, response.getStatus());
    }

}
