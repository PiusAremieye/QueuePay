package com.decagon.queuepay.service;

import com.decagon.queuepay.payload.Analytics;
import javax.servlet.http.HttpServletRequest;

public interface AnalyticsService {
  Double totalWalletAmounts(Long businessId, HttpServletRequest request);
  Analytics getAnalytics(Long businessId, HttpServletRequest request);
}
