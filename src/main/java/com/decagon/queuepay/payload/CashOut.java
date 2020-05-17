package com.decagon.queuepay.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CashOut {
  private Double amount;

  @NotBlank(message = "Please provide your wallet pin")
  private String pin;

  @NotBlank(message = "Please provide a bank name")
  private String bankName;

  @NotBlank(message = "Please provide a bank account number")
  private String bankAccountNumber;
}

