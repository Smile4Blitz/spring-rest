@Documented
@Constraint(validatedBy = CustomValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidation {
    String message() default "Invalid value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class CustomValidator implements ConstraintValidator<CustomValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.startsWith("Valid");
    }
}
