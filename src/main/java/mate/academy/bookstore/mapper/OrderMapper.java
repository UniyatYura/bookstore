package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.OrderItemResponseDto;
import mate.academy.bookstore.dto.OrderRequestDto;
import mate.academy.bookstore.dto.OrderResponseDto;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "order.user.id")
    OrderResponseDto toDto(Order order);

    Order toModel(OrderRequestDto orderRequestDto);

    @AfterMapping
    default void setBookId(@MappingTarget OrderItemResponseDto orderItemResponseDto,
                           OrderItem orderItem) {
        orderItemResponseDto.setBookId(orderItem.getBook().getId());
    }
}
