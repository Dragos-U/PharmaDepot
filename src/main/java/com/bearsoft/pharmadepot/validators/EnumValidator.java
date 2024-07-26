package com.bearsoft.pharmadepot.validators;


import com.bearsoft.pharmadepot.models.domain.enums.MedType;
import com.bearsoft.pharmadepot.validators.annotations.EnumValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<EnumValidation, Enum<?>> {

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (value == null || !isValidEnumValue(value)) {
            isValid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid enum value").addConstraintViolation();
        }
        return isValid;
    }

    private boolean isValidEnumValue(Enum<?> value) {
        if (value instanceof MedType) {
            return Arrays.asList(MedType.values()).contains(value);
        }
        return false;
    }
}
