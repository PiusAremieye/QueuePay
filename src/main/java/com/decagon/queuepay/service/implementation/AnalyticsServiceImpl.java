package com.decagon.queuepay.service.implementation;

import com.decagon.queuepay.exceptions.CustomException;
import com.decagon.queuepay.models.Business;
import com.decagon.queuepay.models.transaction.Transaction;
import com.decagon.queuepay.models.transaction.TransactionStatus;
import com.decagon.queuepay.models.transaction.TransactionType;
import com.decagon.queuepay.models.wallet.Wallet;
import com.decagon.queuepay.payload.Analytics;
import com.decagon.queuepay.repositories.TransactionRepository;
import com.decagon.queuepay.service.AnalyticsService;
import com.decagon.queuepay.service.BusinessService;
import com.decagon.queuepay.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
  private TransactionRepository transactionRepository;
  private BusinessService businessService;
  private WalletService walletService;

  @Autowired
  public AnalyticsServiceImpl(TransactionRepository transactionRepository, BusinessService businessService, WalletService walletService) {
    this.transactionRepository = transactionRepository;
    this.businessService = businessService;
    this.walletService = walletService;
  }

  public Double totalWalletAmounts(Long businessId, HttpServletRequest request){
    double balance = 0.0;
    List<Wallet> wallets = walletService.findWalletsByBusinessId(businessId, request);
    for (Wallet wallet : wallets){
      balance += wallet.getBalance();
    }
    return balance;
  }

  public Analytics getAnalytics(Long businessId, HttpServletRequest request) {
    int transactionVolume = 0;
    double value = 0.0;
    int successfulTransaction = 0;
    int failedTransaction = 0;

    Business business = businessService.findBusiness(businessId, request);
    List<Transaction> transactions = transactionRepository.findByBusiness(business);
    if (!transactions.isEmpty()) {
      Double accountBalance = totalWalletAmounts(businessId, request);
      for (Transaction trans : transactions) {
        transactionVolume++;
        value += trans.getAmount();
        if (trans.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
          successfulTransaction++;
        } else if (trans.getStatus().equals(TransactionStatus.FAILED)) {
          failedTransaction++;
        }
        if (trans.getTransactionType().equals(TransactionType.CREDIT)) {
          accountBalance += trans.getAmount();
        }
      }
      List<Wallet> wallets = walletService.findWalletsByBusinessId(businessId, request);
      Analytics analytics = new Analytics();
      analytics.setAccountBalance(accountBalance);
      analytics.setFailedTransaction(failedTransaction);
      analytics.setSuccessfulTransaction(successfulTransaction);
      analytics.setVolume(transactionVolume);
      analytics.setValue(value);
      analytics.setWallet(wallets);
      return analytics;
    }
    throw new CustomException("There was no transactions made for business of id : '"+businessId, HttpStatus.BAD_REQUEST);
  }
}
