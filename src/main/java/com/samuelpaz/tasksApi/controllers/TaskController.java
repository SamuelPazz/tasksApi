package com.samuelpaz.tasksApi.controllers;

import com.samuelpaz.tasksApi.services.TaskService;
import com.samuelpaz.tasksApi.models.Task;
import com.samuelpaz.tasksApi.services.UserService;

import jakarta.validation.Valid;

import java.util.List;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// @RestController = Define a classe como um controlador REST
// @RequestMapping = Define o endpoint base para todos os métodos do controller
// @Validated = Habilita a validação para os parâmetros da classe e também habilita a validação em um método
@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
    // Injeção dos services de task e user
    private final TaskService taskService;
    private final UserService userService;

    // Construtor que injeta os services
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // Endpoint para buscar uma tarefa pelo ID
    // Busca a tarefa pelo ID e retorna a tarefa com status 200
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        Task obj = this.taskService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    // Endpoint para buscar todas as tarefas de um usuário específico
    // Verifica se o usuário existe, busca todas as tarefas do usuário e retorna a lista de tarefas com status 200
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> findAllByUserId(@PathVariable Long userId) {
        this.userService.findById(userId);
        List<Task> objs = this.taskService.findAllByUserId(userId);
        return ResponseEntity.ok().body(objs);
    }

    // Endpoint para criar uma nova tarefa
    // Valida com base no objeto Task (model)
    // Cria a tarefa, constrói a URI da nova tarefa (localhost:8080/task/{novatarefa})
    // Retorna status 200 com a URI da tarefa nova
    @PostMapping
    @Validated
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj) {
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // Endpoint para atualizar uma tarefa existente
    // Pega o ID da tarefa, atualiza a tarefa, retorna status 204
    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id) {
        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para deletar uma tarefa pelo ID
    // Deleta e retorna status 204
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
