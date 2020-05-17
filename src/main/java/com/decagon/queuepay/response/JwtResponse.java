package com.decagon.queuepay.response;

import com.decagon.queuepay.payload.SignupRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class JwtResponse extends SignupRequest {
  private String token;

  public JwtResponse(String fullName, String phoneNumber, String username, String password ) {
    super(fullName, phoneNumber, username, null);
  }

}
