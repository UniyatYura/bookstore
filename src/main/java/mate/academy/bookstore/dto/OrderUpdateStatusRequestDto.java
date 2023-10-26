package mate.academy.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.bookstore.model.Order;

@Data
public class OrderUpdateStatusRequestDto {
    @NotNull(message = "Please fill in the status")
    private Order.Status status;
}
