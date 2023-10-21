package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.BookQuantityDto;
import mate.academy.bookstore.dto.CartItemRequestDto;
import mate.academy.bookstore.dto.ShoppingCartResponseDto;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Endpoints for managing ShoppingCart")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")

public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Retrieve user's shopping cart")
    public ShoppingCartResponseDto getCart() {
        return shoppingCartService.getShoppingCartDto();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add book to the shopping cart")
    public ShoppingCartResponseDto addBookToShoppingCart(
            @RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addBook(requestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity of a book in the shopping cart")
    public ShoppingCartResponseDto updateQuantityOfBook(
            @PathVariable Long cartItemId, @RequestBody @Valid BookQuantityDto requestDto) {
        return shoppingCartService.updateBookQuantity(requestDto, cartItemId);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a book from the shopping cart")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
