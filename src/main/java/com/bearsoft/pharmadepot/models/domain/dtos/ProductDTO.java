package com.bearsoft.pharmadepot.models.domain.dtos;

import com.bearsoft.pharmadepot.models.validation.OnCreate;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDTO {

    @NotEmpty(message = "Product's NAME must not be empty.", groups = OnCreate.class)
    private String name;

    @NotEmpty(message = "Product's PRICE must not be empty.", groups = OnCreate.class)
    private BigDecimal price;

    @NotEmpty(message = "Product's CATEGORY must not be empty.", groups = OnCreate.class)
    private String category;
}
