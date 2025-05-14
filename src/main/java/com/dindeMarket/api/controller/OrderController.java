package com.dindeMarket.api.controller;

import com.dindeMarket.api.payload.OrderRequest;
import com.dindeMarket.api.payload.OrderResponse;
import com.dindeMarket.api.payload.UpdateOrderStatusRequest;
import com.dindeMarket.db.entity.*;
import com.dindeMarket.db.repository.ProductRepository;
import com.dindeMarket.db.repository.UserRepository;
import com.dindeMarket.service.OrderService;
import com.dindeMarket.validation.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_CLIENT')")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody OrderRequest orderRequest) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).get();

        OrderResponse order = orderService.createOrder(user, orderRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(@AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).get();
        List<OrderResponse> orders = orderService.getAllOrders(user.getRegion().getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@AuthenticationPrincipal UserDetails userDetails) {
        // Предполагаем, что в userDetails хранится email клиента, с которым связан User
        String username = userDetails.getUsername();
        List<OrderResponse> orders = orderService.getOrdersByEmail(username);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusInfo status) {
        OrderResponse order = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/status/{status}")
    public List<OrderResponse> getOrdersByStatus(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable OrderStatus status) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).get();
        return orderService.getOrdersByStatus(user.getRegion().getId(), status);
    }
    @PutMapping("/update/status")
    public ResponseEntity<List<OrderResponse>> updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(request));
    }
    @GetMapping("/region/{regionId}/dates")
    public ResponseEntity<List<OrderResponse>> getOrdersByRegionAndDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).get();
        List<OrderResponse> orders = orderService.findOrdersByRegionAndDate(user.getRegion().getId(), startDate, endDate);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/{orderId}/region/{regionId}")
    public ResponseEntity<OrderResponse> getOrderByIdAndRegion(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable Long orderId) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).get();
        OrderResponse order = orderService.findOrderByIdAndRegion(orderId, user.getRegion().getId());
        return ResponseEntity.ok(order);
    }
}
