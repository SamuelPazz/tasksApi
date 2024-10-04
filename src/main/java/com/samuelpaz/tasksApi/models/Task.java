package com.samuelpaz.tasksApi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;


// @Entity = Indica que a classe representa uma entidade no banco de dados.
// @Table(name = Task.TABLE_NAME) = Define o nome da tabela associada à entidade.
// @AllArgsConstructor = Gera um construtor com todos os argumentos.
// @NoArgsConstructor = Gera um construtor sem argumentos.
// @Getter = Gera automaticamente os métodos getter para todos os atributos.
// @Setter = Gera automaticamente os métodos setter para todos os atributos.
// @EqualsAndHashCode = Gera automaticamente métodos equals() e hashCode().
@Entity
@Table(name = Task.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Task {

    // Nome da tabela no banco de dados.
    public static final String TABLE_NAME = "task";

    // Define "id" como chave primária, faz auto_increment no valor do id, "id" é  único.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    // Define um relacionamento N-1 com a entidade User(muitas tarefas para um usuário).
    // Cria a coluna user_id que é uma chave estrangeira que faz referência ao id da entidade user e a associação não pode ser nula nem atualizável
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    // Define o campo description, com tamanho máximo de 255 caracteres e não pode ser null
    // Valida que a senha não pode ser nula ou vazia.
    // Valida o tamanho mínimo e máximo da senha.
    @Column(name = "description", length = 255, nullable = false)
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 255)
    private String description;
}
