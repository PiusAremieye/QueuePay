package com.decagon.queuepay.payload;

import com.decagon.queuepay.models.user.User;
import com.decagon.queuepay.models.wallet.WalletType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BusinessDto {

  @JsonIgnore
  private User user;

  @NotBlank(message = "Please name is required")
  private String name;

  @NotBlank(message = "Please logo url is required")
  private String logoUrl;

  @NotBlank(message = "Please CAC Document Url is required")
  private String cacDocumentUrl;

  @NotBlank(message = "Please description is required")
  @Column(columnDefinition = "text")
  private String description;

  private WalletType walletType;

  @NotBlank(message = "Please pin is required")
  private String pin;

}
