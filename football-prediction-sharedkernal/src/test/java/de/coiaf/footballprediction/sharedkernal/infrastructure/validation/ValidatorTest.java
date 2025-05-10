package de.coiaf.footballprediction.sharedkernal.infrastructure.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ValidatorTest {

    private static final Set<ConstraintViolation<Object>> NO_VIOLATIONS = new HashSet<>();
    private static final ConstraintViolation<Object> violationMock = (ConstraintViolation<Object>) mock(ConstraintViolation.class);
    private static final Set<ConstraintViolation<Object>> VIOLATIONS = Collections.singleton(violationMock);

    private javax.validation.Validator internalValidatorMock = null;
    private Function<Set<ConstraintViolation<Object>>, IllegalArgumentException> exceptionFactoryMock = null;

    @Before
    public void setUp() {
        this.internalValidatorMock = mock(javax.validation.Validator.class);
        this.exceptionFactoryMock = (Function<Set<ConstraintViolation<Object>>, IllegalArgumentException>) mock(Function.class);
    }

    @After
    public void tearDown() {
        this.internalValidatorMock = null;
        this.exceptionFactoryMock = null;
    }

    @Test(expected = NullPointerException.class)
    public void constructor_nullInternalValidator() {
        new Validator(null);
    }
    @Test
    public void constructor_givenInternalValidator() {
        Validator validator = new Validator(this.internalValidatorMock);

        assertNotNull(validator);
    }

    @Test
    public void validate_nullBean() {
        Validator validator = new Validator(this.internalValidatorMock);

        validator.validate(null);

        verify(this.internalValidatorMock, never()).validate(any());
    }
    @Test
    public void validate_givenBean_internalValidatorReturnsNullViolations() {
        Validator validator = new Validator(this.internalValidatorMock);
        Object beanMock = mock(Object.class);

        when(this.internalValidatorMock.validate(beanMock)).thenReturn(null);

        validator.validate(beanMock);

        verify(this.internalValidatorMock, times(1)).validate(beanMock);
    }
    @Test
    public void validate_givenBean_internalValidatorReturnsNoViolations() {
        Validator validator = new Validator(this.internalValidatorMock);
        Object beanMock = mock(Object.class);

        when(this.internalValidatorMock.validate(beanMock)).thenReturn(NO_VIOLATIONS);

        validator.validate(beanMock);

        verify(this.internalValidatorMock, times(1)).validate(beanMock);
    }
    @Test(expected = ConstraintViolationException.class)
    public void validate_givenBean_internalValidatorReturnsViolations() {
        Validator validator = new Validator(this.internalValidatorMock);
        Object beanMock = mock(Object.class);

        when(this.internalValidatorMock.validate(beanMock)).thenReturn(VIOLATIONS);

        validator.validate(beanMock);
    }

    @Test
    public void validate_nullBean_givenViolationExceptionFactory() {
        Validator validator = new Validator(this.internalValidatorMock);

        when(this.exceptionFactoryMock.apply(any())).thenReturn(new IllegalArgumentException());

        validator.validate(null, this.exceptionFactoryMock);

        verify(this.internalValidatorMock, never()).validate(any());
        verify(this.exceptionFactoryMock, never()).apply(any());
    }
    @Test(expected = NullPointerException.class)
    public void validate_givenBean_nullViolationExceptionFactory() {
        Validator validator = new Validator(this.internalValidatorMock);
        Object beanMock = mock(Object.class);

        validator.validate(beanMock, null);
    }
    @Test
    public void validate_givenBean_givenViolationExceptionFactory_internalValidatorReturnsNullViolations() {
        Validator validator = new Validator(this.internalValidatorMock);
        Object beanMock = mock(Object.class);

        when(this.internalValidatorMock.validate(beanMock)).thenReturn(null);
        when(this.exceptionFactoryMock.apply(any())).thenReturn(new IllegalArgumentException());

        validator.validate(beanMock, this.exceptionFactoryMock);

        verify(this.internalValidatorMock, times(1)).validate(any());
        verify(this.exceptionFactoryMock, never()).apply(any());
    }
    @Test
    public void validate_givenBean_givenViolationExceptionFactory_internalValidatorReturnsNoViolations() {
        Validator validator = new Validator(this.internalValidatorMock);
        Object beanMock = mock(Object.class);

        when(this.internalValidatorMock.validate(beanMock)).thenReturn(NO_VIOLATIONS);
        when(this.exceptionFactoryMock.apply(any())).thenReturn(new IllegalArgumentException());

        validator.validate(beanMock, this.exceptionFactoryMock);

        verify(this.internalValidatorMock, times(1)).validate(any());
        verify(this.exceptionFactoryMock, never()).apply(any());
    }
    @Test(expected = IllegalArgumentException.class)
    public void validate_givenBean_violationExceptionFactoryReturnsException_internalValidatorReturnsViolations() {
        Validator validator = new Validator(this.internalValidatorMock);
        Object beanMock = mock(Object.class);

        when(this.internalValidatorMock.validate(beanMock)).thenReturn(VIOLATIONS);
        when(this.exceptionFactoryMock.apply(any())).thenReturn(new IllegalArgumentException());

        validator.validate(beanMock, this.exceptionFactoryMock);
    }
    @Test(expected = ConstraintViolationException.class)
    public void validate_givenBean_violationExceptionFactoryReturnsNull_internalValidatorReturnsViolations() {
        Validator validator = new Validator(this.internalValidatorMock);
        Object beanMock = mock(Object.class);

        when(this.internalValidatorMock.validate(beanMock)).thenReturn(VIOLATIONS);
        when(this.exceptionFactoryMock.apply(any())).thenReturn(null);

        validator.validate(beanMock, this.exceptionFactoryMock);
    }
}