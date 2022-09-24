package com.ruchira.learn.grpc.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ruchira.learn.grpc.order.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	Page<OrderItem> findByOrderId(Long orderId, Pageable page);
}
