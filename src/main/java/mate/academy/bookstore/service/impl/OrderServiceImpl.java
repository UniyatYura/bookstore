package mate.academy.bookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.OrderItemResponseDto;
import mate.academy.bookstore.dto.OrderRequestDto;
import mate.academy.bookstore.dto.OrderResponseDto;
import mate.academy.bookstore.dto.OrderUpdateStatusRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.OrderItemRepository;
import mate.academy.bookstore.repository.OrderRepository;
import mate.academy.bookstore.repository.ShoppingCartRepository;
import mate.academy.bookstore.service.OrderService;
import mate.academy.bookstore.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto createUserOrder(Long userId, OrderRequestDto orderRequestDto) {
        User user = userService.getUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't get ShoppingCart by id "
                        + userId));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Your shopping cart is empty!");
        }
        Order order = createOrderByUser(user);
        Set<OrderItem> orderItems = collectOrderItems(order, cartItems);
        order.setTotal(calculatePrice(orderItems));
        order.setOrderItems(orderItems);
        shoppingCart.setCartItems(Collections.emptySet());
        Order saveOrder = orderRepository.save(order);
        return orderMapper.toDto(saveOrder);
    }

    @Override
    public List<OrderResponseDto> findAllOrdersByUser(Long id, Pageable pageable) {
        return orderRepository.findAll(pageable)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId,
                                              OrderUpdateStatusRequestDto orderUpdateStatusDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Can't find order by id " + orderId));
        order.setStatus(orderUpdateStatusDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderItemResponseDto> findAllOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Can't find order by id " + orderId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemResponseDto findOrderItemByOrderId(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findOrderItemByOrderIdAndId(orderId, itemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cant find order by id " + orderId
                                + " and item by id " + itemId));
        return orderItemMapper.toDto(orderItem);
    }

    private Set<OrderItem> collectOrderItems(Order order, Set<CartItem> cartItems) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private BigDecimal calculatePrice(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(oi -> BigDecimal.valueOf(oi.getQuantity()).multiply(oi.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order createOrderByUser(User user) {
        Order order = new Order();
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setShippingAddress(user.getShippingAddress());
        return order;
    }
}
