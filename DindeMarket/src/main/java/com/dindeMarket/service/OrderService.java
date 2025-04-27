package com.dindeMarket.service;

import com.dindeMarket.api.payload.*;
import com.dindeMarket.db.entity.*;
import com.dindeMarket.db.repository.OrderRepository;
import com.dindeMarket.db.repository.OrderStatusInfoRepository;
import com.dindeMarket.db.repository.ProductRepository;
import com.dindeMarket.db.repository.UserRepository;
import com.dindeMarket.validation.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderStatusInfoRepository orderStatusInfoRepository;

    @Transactional
    public OrderResponse createOrder(UserEntity user,OrderRequest request) {
        OrderEntity savedOrder = orderRepository.save(convertToEntity(request,user));
        return convertToResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToResponse(order);
    }

    public List<OrderResponse> getAllOrders(Long id) {
        return orderRepository.findOrdersByRegionId(id).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByEmail(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserId(user.getId()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatusInfo status) {
        OrderEntity order = orderRepository.getById(id);
        order.getOrderStatus().add(status);
        OrderEntity updatedOrder = orderRepository.save(order);
        return convertToResponse(updatedOrder);
    }

    public List<OrderResponse> getOrdersByStatus(Long id,OrderStatus status) {
        return orderRepository.findOrdersByRegionAndStatus(id,status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<OrderResponse> updateOrderStatus(UpdateOrderStatusRequest request) {
        // Сначала сохраняем объект OrderStatusInfo, чтобы получить его id
//        OrderStatusInfo newStatus =new OrderStatusInfo();
//        newStatus.setStatus(request.getNewStatus().getStatus());
//        newStatus.setStatusTime(request.getNewStatus().getStatusTime());
//        OrderStatusInfo savedStatus = orderStatusInfoRepository.save(newStatus);

        // Получаем заказы по списку ID
        List<OrderEntity> orders = orderRepository.findAllById(request.getOrderIds());

        // Обновляем статус каждого заказа
        for (OrderEntity order : orders) {
            OrderStatusInfo newStatus =new OrderStatusInfo();
            newStatus.setStatus(request.getNewStatus().getStatus());
            newStatus.setStatusTime(request.getNewStatus().getStatusTime());
            newStatus.setOrder(order);
            OrderStatusInfo savedStatus = orderStatusInfoRepository.save(newStatus);

            if (order.getOrderStatus() == null) {
                order.setOrderStatus(new ArrayList<>());
            }

            // Устанавливаем ссылку на заказ в объекте OrderStatusInfo
            savedStatus.setOrder(order);

            // Добавляем новый статус
            order.getOrderStatus().add(savedStatus);
        }

        // Сохраняем все заказы с новым статусом
        List<OrderResponse> responses = new ArrayList<>();
        for (OrderEntity order : orders) {
            responses.add(convertToResponse(order));
        }
        return responses;
    }

    public List<OrderResponse> findOrdersByRegionAndDate(Long regionId, LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderEntity> orders = orderRepository.findOrdersByRegionAndOrderDate(regionId, startDate, endDate);
        // Преобразуем OrderEntity в OrderResponse
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    public OrderResponse findOrderByIdAndRegion(Long orderId, Long regionId) {
        Optional<OrderEntity> orderOpt = orderRepository.findByIdAndRegion(orderId, regionId);
        return orderOpt.map(this::convertToResponse)
                .orElseThrow(() -> new NotFoundException("Order not found for the given id and region"));
    }
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    private OrderEntity convertToEntity(OrderRequest orderRequest,UserEntity user) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setTotalAmount(orderRequest.getTotalAmount());
        orderEntity.setDiscount(orderRequest.getDiscount());
        orderEntity.setPhoneNumber(orderRequest.getPhoneNumber());
        orderEntity.setComment(orderRequest.getComment());
        orderEntity.setFirstName(orderRequest.getFirstName());
        orderEntity.setLastName(orderRequest.getLastName());
        orderEntity.setCity(orderRequest.getCity());
        orderEntity.setStreet(orderRequest.getStreet());
        orderEntity.setUnit(orderRequest.getUnit());
        orderEntity.setEntrance(orderRequest.getEntrance());
        orderEntity.setFloor(orderRequest.getFloor());
        orderEntity.setUser(user);
        orderEntity.setPaymentType(orderRequest.getPaymentType());

        // Устанавливаем продукты
        // Преобразуем ProductOrderRequest в OrderProductEntity и добавляем в заказ
        for (ProductOrderRequest productRequest : orderRequest.getProducts()) {
            ProductEntity product = productRepository.findById(productRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderProductEntity orderProduct = new OrderProductEntity();
            orderProduct.setOrder(orderEntity);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(productRequest.getQuantity()); // Устанавливаем количество

            orderEntity.getProducts().add(orderProduct);
        }
        orderRequest.getOrderStatus().setOrder(orderEntity);
        orderEntity.getOrderStatus().add(orderRequest.getOrderStatus());

        return orderEntity;
    }

    public OrderResponse convertToResponse(OrderEntity orderEntity) {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(orderEntity.getId());
        List<ProductOrderResponse> productOrderResponses = new ArrayList<>();
        // Используем ProductService для конвертации списка продуктов
        for (OrderProductEntity productOrder : orderEntity.getProducts()) {
            ProductOrderResponse response = new ProductOrderResponse();
            response.setProduct(productService.convertToResponse(productOrder.getProduct()));
            response.setQuantity(productOrder.getQuantity());
            productOrderResponses.add(response);
        }
        orderResponse.setProducts(productOrderResponses);

        orderResponse.setTotalAmount(orderEntity.getTotalAmount());
        orderResponse.setDiscount(orderEntity.getDiscount());
        orderResponse.setPriceDelivery(orderEntity.getUser().getRegion().getPriceDelivery()); // Новое поле
        orderResponse.setPhoneNumber(orderEntity.getPhoneNumber());
        orderResponse.setComment(orderEntity.getComment());
        orderResponse.setFirstName(orderEntity.getFirstName());
        orderResponse.setLastName(orderEntity.getLastName());
        orderResponse.getOrderStatus().addAll(orderEntity.getOrderStatus());
        orderResponse.setCity(orderEntity.getCity());
        orderResponse.setStreet(orderEntity.getStreet());
        orderResponse.setUnit(orderEntity.getUnit());
        orderResponse.setEntrance(orderEntity.getEntrance());
        orderResponse.setFloor(orderEntity.getFloor());
        orderResponse.setPaymentType(orderEntity.getPaymentType());
        return orderResponse;
    }
}
