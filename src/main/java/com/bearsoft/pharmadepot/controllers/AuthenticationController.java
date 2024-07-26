package com.bearsoft.pharmadepot.controllers;

import com.bearsoft.pharmadepot.models.domain.dtos.AuthenticationRequestDTO;
import com.bearsoft.pharmadepot.models.domain.dtos.AuthenticationResponseDTO;
import com.bearsoft.pharmadepot.models.domain.dtos.PharmacyDTO;
import com.bearsoft.pharmadepot.services.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public ResponseEntity<AuthenticationResponseDTO> registerPharmacy(
            @RequestBody PharmacyDTO pharmacyDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService
                        .registerPharmacy(pharmacyDTO));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> loginPharmacy(
            @RequestBody AuthenticationRequestDTO authenticationRequestDTO){
        return ResponseEntity.ok(authenticationService
                .loginPharmacy(authenticationRequestDTO));
    }

    @PostMapping("/refresh-token")
    public void refreshLoggedUserToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authenticationService.refreshLoggedUserToken(request, response);
    }
}
