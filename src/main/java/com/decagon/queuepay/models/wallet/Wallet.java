package com.decagon.queuepay.models.wallet;

import com.decagon.queuepay.models.AuditModel;
import com.decagon.queuepay.models.Business;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wallets")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Wallet extends AuditModel {

  private Double balance = 0.00;

  private WalletType walletType;

  @NotNull
  @NotBlank
  private String pin;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
          name = "businesses_wallets",
          joinColumns = @JoinColumn(name = "wallet_id"),
          inverseJoinColumns = @JoinColumn(name = "business_id"))
  private Business business;
}
