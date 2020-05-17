package com.decagon.queuepay.controller;

import com.decagon.queuepay.apiresponse.ApiResponse;
import com.decagon.queuepay.models.Business;
import com.decagon.queuepay.payload.BusinessDto;
import com.decagon.queuepay.service.BusinessService;
import com.decagon.queuepay.service.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/business")
@CrossOrigin
public class BusinessController {
  private BusinessService businessService;
  private MapValidationErrorService mapValidationErrorService;

  @Autowired
  public BusinessController(BusinessService businessService, MapValidationErrorService mapValidationErrorService) {
    this.businessService = businessService;
    this.mapValidationErrorService = mapValidationErrorService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> businessReg(@RequestBody @Valid BusinessDto businessDto, BindingResult bindingResult, HttpServletRequest request)
          throws Exception {
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
    if (errorMap != null){
      return errorMap;
    }
    Business business = businessService.businessRegistration(businessDto, request);
    ApiResponse<Business> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(business);
    response.setMessage("Business registered successfully!");
    return new ResponseEntity<>(response, response.getStatus());
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllBusiness(HttpServletRequest request){
    List<Business> allBusiness = businessService.findAllBusiness(request);
    ApiResponse<List<Business>> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(allBusiness);
    response.setMessage("Businesses retrieved successfully!");
    return new ResponseEntity<>(response, response.getStatus());
  }

  @DeleteMapping("/{businessId}")
  public ResponseEntity<?> getAllBusiness(@PathVariable(value = "businessId") Long businessId, HttpServletRequest request){
    businessService.deleteBusiness(businessId, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Business deleted successfully!");
    return new ResponseEntity<>(response, response.getStatus());
  }
}
