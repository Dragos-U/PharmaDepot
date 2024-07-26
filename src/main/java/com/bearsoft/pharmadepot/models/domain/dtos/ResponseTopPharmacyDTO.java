package com.bearsoft.pharmadepot.models.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTopPharmacyDTO {

    private String pharmacyName;
    private BigDecimal totalValue;
}
