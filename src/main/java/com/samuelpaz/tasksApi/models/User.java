package com.samuelpaz.tasksApi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.samuelpaz.tasksApi.models.enums.ProfileEnum;

import jakarta.persistence.FetchType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// @Entity = Indica que a classe representa uma entidade no banco de dados.
// @Table(name = User.TABLE_NAME) = Define o nome da tabela associada à entidade.
// @AllArgsConstructor = Gera um construtor com todos os argumentos.
// @NoArgsConstructor = Gera um construtor sem argumentos.
// @Getter = Gera automaticamente os métodos getter para todos os atributos.
// @Setter = Gera automaticamente os métodos setter para todos os atributos.
// @EqualsAndHashCode = Gera automaticamente métodos equals() e hashCode().
@Entity
@Table(name = User.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {

    // Interfaces para validações específicas ao criar ou atualizar o usuário.
    public interface CreateUser {
    }

    public interface UpdateUser {
    }

    // Nome da tabela no banco de dados.
    public static final String TABLE_NAME = "user";

    // Define "id" como chave primária, faz auto_increment no valor do id, "id" é  único.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    // Define a coluna "username".
    // Valida que a senha não pode ser nula ou vazia.
    // Define o tamanho mínimo e máximo do nome de usuário.
    @Column(name = "username", length = 100, nullable = false, unique = true)
    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 2, max = 100)
    private String username;

    // Define que a senha só será escrita (não exposta no JSON de resposta).
    // Valida que a senha não pode ser nula ou vazia.
    // Valida o tamanho mínimo e máximo da senha.
    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password", length = 60, nullable = false)
    @NotNull(groups = {CreateUser.class, UpdateUser.class})
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 8, max = 60)
    private String password;

    // Define um relacionamento 1-N com a entidade Task(um usuário para muitas tarefas).
    // Esconde as tarefas do usuário na resposta JSON.
    @OneToMany(mappedBy = "user")
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Task> tasks = new ArrayList<Task>();

    // Define uma coleção de valores elementares que são carregados imediatamente.
    // Oculta os perfis do usuário no JSON de resposta.
    // Define uma tabela associativa para armazenar o tipo de perfil do usuário.
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @CollectionTable(name = "user_profile")
    @Column(name = "profile", nullable = false)
    private Set<Integer> profiles = new HashSet<>();

    // Método que retorna o perfil do usuário convertido para enum (ProfileEnum).
    public Set<ProfileEnum> getProfiles() {
        return this.profiles.stream().map(ProfileEnum::toEnum).collect(Collectors.toSet());
    }

    // Método que adiciona um perfil ao usuário, baseado no enum ProfileEnum.
    public void addProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }
}

