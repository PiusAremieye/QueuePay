package com.decagon.queuepay.service.implementation;

import com.decagon.queuepay.exceptions.CustomException;
import com.decagon.queuepay.models.Business;
import com.decagon.queuepay.models.transaction.Transaction;
import com.decagon.queuepay.models.transaction.TransactionStatus;
import com.decagon.queuepay.models.transaction.TransactionType;
import com.decagon.queuepay.models.user.User;
import com.decagon.queuepay.models.wallet.Wallet;
import com.decagon.queuepay.payload.CashOut;
import com.decagon.queuepay.repositories.TransactionRepository;
import com.decagon.queuepay.repositories.WalletRepository;
import com.decagon.queuepay.security.JwtProvider;
import com.decagon.queuepay.service.BusinessService;
import com.decagon.queuepay.service.CashOutService;
import com.decagon.queuepay.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
public class CashOutServiceImpl implements CashOutService {
  private TransactionRepository transactionRepository;
  private WalletService walletService;
  private BusinessService businessService;
  private WalletRepository walletRepository;
  private JwtProvider jwtProvider;

  @Autowired
  public CashOutServiceImpl(TransactionRepository transactionRepository, WalletService walletService, BusinessService businessService, WalletRepository walletRepository, JwtProvider jwtProvider) {
    this.transactionRepository = transactionRepository;
    this.walletService = walletService;
    this.businessService = businessService;
    this.walletRepository = walletRepository;
    this.jwtProvider = jwtProvider;
  }

  @Override
  @Transactional
  public Double cashOut(Long businessId, Long walletId, CashOut cashOut, HttpServletRequest request) {
//    try{
      User user = jwtProvider.resolveUser(request);
      Business business = businessService.findBusiness(businessId, request);
      Wallet wallet = walletService.findWalletByBusinessId(walletId, businessId, request);
      if (!wallet.getPin().equals(cashOut.getPin())) {
        throw new CustomException("Incorrect Pin", HttpStatus.NOT_FOUND);
      }
      if (cashOut.getAmount() >= wallet.getBalance()) {
        throw new CustomException("Insufficient Balance", HttpStatus.NOT_FOUND);
      }
      Double cashOutAmount = cashOut.getAmount();
      wallet.setBalance(wallet.getBalance() - cashOutAmount);
      walletRepository.save(wallet);

      Transaction transaction = new Transaction();
      transaction.setBusiness(business);
      transaction.setWallet(wallet);
      transaction.setUser(user);
      transaction.setAmount(cashOutAmount);
      transaction.setTransactionType(TransactionType.DEBIT);
      transaction.setStatus(TransactionStatus.SUCCESSFUL);
      transactionRepository.save(transaction);
      return cashOutAmount;
//    } catch (Exception ex){
//      throw new CustomException("Something went wrong", HttpStatus.BAD_REQUEST);
//    }
  }
}
