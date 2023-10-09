package mate.academy.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidationImpl implements ConstraintValidator<EmailValidation, String> {
    private static final String PATTERN_OF_EMAIL = "^(.+)@(\\S+)$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email != null && Pattern.compile(PATTERN_OF_EMAIL).matcher(email).matches();
    }
}
