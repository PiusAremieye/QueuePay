package com.decagon.queuepay.repositories;

import com.decagon.queuepay.models.Business;
import com.decagon.queuepay.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
  List<Business> findBusinessesByUser(User user);
}
