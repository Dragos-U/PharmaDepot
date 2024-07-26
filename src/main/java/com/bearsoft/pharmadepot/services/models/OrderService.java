package com.bearsoft.pharmadepot.services.models;

import com.bearsoft.pharmadepot.exceptions.pharmacy.InvalidPharmacyAuthenticationException;
import com.bearsoft.pharmadepot.exceptions.product.ProductNotFoundException;
import com.bearsoft.pharmadepot.models.domain.dtos.ResponseOrderCategoryDTO;
import com.bearsoft.pharmadepot.models.domain.dtos.ResponseOrderSummaryDTO;
import com.bearsoft.pharmadepot.models.domain.dtos.ResponseTopPharmacyDTO;
import com.bearsoft.pharmadepot.models.domain.enums.MedType;
import com.bearsoft.pharmadepot.models.security.SecurityPharmacy;
import com.bearsoft.pharmadepot.models.domain.dtos.OrderDTO;
import com.bearsoft.pharmadepot.models.domain.entities.OrderItem;
import com.bearsoft.pharmadepot.models.domain.entities.PharmacyOrder;
import com.bearsoft.pharmadepot.models.domain.entities.Product;
import com.bearsoft.pharmadepot.repositories.OrderRepository;
import com.bearsoft.pharmadepot.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public List<OrderDTO> placeOrder(List<OrderDTO> orderDTOList, Principal principal) {
        if (orderDTOList == null || principal == null) {
            throw new IllegalArgumentException("OrderDTO list and principal cannot be null");
        }

        SecurityPharmacy securityPharmacy = extractPharmacyFromPrincipal(principal);
        var pharmacy = securityPharmacy.getPharmacy();
        LocalDate date = LocalDate.now();

        PharmacyOrder pharmacyOrder = PharmacyOrder.builder()
                .pharmacy(pharmacy)
                .localDate(date)
                .build();

        PharmacyOrder savedPharmacyOrder = orderRepository.save(pharmacyOrder);

        List<OrderItem> orderItems = orderDTOList.stream()
                .map(orderDTO -> {
                    Product product = productRepository.findByName(orderDTO.getName())
                            .orElseThrow(() -> new ProductNotFoundException("Product not found: " + orderDTO.getName()));

                    return OrderItem.builder()
                            .pharmacyOrder(savedPharmacyOrder)
                            .product(product)
                            .quantity(orderDTO.getQuantity())
                            .build();
                })
                .toList();

        savedPharmacyOrder.setOrderItems(orderItems);
        orderRepository.save(savedPharmacyOrder);

        return orderDTOList;
    }


    private SecurityPharmacy extractPharmacyFromPrincipal(Principal connectedPharmacy) {
        checkConnectedUserAuthentication(connectedPharmacy);
        return (SecurityPharmacy) ((UsernamePasswordAuthenticationToken) connectedPharmacy).getPrincipal();
    }

    private void checkConnectedUserAuthentication(Principal connectedPharmacy) {
        if (!(connectedPharmacy instanceof UsernamePasswordAuthenticationToken)) {
            throw new InvalidPharmacyAuthenticationException("Invalid user authentication");
        }
    }

    public ResponseOrderSummaryDTO getOrdersByPharmacyAndMonth(String pharmacyName, int year, int month) {
        List<Object[]> results = orderRepository.findOrdersByPharmacyAndMonth(pharmacyName, year, month);

        Object[] result = results.get(0);
        Long totalOrders = result[0] != null ? ((Number) result[0]).longValue() : 0L;
        BigDecimal totalSum = result[1] != null ? convertToBigDecimal(result[1]) : BigDecimal.ZERO;
        BigDecimal averageValue = result[2] != null ? convertToBigDecimal(result[2]) : BigDecimal.ZERO;

        return new ResponseOrderSummaryDTO(totalOrders, totalSum, averageValue);
    }

    public ResponseOrderCategoryDTO getOrdersByPharmacyAndCategory(String pharmacyName, int year, String category) {
        MedType medType;
        try {
            medType = MedType.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + category);
        }

        List<Object[]> results = orderRepository.findOrdersByPharmacyAndCategory(pharmacyName, year, medType);
        Object[] result = results.get(0);
        String categoryName = medType.name();
        Long totalOrders = result[1] != null ? ((Number) result[1]).longValue() : 0L;

        return new ResponseOrderCategoryDTO(categoryName, totalOrders);
    }

    public ResponseTopPharmacyDTO getTopPharmacyByYear(int year) {
        List<Object[]> results = orderRepository.findTopPharmacyByYear(year);
        Object[] topResult = results.get(0);
        String pharmacyName = (String) topResult[0];
        BigDecimal totalValue = topResult[1] != null ? (BigDecimal) topResult[1] : BigDecimal.ZERO;

        return new ResponseTopPharmacyDTO(pharmacyName, totalValue);
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return BigDecimal.valueOf((Double) value);
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        } else {
            throw new IllegalArgumentException("Cannot convert value of type " + value.getClass() + " to BigDecimal");
        }
    }
}
