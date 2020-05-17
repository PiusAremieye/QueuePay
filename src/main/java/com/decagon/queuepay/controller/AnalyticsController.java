package com.decagon.queuepay.controller;

import com.decagon.queuepay.apiresponse.ApiResponse;
import com.decagon.queuepay.payload.Analytics;
import com.decagon.queuepay.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/business")
@CrossOrigin
public class AnalyticsController {
  private AnalyticsService analyticsService;

  @Autowired
  public AnalyticsController(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  @GetMapping("/{businessId}/analytics")
  public ResponseEntity<?> analysis(@PathVariable(value = "businessId") Long businessId, HttpServletRequest request){
    Analytics analytics = analyticsService.getAnalytics(businessId, request);
    ApiResponse<Analytics> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(analytics);
    response.setMessage("Analysis retrieved successfully!");
    return new ResponseEntity<>(response, response.getStatus());
  }
}
