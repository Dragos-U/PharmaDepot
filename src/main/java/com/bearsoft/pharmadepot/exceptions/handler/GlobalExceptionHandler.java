package com.bearsoft.pharmadepot.exceptions.handler;

import com.bearsoft.pharmadepot.exceptions.constraints.ConstraintViolationException;
import com.bearsoft.pharmadepot.exceptions.constraints.ObjectNotValidException;
import com.bearsoft.pharmadepot.exceptions.pharmacy.*;
import com.bearsoft.pharmadepot.exceptions.product.ProductAlreadyExistsException;
import com.bearsoft.pharmadepot.exceptions.product.ProductNotFoundException;
import com.bearsoft.pharmadepot.exceptions.ratelimiter.RateLimiterException;
import com.bearsoft.pharmadepot.models.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final HttpStatus CONFLICT = HttpStatus.CONFLICT;
    private static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;

    private static ZonedDateTime getCurrentTimeStamp() {
        return ZonedDateTime.now(ZoneId.of("Z"));
    }

    private static ResponseEntity<Object> getResponseEntity(String httpMessageNotReadableException, HttpStatus badRequest) {
        ApiException apiException = ApiException.builder()
                .message(httpMessageNotReadableException)
                .httpStatus(badRequest)
                .timeStamp(getCurrentTimeStamp())
                .build();
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> handleLoginException(LoginException loginException) {
        log.error("LoginException occurred: {}", loginException.getMessage(), loginException);
        return getResponseEntity(loginException.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        log.error("HttpMessageNotReadableException occurred: {}", httpMessageNotReadableException.getMessage(), httpMessageNotReadableException);
        return getResponseEntity(httpMessageNotReadableException.getMessage(), BAD_REQUEST);
    }


    @ExceptionHandler(RateLimiterException.class)
    public ResponseEntity<Object> handleRateLimiterException(RateLimiterException limiterException) {
        log.error("HttpMessageNotReadableException occurred: {}", limiterException.getMessage(), limiterException);
        return getResponseEntity(limiterException.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<Object> handleObjectNotValidException(ObjectNotValidException objectNotValidException) {
        log.error("ObjectNotValidException occurred: {}", objectNotValidException.getMessage(), objectNotValidException);
        Set<String> errorMessages = objectNotValidException.getErrorMessages();
        String combinedErrorMessage = String.join(", ", errorMessages);
        return getResponseEntity(combinedErrorMessage, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handlerConstraintViolationException(ConstraintViolationException constraintViolationException) {
        log.error("ConstraintViolationException occurred: {}", constraintViolationException.getMessage(), constraintViolationException);
        return getResponseEntity(constraintViolationException.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(PasswordDoesNotMatchException.class)
    public ResponseEntity<Object> handlerPasswordDoesNotMatchException(PasswordDoesNotMatchException passwordDoesNotMatchException) {
        log.error("PasswordDoesNotMatchException occurred: {}", passwordDoesNotMatchException.getMessage(), passwordDoesNotMatchException);
        return getResponseEntity(passwordDoesNotMatchException.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(PharmacyNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(PharmacyNotFoundException appUserNotFoundException) {
        log.error("UserNotFoundException occurred: {}", appUserNotFoundException.getMessage(), appUserNotFoundException);
        return getResponseEntity(appUserNotFoundException.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPharmacyAuthenticationException.class)
    public ResponseEntity<Object> handlerInvalidUserAuthenticationException(InvalidPharmacyAuthenticationException invalidUserAuthenticationException) {
        log.error("InvalidUserAuthenticationException occurred: {}", invalidUserAuthenticationException.getMessage(), invalidUserAuthenticationException);
        return getResponseEntity(invalidUserAuthenticationException.getMessage(), UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<Object> handlerInvalidSortFieldException(InvalidSortFieldException invalidSortFieldException) {
        log.error("InvalidSortFieldException occurred: {}", invalidSortFieldException.getMessage());
        return getResponseEntity(invalidSortFieldException.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Object> handlerTokenNotFoundException(TokenNotFoundException tokenNotFoundException) {
        log.error("TokenNotFoundException occurred: {}", tokenNotFoundException.getMessage());
        return getResponseEntity(tokenNotFoundException.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(PharmacyAlreadyExistsException.class)
    public ResponseEntity<Object> handlerAppUserAlreadyExistsException(PharmacyAlreadyExistsException appUserAlreadyExistsException) {
        log.error("PharmacyAlreadyExistsException occurred: {}", appUserAlreadyExistsException.getMessage(), appUserAlreadyExistsException);
        return getResponseEntity(appUserAlreadyExistsException.getMessage(), CONFLICT);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Object> handlerProductAlreadyExistsException(ProductAlreadyExistsException productAlreadyExistsException) {
        log.error("productAlreadyExistsException occurred: {}", productAlreadyExistsException.getMessage(), productAlreadyExistsException);
        return getResponseEntity(productAlreadyExistsException.getMessage(), CONFLICT);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handlerProductNotFoundException(ProductNotFoundException productNotFoundException) {
        log.error("productNotFoundException occurred: {}", productNotFoundException.getMessage(), productNotFoundException);
        return getResponseEntity(productNotFoundException.getMessage(), BAD_REQUEST);
    }

}