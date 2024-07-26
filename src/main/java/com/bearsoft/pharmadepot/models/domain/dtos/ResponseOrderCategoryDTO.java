package com.bearsoft.pharmadepot.models.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOrderCategoryDTO {

    private String categoryName;
    private Long totalOrders;
}
