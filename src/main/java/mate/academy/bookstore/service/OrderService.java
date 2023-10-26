package mate.academy.bookstore.service;

import java.util.List;
import java.util.Set;
import mate.academy.bookstore.dto.OrderItemResponseDto;
import mate.academy.bookstore.dto.OrderRequestDto;
import mate.academy.bookstore.dto.OrderResponseDto;
import mate.academy.bookstore.dto.OrderUpdateStatusRequestDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createUserOrder(Long userId, OrderRequestDto orderRequestDto);

    List<OrderResponseDto> findAllOrdersByUser(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId,
                                       OrderUpdateStatusRequestDto orderUpdateStatusRequestDto);

    Set<OrderItemResponseDto> findAllOrderItems(Long orderId);

    OrderItemResponseDto findOrderItemByOrderId(Long orderId, Long itemId);
}
