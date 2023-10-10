package mate.academy.bookstore.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import mate.academy.bookstore.validation.EmailValidation;

@Data
public class UserLoginRequestDto {
    @NotEmpty
    @Size(min = 4, max = 20)
    @EmailValidation
    private String email;
    @NotEmpty
    @Size(min = 4, max = 20)
    private String password;
}
