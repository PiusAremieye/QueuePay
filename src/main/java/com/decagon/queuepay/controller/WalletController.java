package com.decagon.queuepay.controller;

import com.decagon.queuepay.apiresponse.ApiResponse;
import com.decagon.queuepay.models.wallet.Wallet;
import com.decagon.queuepay.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/business")
@CrossOrigin
public class WalletController {
  private WalletService walletService;

  @Autowired
  public WalletController(WalletService walletService) {
    this.walletService = walletService;
  }

  @GetMapping("{businessId}/wallets")
  public ResponseEntity<?> getAllWallets(@PathVariable(value = "businessId") Long businessId, HttpServletRequest request){
    List<Wallet> wallets = walletService.findWalletsByBusinessId(businessId, request);
    ApiResponse<List<Wallet>> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(wallets);
    response.setMessage("Wallets retrieved successfully!");
    return new ResponseEntity<>(response, response.getStatus());
  }

  @GetMapping("{businessId}/wallets/{walletId}")
  public ResponseEntity<?> getWallet(@PathVariable(value = "businessId") Long businessId, @PathVariable(value = "walletId") Long walletId, HttpServletRequest request){
    Wallet wallet = walletService.findWalletByBusinessId(walletId, businessId, request);
    ApiResponse<Wallet> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(wallet);
    response.setMessage("Wallet retrieved successfully!");
    return new ResponseEntity<>(response, response.getStatus());
  }

  @DeleteMapping("{businessId}/wallets/{walletId}")
  public ResponseEntity<?> deleteWallet(@PathVariable(value = "businessId") Long businessId, @PathVariable(value = "walletId") Long walletId, HttpServletRequest request){
    walletService.deleteWallet(walletId, businessId, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Wallet deleted successfully!");
    return new ResponseEntity<>(response, response.getStatus());
  }
}
