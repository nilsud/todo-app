package com.example.todonew.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class StatusValidator implements ConstraintValidator<StatusValidation,String> {

    public boolean isValid(String statusName, ConstraintValidatorContext ctx){
        List list= Arrays.asList(new String[]{"COMPLETED","NOT_COMPLETED"});
        return list.contains(statusName);
    }
}
