package mate.academy.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.BookQuantityDto;
import mate.academy.bookstore.dto.CartItemRequestDto;
import mate.academy.bookstore.dto.ShoppingCartResponseDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.CartItemRepository;
import mate.academy.bookstore.repository.ShoppingCartRepository;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.service.ShoppingCartService;
import mate.academy.bookstore.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Override
    public ShoppingCartResponseDto getShoppingCartDto() {
        return shoppingCartMapper.toDto(getShoppingCartModel());
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartByUserId(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find Shopping Cart by id " + id));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto addBook(CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartModel();
        Book book = bookRepository.findById(cartItemRequestDto.getBookId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id "));
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto, book);
        cartItem.setShoppingCart(shoppingCart);
        CartItem saveCartItem = cartItemRepository.save(cartItem);
        shoppingCart.addCartItem(saveCartItem);
        return getShoppingCartDto();
    }

    @Override
    public ShoppingCartResponseDto updateBookQuantity(BookQuantityDto bookQuantityDto,
                                                      Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() ->
                new EntityNotFoundException("Can't find cart item by id: " + cartItemId));
        cartItem.setQuantity(bookQuantityDto.getQuantity());
        cartItemRepository.save(cartItem);
        return getShoppingCartDto();
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new EntityNotFoundException("Can't delete cart item by id: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    private ShoppingCart getShoppingCartModel() {
        User user = userService.getUser();
        return shoppingCartRepository.findById(user.getId()).get();
    }
}
