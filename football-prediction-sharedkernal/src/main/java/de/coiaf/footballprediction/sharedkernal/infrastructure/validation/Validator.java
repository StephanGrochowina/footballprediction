package de.coiaf.footballprediction.sharedkernal.infrastructure.validation;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class Validator {

    private final javax.validation.Validator internalValidator;

    @Inject Validator(javax.validation.Validator internalValidator) {
        Objects.requireNonNull(internalValidator, "Parameter internalValidator must not be null.");

        this.internalValidator = internalValidator;
    }

    private static <BEAN> ConstraintViolationException createDefaultException(Set<ConstraintViolation<BEAN>> violations) {
        return new ConstraintViolationException(violations);
    }

    /**
     * Validates a bean an throws an exception if validation fails. If the bean is null validation will be omitted.
     * @param bean the bean to be validated
     * @param <BEAN> the type of the bean to be validated
     * @throws ConstraintViolationException if validation fails
     */
    public <BEAN> void validate(BEAN bean) {
        this.validate(bean, Validator::createDefaultException);
    }

    /**
     * Validates a bean an throws an exception if validation fails. If the bean is null validation will be omitted.
     * @param bean the bean to be validated
     * @param exceptionFactory the factory which creates the exception of the expected type
     * @param <BEAN> the type of the bean to be validated
     * @param <EXCEPTION> the type of the exception to be thrown
     * @throws NullPointerException if {@param exceptionFactory} is null.
     * @throws EXCEPTION if validation fails and the exception created by {@param exceptionFactory} is not null
     * @throws ConstraintViolationException if validation fails and the exception created by {@param exceptionFactory} is null
     */
    public <BEAN, EXCEPTION extends Exception> void validate(BEAN bean, Function<Set<ConstraintViolation<BEAN>>, EXCEPTION> exceptionFactory) throws EXCEPTION {
        if (bean == null) {
            return;
        }
        Objects.requireNonNull(exceptionFactory, "Parameter exceptionFactory must not be null.");

        Set<ConstraintViolation<BEAN>> violations = this.internalValidator.validate(bean);

        if (violations != null && violations.size() > 0) {
            EXCEPTION exception = exceptionFactory.apply(violations);

            if (exception == null) {
                throw new ConstraintViolationException(violations);
            }

            throw exception;
        }
    }
}
