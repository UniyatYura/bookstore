package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.BookQuantityDto;
import mate.academy.bookstore.dto.CartItemRequestDto;
import mate.academy.bookstore.dto.ShoppingCartResponseDto;
import mate.academy.bookstore.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getShoppingCartModel();

    ShoppingCartResponseDto getShoppingCartDto();

    ShoppingCartResponseDto getShoppingCartByUserId(Long id);

    ShoppingCartResponseDto addBook(CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateBookQuantity(BookQuantityDto bookQuantityDto, Long cartItemId);

    void deleteCartItem(Long cartItemId);
}
