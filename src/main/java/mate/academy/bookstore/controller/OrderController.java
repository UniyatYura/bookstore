package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.OrderItemResponseDto;
import mate.academy.bookstore.dto.OrderRequestDto;
import mate.academy.bookstore.dto.OrderResponseDto;
import mate.academy.bookstore.dto.OrderUpdateStatusRequestDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order", description = "Create new order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderResponseDto createOrder(Authentication authentication,
                                        @RequestBody @Valid OrderRequestDto orderRequestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createUserOrder(user.getId(), orderRequestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all orders", description = "Get all users order history")
    public List<OrderResponseDto> findAllOrdersByUser(Authentication authentication,
                                                      Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrdersByUser(user.getId(), pageable);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order status", description = "Update order status")
    public OrderResponseDto updateOrderStatus(@PathVariable Long orderId,
                                              @RequestBody @Valid
                OrderUpdateStatusRequestDto orderUpdateStatusRequestDto) {
        return orderService.updateOrderStatus(orderId, orderUpdateStatusRequestDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all order items from order",
            description = "Find all order items")
    public Set<OrderItemResponseDto> findAllOrderItemsByOrder(@PathVariable Long orderId) {
        return orderService.findAllOrderItems(orderId);
    }

    @Operation(summary = "Find order item by id ", description = "Find order item by ID")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemResponseDto findOrderItemById(@PathVariable Long orderId,
                                                  @PathVariable Long itemId) {
        return orderService.findOrderItemByOrderId(orderId, itemId);
    }
}
