package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.BookQuantityDto;
import mate.academy.bookstore.dto.CartItemRequestDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    BookQuantityDto toDto(CartItem cartItem);

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toModel(CartItemRequestDto cartItemRequestDto, Book book);

    BookQuantityDto toBookQuantityDto(CartItem cartItem);
}
