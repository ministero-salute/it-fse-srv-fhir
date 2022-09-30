package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.impl.UniqueMultipartValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueMultipartValidator.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueMultipart {
    String message() default "One or more files duplicated";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}