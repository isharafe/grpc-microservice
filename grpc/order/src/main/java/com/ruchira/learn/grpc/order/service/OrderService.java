package com.ruchira.learn.grpc.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		order.setUsername(username);
		order.getItems().forEach(item -> item.setOrder(order));
		return orderRepo.save(order);
	}

	public Page<CustomerOrder> getMyOrders(Pageable pageable) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return orderRepo.findByUsername(username, pageable);
	}

	public CustomerOrder getMyOrder(Long orderId) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return orderRepo.findByIdAndUsername(orderId, username);
	}

	public Page<OrderItem> getMyOrderItems(Long orderId, Pageable pageable) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return itemRepo.findByOrderIdAndOrderUsername(orderId, username, pageable);
	}
}
