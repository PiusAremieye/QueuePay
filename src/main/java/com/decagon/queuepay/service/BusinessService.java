package com.decagon.queuepay.service;

import com.decagon.queuepay.models.Business;
import com.decagon.queuepay.payload.BusinessDto;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

public interface BusinessService {
  Business findBusiness(Long businessId, HttpServletRequest request);
  List<Business> findAllBusiness(HttpServletRequest request);
  Business businessRegistration(@Valid BusinessDto businessDto, HttpServletRequest request) throws Exception;
  void deleteBusiness(Long businessId, HttpServletRequest request);
}
