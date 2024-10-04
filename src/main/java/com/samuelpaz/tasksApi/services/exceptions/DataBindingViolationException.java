package com.samuelpaz.tasksApi.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Indica que, ao lançar esta exceção, a resposta HTTP terá o status 404 (Not Found)
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataBindingViolationException extends DataIntegrityViolationException {

    // Construtor que aceita uma mensagem personalizada de erro
    public DataBindingViolationException(String message) {
        // Chama o construtor da superclasse (DataIntegrityViolationException) com a mensagem fornecida
        super(message);
    }
}
