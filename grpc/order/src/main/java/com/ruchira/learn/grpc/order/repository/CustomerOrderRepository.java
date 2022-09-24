package com.ruchira.learn.grpc.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ruchira.learn.grpc.order.model.CustomerOrder;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

	Page<CustomerOrder> findByUsername(String username, Pageable pageable);

	CustomerOrder findByIdAndUsername(Long orderId, String username);

}
