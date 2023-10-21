package mate.academy.bookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookQuantityDto {
    @NotNull(message = "Please enter quantity ")
    @Min(value = 0, message = "Invalid quantity, it cannot be less than zero")
    private int quantity;
}
