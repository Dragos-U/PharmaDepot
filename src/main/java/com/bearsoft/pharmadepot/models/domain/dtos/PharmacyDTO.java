package com.bearsoft.pharmadepot.models.domain.dtos;

import com.bearsoft.pharmadepot.models.validation.OnCreate;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PharmacyDTO {

    @NotEmpty(message = "Pharmacy's NAME must not be empty.", groups = OnCreate.class)
    private String name;

    @NotEmpty(message = "Pharmacy's PASSWORD must not be empty.", groups = OnCreate.class)
    private String password;
}
