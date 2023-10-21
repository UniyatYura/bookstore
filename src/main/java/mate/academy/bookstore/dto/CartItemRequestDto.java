package mate.academy.bookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotNull
    @Positive
    private Long bookId;
    @NotNull(message = "Please enter quantity ")
    @Min(value = 0, message = "Invalid quantity, it cannot be less than zero")
    private int quantity;
}
