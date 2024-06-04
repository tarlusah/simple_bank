package com.vinra.simple_banking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValueOfEnum {

	Class<? extends Enum<?>> enumClass();

	String message() default "must be any of enum {enumClass}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
