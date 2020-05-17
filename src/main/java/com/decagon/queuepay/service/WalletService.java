package com.decagon.queuepay.service;

import com.decagon.queuepay.models.wallet.Wallet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WalletService {
  Wallet findWalletByBusinessId(Long walletId, Long businessId, HttpServletRequest request);
  List<Wallet> findWalletsByBusinessId(Long businessId, HttpServletRequest request);
  void deleteWallet(Long walletId, Long businessId, HttpServletRequest request);
}

