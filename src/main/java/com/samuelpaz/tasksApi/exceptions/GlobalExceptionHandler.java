package com.samuelpaz.tasksApi.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.samuelpaz.tasksApi.services.exceptions.DataBindingViolationException;
import com.samuelpaz.tasksApi.services.exceptions.ObjectNotFoundException;

import lombok.extern.slf4j.Slf4j;

// @Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER") = Adiciona logs ao manipulador de exceções usando o SLF4J
// @RestControllerAdvice = Indica que esta classe fornece conselhos globais para todos os controladores
// @ResponseStatus(HttpStatus.status) = Define o status HTTP
// @ExceptionHandler(Exception.class) =
@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Injeta o StackTrace nos erros caso a boolean for true no application-properties
    @Value("${server.error.include-exception}")
    private boolean printStackTrace;

    /*
    Sobrescreve o método MethodArgumentNotValidException da classe ResponseEntityExceptionHandler
    para tratar a exceção MethodArgumentNotValidException (validação de argumentos inválidos)
    */
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // Cria uma resposta de erro para validação
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.");

        // Adiciona detalhes de erro de campo à resposta
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    // Manipula todas as exceções não tratadas
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {

        // Mensagem de erro padrão
        final String errorMessage = "Unknown error occurred";
        // Registra a exceção
        log.error(errorMessage, exception);
        return buildErrorResponse(
                exception,
                errorMessage,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    // Manipula exceções de violação de integridade de dados (banco de dados)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException dataIntegrityViolationException,
            WebRequest request) {

        // Pega a causa específica
        String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
        // Registra o erro
        log.error("Failed to save entity with integrity problems: " + errorMessage, dataIntegrityViolationException);
        return buildErrorResponse(
                dataIntegrityViolationException,
                errorMessage,
                HttpStatus.CONFLICT,
                request);
    }

    // Manipula exceções de violação de restrições de validação
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException constraintViolationException,
            WebRequest request) {

        // Registra o erro
        log.error("Failed to validate element", constraintViolationException);
        return buildErrorResponse(
                constraintViolationException,
                HttpStatus.UNPROCESSABLE_ENTITY,
                request);
    }

    // Manipula exceções quando o objeto não é encontrado (404)
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException objectNotFoundException,
            WebRequest request) {

        // Registra o erro
        log.error("Failed to find the requested element", objectNotFoundException);
        return buildErrorResponse(
                objectNotFoundException,
                HttpStatus.NOT_FOUND,
                request);
    }

    // Manipula exceções de violação de ligação de dados (vínculos de dados não consistentes)
    @ExceptionHandler(DataBindingViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolationException(
            DataBindingViolationException dataBindingViolationException,
            WebRequest request) {

        // Registra o erro
        log.error("Failed to save entity with associated data", dataBindingViolationException);
        return buildErrorResponse(
                dataBindingViolationException,
                HttpStatus.CONFLICT,
                request);
    }

    // Constrói a resposta de erro com status HTTP e detalhes da exceção
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    // Constrói a resposta de erro com mensagem personalizada, status e detalhes da exceção
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {

        // Cria o objeto de resposta de erro
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        // Adiciona a stack trace se a propriedade estiver configurada com true para exibir
        if (this.printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
