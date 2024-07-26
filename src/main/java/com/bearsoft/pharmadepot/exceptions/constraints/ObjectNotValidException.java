package com.bearsoft.pharmadepot.exceptions.constraints;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false)
public class ObjectNotValidException extends RuntimeException{

    private final Set<String> errorMessages;
}
