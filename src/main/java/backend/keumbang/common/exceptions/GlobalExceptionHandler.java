package backend.keumbang.common.exceptions;

import backend.keumbang.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(GeneralException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto("관리자에게 문의바랍니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlreadyExistUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)  // HTTP 409 Conflict
    public ErrorResponseDto handleAlreadyExistUserException(AlreadyExistUserException e) {
        return new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleBadCredentialsException(BadCredentialsException e) {
        return new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
