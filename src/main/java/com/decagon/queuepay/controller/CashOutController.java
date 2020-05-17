package com.decagon.queuepay.controller;

import com.decagon.queuepay.apiresponse.ApiResponse;
import com.decagon.queuepay.payload.CashOut;
import com.decagon.queuepay.service.CashOutService;
import com.decagon.queuepay.service.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/business")
@CrossOrigin
public class CashOutController {
  private CashOutService cashOutService;
  private MapValidationErrorService mapValidationErrorService;

  @Autowired
  public CashOutController(CashOutService cashOutService, MapValidationErrorService mapValidationErrorService) {
    this.cashOutService = cashOutService;
    this.mapValidationErrorService = mapValidationErrorService;
  }

  @PatchMapping("/{businessId}/wallet/{walletId}/cashout")
  public ResponseEntity<?> cashOut(
          @PathVariable(value = "businessId") Long businessId,
          @PathVariable(value = "walletId") Long walletId,
          @Valid @RequestBody CashOut cashOut, HttpServletRequest request, BindingResult bindingResult){
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
    if (errorMap != null){
      return errorMap;
    }
    Double amount = cashOutService.cashOut(businessId, walletId, cashOut, request);
    ApiResponse<Double> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Successfully Cashed Out the amount of "+ amount);
    return new ResponseEntity<>(response, response.getStatus());
  }

}
