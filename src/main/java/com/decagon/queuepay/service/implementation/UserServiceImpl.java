package com.decagon.queuepay.service.implementation;

import com.decagon.queuepay.exceptions.CustomException;
import com.decagon.queuepay.models.user.EmailVerificationStatus;
import com.decagon.queuepay.models.user.User;
import com.decagon.queuepay.payload.LoginRequest;
import com.decagon.queuepay.payload.SignupRequest;
import com.decagon.queuepay.repositories.UserRepository;
import com.decagon.queuepay.response.JwtResponse;
import com.decagon.queuepay.security.JwtProvider;
import com.decagon.queuepay.service.EmailSenderService;
import com.decagon.queuepay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.validation.Valid;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private JwtProvider jwtProvider;
  private AuthenticationManager authenticationManager;
  private PasswordEncoder passwordEncoder;
  private EmailSenderService emailSenderService;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, EmailSenderService emailSenderService) {
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.emailSenderService = emailSenderService;
  }

  public User registration(@Valid SignupRequest signupRequest) throws Exception {
    if (userRepository.existsByUsername(signupRequest.getUsername())) {
      throw new CustomException("This email already exists!", HttpStatus.BAD_REQUEST);
    }
    User user = new User();
    user.setPhoneNumber(signupRequest.getPhoneNumber());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    user.setUsername(signupRequest.getUsername());
    user.setFullName(signupRequest.getFullName());
    return userRepository.save(user);

//    String token = jwtProvider.createToken(signupRequest.getUsername());
//    SimpleMailMessage mailMessage = new SimpleMailMessage();
//    mailMessage.setTo(signupRequest.getUsername());
//    mailMessage.setSubject("Complete Registration!");
//    mailMessage.setFrom("testing-70a156@inbox.mailtrap.io");
//    mailMessage.setText("To confirm your account, please click here : "
//      + "http://localhost:3000/verify?token=" + token);
//
//    emailSenderService.sendEmail(mailMessage);
  }

  public void verifyRegistration(String token) throws Exception {
    User user = userRepository.findByEmailVerificationToken(token);
    if (user == null) {
      throw new Exception("Unable to verify that you registered here");
    }
    user.setEmailVerificationStatus(EmailVerificationStatus.VERIFIED);
    userRepository.save(user);
  }

  public JwtResponse authenticate(LoginRequest loginRequest){
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();

    try{
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      Optional<User> user = userRepository.findByUsername(username);
      String token = jwtProvider.createToken(username);
      if (user.isPresent()){
        JwtResponse loginResponse = new JwtResponse(user.get().getFullName(), user.get().getPhoneNumber(), user.get().getUsername(), null);
        loginResponse.setToken(token);
        return loginResponse;
      }
      throw new CustomException("Invalid username/password supplied...", HttpStatus.UNPROCESSABLE_ENTITY);
    } catch (Exception ex){
      throw new CustomException("Invalid username/password supplied...", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

}
