package com.decagon.queuepay.service;

import com.decagon.queuepay.models.user.User;
import com.decagon.queuepay.payload.LoginRequest;
import com.decagon.queuepay.payload.SignupRequest;
import com.decagon.queuepay.response.JwtResponse;
import javax.validation.Valid;

public interface UserService {
  User registration(@Valid SignupRequest signupRequest) throws Exception;
  void verifyRegistration(String token) throws Exception;
  JwtResponse authenticate(LoginRequest loginRequest);
}
