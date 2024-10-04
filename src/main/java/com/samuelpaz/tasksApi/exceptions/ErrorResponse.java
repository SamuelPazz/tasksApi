package com.samuelpaz.tasksApi.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// @Getter = Gera métodos getter para os campos da classe
// @Setter = Gera métodos setter para os campos da classe
// @RequiredArgsConstructor = Gera um construtor para os campos (final), que são obrigatórios
// @JsonInclude(JsonInclude.Include.NON_NULL) = Inclui apenas campos não nulos na serialização JSON

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    // status http do erro, mensagem do erro, rastro do erro, lista de erros de validação
    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationError {
        // nome do campo que causou o erro, mensagem do erro
        private final String field;
        private final String message;
    }

    // Adiciona um erro de validação na lista de erros
    public void addValidationError(String field, String message) {
        // Inicializa a lista se estiver nula
        if (Objects.isNull(errors)) {
            this.errors = new ArrayList<>();
        }
        // Adiciona o novo erro de validação
        this.errors.add(new ValidationError(field, message));
    }

}
