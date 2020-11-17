package de.coiaf.footballprediction.backend.persistence.entity;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.lang.annotation.*;

@DecimalMin(value = "1", inclusive = false, message = "{de.coiaf.footballprediction.backend.persistence.constraints.annotations.DecimalOddValue.min}")
@DecimalMax(value = "99999999999.99", message = "{de.coiaf.footballprediction.backend.persistence.constraints.annotations.DecimalOddValue.max}")
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface DecimalOddValue {

    String message() default "{de.coiaf.footballprediction.backend.persistence.constraints.annotations.DecimalOddValue.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
