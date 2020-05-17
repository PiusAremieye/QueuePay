package com.decagon.queuepay.service;

import com.decagon.queuepay.payload.CashOut;
import javax.servlet.http.HttpServletRequest;

public interface CashOutService {
  Double cashOut(Long businessId, Long walletId, CashOut cashOut, HttpServletRequest request);
}