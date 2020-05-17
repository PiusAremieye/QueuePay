package com.decagon.queuepay.repositories;

import com.decagon.queuepay.models.Business;
import com.decagon.queuepay.models.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByBusiness(Business business);
}
