package com.decagon.queuepay.service.implementation;

import com.decagon.queuepay.exceptions.CustomException;
import com.decagon.queuepay.models.Business;
import com.decagon.queuepay.models.wallet.Wallet;
import com.decagon.queuepay.repositories.WalletRepository;
import com.decagon.queuepay.service.BusinessService;
import com.decagon.queuepay.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
  private WalletRepository walletRepository;
  private BusinessService businessService;

  @Autowired
  public WalletServiceImpl(WalletRepository walletRepository, BusinessService businessService) {
    this.walletRepository = walletRepository;
    this.businessService = businessService;
  }

  @Override
  public Wallet findWalletByBusinessId(Long walletId, Long businessId, HttpServletRequest request) {
    businessService.findBusiness(businessId, request);
    return walletRepository.findById(walletId).orElseThrow(() -> new CustomException("Wallet with id : '"+walletId+"' does not exists", HttpStatus.NOT_FOUND));
  }

  @Override
  public List<Wallet> findWalletsByBusinessId(Long businessId, HttpServletRequest request) {
    Business business = businessService.findBusiness(businessId, request);
    return walletRepository.findByBusiness(business);
  }

  @Override
  public void deleteWallet(Long walletId, Long businessId, HttpServletRequest request) {
    try{
      walletRepository.delete(findWalletByBusinessId(walletId, businessId, request));
    } catch (Exception ex){
      throw new CustomException("Something went wrong trying to delete wallet of id : '"+walletId, HttpStatus.BAD_REQUEST);
    }
  }
}
