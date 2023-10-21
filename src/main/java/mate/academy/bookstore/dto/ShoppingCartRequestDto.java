package mate.academy.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartRequestDto {
    @NotNull
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
