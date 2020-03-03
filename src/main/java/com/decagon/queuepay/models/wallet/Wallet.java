package com.decagon.queuepay.models.wallet;

import com.decagon.queuepay.models.AuditModel;
import com.decagon.queuepay.models.Business;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Wallet extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private Double balance = 0.00;

    @NotNull
    @NotBlank
    private WalletType walletType;

    @NotNull
    @NotBlank
    private String pin;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "businessId", nullable = false)
    private Business business;
}
