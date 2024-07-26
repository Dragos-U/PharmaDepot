package com.bearsoft.pharmadepot.controllers;

import com.bearsoft.pharmadepot.models.domain.dtos.OrderDTO;
import com.bearsoft.pharmadepot.models.domain.dtos.ResponseOrderCategoryDTO;
import com.bearsoft.pharmadepot.models.domain.dtos.ResponseOrderSummaryDTO;
import com.bearsoft.pharmadepot.models.domain.dtos.ResponseTopPharmacyDTO;
import com.bearsoft.pharmadepot.services.models.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<OrderDTO>> placeOrder(
            @RequestBody List<OrderDTO> orderDTOList,
            Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService
                        .placeOrder(orderDTOList, principal));
    }

    @GetMapping("/monthly-orders/{pharmacyName}/{year}/{month}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseOrderSummaryDTO> getOrdersByPharmacyAndMonth(
            @PathVariable String pharmacyName,
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrdersByPharmacyAndMonth(pharmacyName, year, month));
    }

    @GetMapping("/category-orders/{pharmacyName}/{year}/{category}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseOrderCategoryDTO> getOrdersByPharmacyAndCategory(
            @PathVariable String pharmacyName,
            @PathVariable int year,
            @PathVariable String category) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrdersByPharmacyAndCategory(pharmacyName, year, category));
    }

    @GetMapping("/top-pharmacy/{year}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseTopPharmacyDTO> getTopPharmacyByYear(
            @PathVariable int year) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getTopPharmacyByYear(year));
    }
}
