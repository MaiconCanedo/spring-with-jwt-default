package br.com.maicon.pratica.jwt.resource.exception;

import br.com.maicon.pratica.jwt.dominio.exception.ForbiddenException;
import br.com.maicon.pratica.jwt.dominio.exception.NoContentException;
import br.com.maicon.pratica.jwt.dominio.exception.NotFoundException;
import br.com.maicon.pratica.jwt.dominio.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    private ResponseEntity<StandardError> standardHandlingOfExceptions(
            HttpServletRequest request,
            HttpStatus httpStatus,
            Exception exception) {
        return ResponseEntity.status(httpStatus).body(StandardError.Builder.create()
                .now()
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardError> unauthorized(UnauthorizedException exception,
                                                      HttpServletRequest request) {
        return standardHandlingOfExceptions(request, HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StandardError> forbidden(ForbiddenException exception,
                                                   HttpServletRequest request) {
        return standardHandlingOfExceptions(request, HttpStatus.FORBIDDEN, exception);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<StandardError> noContent(NoContentException exception,
                                                   HttpServletRequest request) {
        return standardHandlingOfExceptions(request, HttpStatus.NO_CONTENT, exception);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> notFound(NotFoundException exception,
                                                  HttpServletRequest request) {
        return standardHandlingOfExceptions(request, HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler
    public ResponseEntity<StandardError> badRequest(Exception exception,
                                                    HttpServletRequest request) {
        return standardHandlingOfExceptions(request, HttpStatus.BAD_REQUEST, exception);
    }
}