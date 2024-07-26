package com.bearsoft.pharmadepot.services.security;

import com.bearsoft.pharmadepot.exceptions.pharmacy.*;
import com.bearsoft.pharmadepot.models.security.SecurityPharmacy;
import com.bearsoft.pharmadepot.models.domain.dtos.AuthenticationRequestDTO;
import com.bearsoft.pharmadepot.models.domain.dtos.AuthenticationResponseDTO;
import com.bearsoft.pharmadepot.models.domain.dtos.PharmacyDTO;
import com.bearsoft.pharmadepot.models.domain.entities.Pharmacy;
import com.bearsoft.pharmadepot.models.domain.entities.Role;
import com.bearsoft.pharmadepot.models.domain.enums.RoleType;
import com.bearsoft.pharmadepot.models.validation.OnCreate;
import com.bearsoft.pharmadepot.repositories.PharmacyRepository;
import com.bearsoft.pharmadepot.validators.ObjectsValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    public static final String USERNAME_NOT_FOUND = "Username not found.";
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHENTICATION_FAILED = "Authentication failed.";
    public static final String WRONG_CREDENTIALS = " Wrong credentials.";

    private final PharmacyRepository pharmacyRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtFilterService jwtFilterService;
    private final PasswordEncoder passwordEncoder;

    private final ObjectsValidator<PharmacyDTO> appUserDTOValidator;
    private final ObjectsValidator<AuthenticationRequestDTO> authenticationRequestDTOObjectsValidator;


    @Transactional
    public AuthenticationResponseDTO registerPharmacy(PharmacyDTO pharmacyDTO) {
        appUserDTOValidator.validate(pharmacyDTO, OnCreate.class);

        Optional<Pharmacy> existingUser = pharmacyRepository.findPharmaciesByName(pharmacyDTO.getName());
        if (existingUser.isPresent()) {
            throw new PharmacyAlreadyExistsException("Pharmacy name is already used.");
        }

        var appUser = Pharmacy.builder()
                .name(pharmacyDTO.getName())
                .password(passwordEncoder.encode(pharmacyDTO.getPassword()))
                .roles(new HashSet<>(List.of(Role.builder().roleType(RoleType.ROLE_USER).build())))
                .build();
        pharmacyRepository.save(appUser);

        var securityAppUser = new SecurityPharmacy(appUser);

        return generateTokens(securityAppUser);
    }

    public AuthenticationResponseDTO loginPharmacy(AuthenticationRequestDTO authenticationRequestDTO) {
        authenticationRequestDTOObjectsValidator.validate(authenticationRequestDTO);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequestDTO.getName(),
                            authenticationRequestDTO.getPassword()));
        } catch (UsernameNotFoundException usernameNotFoundException) {
            log.error(AUTHENTICATION_FAILED + USERNAME_NOT_FOUND + " {}", usernameNotFoundException.getMessage());
            throw new PharmacyNotFoundException(USERNAME_NOT_FOUND);
        } catch (BadCredentialsException badCredentialsException) {
            log.error(AUTHENTICATION_FAILED + WRONG_CREDENTIALS + " {}", badCredentialsException.getMessage());
            throw new PasswordDoesNotMatchException(WRONG_CREDENTIALS);
        } catch (Exception e) {
            log.error(AUTHENTICATION_FAILED + e.getMessage());
            throw new LoginException("Login failed");
        }

        var pharmacy = pharmacyRepository.findPharmaciesByName(authenticationRequestDTO.getName() )
                .orElseThrow(() -> new PharmacyNotFoundException(USERNAME_NOT_FOUND));
        var securityPharmacy = new SecurityPharmacy(pharmacy);

        return generateTokens(securityPharmacy);
    }

    public void refreshLoggedUserToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String refreshToken = extractTokenFromRequest(request, BEARER_PREFIX);
        if (refreshToken == null) {
            throw new TokenNotFoundException("The request has no token in the header.");
        }

        String pharmacyName = jwtFilterService.extractPharmacyName(refreshToken);
        Optional<Pharmacy> appUserOptional = pharmacyRepository.findPharmaciesByName(pharmacyName);

        if (appUserOptional.isPresent() && isTokenValidForUser(refreshToken, appUserOptional.get())) {
            var securityAppUser = new SecurityPharmacy(appUserOptional.get());
            String accessToken = jwtFilterService.generateToken(securityAppUser);
            sendAuthenticationResponse(accessToken, refreshToken, response);
        }
    }

    private String extractTokenFromRequest(
            HttpServletRequest request,
            String tokenPrefix) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(tokenPrefix)) {
            return authHeader.substring(tokenPrefix.length());
        }
        return null;
    }

    private boolean isTokenValidForUser(String token, Pharmacy pharmacy) {
        var securityAppUser = new SecurityPharmacy(pharmacy);
        return jwtFilterService.isTokenValid(token, securityAppUser);
    }

    private void sendAuthenticationResponse(String accessToken, String refreshToken, HttpServletResponse response) throws IOException {
        var authResponse = AuthenticationResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }

    private AuthenticationResponseDTO generateTokens(SecurityPharmacy securityPharmacy) {
        String jwtToken = jwtFilterService.generateToken(securityPharmacy);
        log.info("New token was generated");
        String refreshToken = jwtFilterService.generateRefreshToken(securityPharmacy);
        log.info("New refresh was generated");
        log.info("Role assigned "+ securityPharmacy.getAuthorities());
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
