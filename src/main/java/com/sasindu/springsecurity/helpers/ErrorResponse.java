package com.sasindu.springsecurity.helpers;

import com.sasindu.springsecurity.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

public class ErrorResponse {

    //! Handle all exceptions
    private static ResponseEntity<ApiResponse> handleAllExceptions(HttpStatus status, Exception ex) {
        return ResponseEntity.status(status.value())
                .body(new ApiResponse(ex.getMessage(), null, null));
    }

    //! Static method to handle exceptions manually with a custom message
    public static ResponseEntity<ApiResponse> handleError(Exception e) {
        //? Bad Request Exception - Return 400
        if (e instanceof BadRequestException) {
            return handleAllExceptions(HttpStatus.BAD_REQUEST, e);
        }

        //? Unauthorized Exception - Return 401
        if (e instanceof UnAuthorizedException) {
            return handleAllExceptions(HttpStatus.UNAUTHORIZED, e);
        }

        //? Forbidden Exception - Return 403
        if (e instanceof ForbiddenException) {
            return handleAllExceptions(HttpStatus.FORBIDDEN, e);
        }

        //? Not Found Exception - Return 404
        if (e instanceof NotFoundException) {
            return handleAllExceptions(HttpStatus.NOT_FOUND, e);
        }

        //? Conflict Exception - Return 409
        if (e instanceof ConflictException) {
            return handleAllExceptions(HttpStatus.CONFLICT, e);
        }

        //? Bad credentials Exception - Return 401
        if (e instanceof BadCredentialsException) {
            return handleAllExceptions(HttpStatus.UNAUTHORIZED, e);
        }

        //? Generic Exception - Return 500
        return handleAllExceptions(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
