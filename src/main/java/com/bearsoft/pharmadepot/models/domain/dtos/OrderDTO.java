package com.bearsoft.pharmadepot.models.domain.dtos;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {

    private String name;

    @Min(value = 0)
    private int quantity;
}
