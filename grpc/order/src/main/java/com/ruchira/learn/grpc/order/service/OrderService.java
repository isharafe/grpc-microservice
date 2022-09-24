package com.ruchira.learn.grpc.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ruchira.learn.grpc.order.model.CustomerOrder;
import com.ruchira.learn.grpc.order.model.OrderItem;
import com.ruchira.learn.grpc.order.repository.CustomerOrderRepository;
import com.ruchira.learn.grpc.order.repository.OrderItemRepository;

@Service
public class OrderService {

	@Autowired
	private CustomerOrderRepository orderRepo;

	@Autowired
	private OrderItemRepository itemRepo;

	public CustomerOrder save(CustomerOrder order) {
		order.getItems().forEach(item -> item.setOrder(order));
		return orderRepo.save(order);
	}

	public Page<CustomerOrder> getOrders(Pageable pageable) {
		return orderRepo.findAll(pageable);
	}

	public CustomerOrder getOrder(Long orderId, Pageable pageable) {
		return orderRepo.findById(orderId).orElse(null);
	}

	public Page<OrderItem> getOrderItems(Long orderId, Pageable pageable) {
		return itemRepo.findByOrderId(orderId, pageable);
	}
}
