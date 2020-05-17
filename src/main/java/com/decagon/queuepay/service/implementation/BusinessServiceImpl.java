package com.decagon.queuepay.service.implementation;

import com.decagon.queuepay.exceptions.CustomException;
import com.decagon.queuepay.models.Business;
import com.decagon.queuepay.models.user.User;
import com.decagon.queuepay.models.wallet.Wallet;
import com.decagon.queuepay.payload.BusinessDto;
import com.decagon.queuepay.repositories.BusinessRepository;
import com.decagon.queuepay.repositories.WalletRepository;
import com.decagon.queuepay.security.JwtProvider;
import com.decagon.queuepay.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {
  private BusinessRepository businessRepository;
  private WalletRepository walletRepository;
  private JwtProvider jwtProvider;

  @Autowired
  public BusinessServiceImpl(BusinessRepository businessRepository, WalletRepository walletRepository, JwtProvider jwtProvider) {
    this.businessRepository = businessRepository;
    this.walletRepository = walletRepository;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Business findBusiness(Long businessId, HttpServletRequest request) {
    jwtProvider.resolveUser(request);
    return businessRepository.findById(businessId).orElseThrow(() -> new CustomException("Business of id : '"+businessId+ "' does not exists", HttpStatus.NOT_FOUND));
  }

  public List<Business> findAllBusiness(HttpServletRequest request){
    User user = jwtProvider.resolveUser(request);
    return businessRepository.findBusinessesByUser(user);
  }

  public Business businessRegistration(@Valid BusinessDto businessDto, HttpServletRequest request){
    User user = jwtProvider.resolveUser(request);
    Business business = new Business();
    Wallet wallet = new Wallet();
    business.setUser(user);
    business.setName(businessDto.getName());
    business.setLogoUrl(businessDto.getLogoUrl());
    business.setCacDocumentUrl(businessDto.getCacDocumentUrl());
    business.setDescription(businessDto.getDescription());
    wallet.setPin(businessDto.getPin());
    wallet.setWalletType(businessDto.getWalletType());

    wallet.setBusiness(business);
    walletRepository.save(wallet);
    return businessRepository.save(business);
  }

  @Override
  public void deleteBusiness(Long businessId, HttpServletRequest request) {
    try{
      businessRepository.delete(findBusiness(businessId, request));
    } catch (Exception ex){
      throw new CustomException("Something went wrong trying to delete business of id : '"+businessId, HttpStatus.BAD_REQUEST);
    }
  }
}
