package com.ruchira.learn.grpc.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruchira.learn.grpc.order.model.CustomerOrder;
import com.ruchira.learn.grpc.order.model.OrderItem;
import com.ruchira.learn.grpc.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public Page<CustomerOrder> getOrders(Pageable pageable) {
		return orderService.getOrders(pageable);
	}

	@GetMapping("/{orderId}")
	public CustomerOrder getOrder(@PathVariable Long orderId, Pageable pageable) {
		return orderService.getOrder(orderId, pageable);
	}

	@GetMapping("{orderId}/items")
	public Page<OrderItem> getOrderItems(@PathVariable Long orderId, Pageable pageable) {
		return orderService.getOrderItems(orderId, pageable);
	}

	@PostMapping
	public CustomerOrder saveOrder(@RequestBody CustomerOrder order) {
		return orderService.save(order);
	}
}
