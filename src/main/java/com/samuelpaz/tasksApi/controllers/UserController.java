package com.samuelpaz.tasksApi.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.samuelpaz.tasksApi.models.User;
import com.samuelpaz.tasksApi.services.UserService;

import java.net.URI;

// @RestController = Define a classe como um controlador REST
// @RequestMapping = Define o endpoint base para todos os métodos do controller
// @Validated = Habilita a validação de métodos nesta classe
// @Validated(User.CreateUser.class) = Valida o objeto User usando a interface CreateUser em todos os campos do user
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    // Injeção do service user
    private final UserService userService;

    // Construtor que injeta o service
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint para buscar um usuário pelo id
    // Busca e retorna status 200
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User obj = this.userService.findById(id);  // Busca o usuário pelo ID
        return ResponseEntity.ok().body(obj);  // Retorna o usuário encontrado com status 200 OK
    }

    // Endpoint para criar usuário
    // Cria um novo usuário, constroi a URI do novo usuário, retorna 201
    @PostMapping
    @Validated(User.CreateUser.class)
    public ResponseEntity<Void> create(@Valid @RequestBody User obj) {
        this.userService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // Endpoint para atualizar usuário
    // Pega o id do usuário que vai ser atualizado, atualiza o usuário, retorna status 204
    @PutMapping("/{id}")
    @Validated(User.UpdateUser.class)
    public ResponseEntity<Void> update(@Valid @RequestBody User obj, @PathVariable Long id) {
        obj.setId(id);
        this.userService.update(obj);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para deletar usuário
    /// Pega o id do usuário a ser deletado, retorna 204
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
