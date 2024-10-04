package com.samuelpaz.tasksApi.services.exceptions;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Define que a resposta HTTP será 404 (Not Found) quando esta exceção for lançada
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends EntityNotFoundException {

    // Construtor que recebe uma mensagem personalizada de erro
    public ObjectNotFoundException(String message) {
        // Passa a mensagem para o construtor da classe EntityNotFoundException
        super(message);
    }

}

