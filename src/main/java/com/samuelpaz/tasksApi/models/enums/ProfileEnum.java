package com.samuelpaz.tasksApi.models.enums;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

// AllArgsConstructor = Gera um construtor com todos os argumentos da classe
// @Getter = Gera os métodos getter para os atributos da enumeração
@AllArgsConstructor
@Getter
public enum ProfileEnum {

    // Definição de dois perfis: ADMIN e USER, cada um com um código e uma descrição
    ADMIN(1, "ROLE_ADMIN"),
    USER(2, "ROLE_USER");

    // Atributos associados a cada enum (um código e uma descrição)
    private final Integer code;
    private final String description;

    // Método para converter um código inteiro em um valor do enum ProfileEnum
    public static ProfileEnum toEnum(Integer code) {
        // Se o código for nulo, retorna null
        if (Objects.isNull(code))
            return null;

        // Loop pelos valores do enum para encontrar o que tem o código correspondente
        for (ProfileEnum x : ProfileEnum.values()) {
            if (code.equals(x.getCode()))
                return x;  // Retorna o enum correspondente ao código
        }

        // Se nenhum código for encontrado, lança uma exceção indicando que o código é inválido
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
