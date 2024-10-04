package com.samuelpaz.tasksApi.services;

import com.samuelpaz.tasksApi.models.User;
import com.samuelpaz.tasksApi.models.Task;
import com.samuelpaz.tasksApi.repositories.TaskRepository;
import com.samuelpaz.tasksApi.services.exceptions.DataBindingViolationException;
import com.samuelpaz.tasksApi.services.exceptions.ObjectNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

// @Service: indica que a classe é um serviço do Spring.
// Serviços contêm a regra de negócio e interagem com os repositórios.
@Service
public class TaskService {

    // Injeção das dependências do repositório de tarefas e serviço de usuários
    private final TaskRepository taskRepository;
    private final UserService userService;

    // Construtor da classe, injeta o repositório e o serviço de usuário
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    // Método que encontra uma tarefa pelo ID, lança exceção se a tarefa não for encontrada
    @Transactional
    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new ObjectNotFoundException(
                "Task not found! Id: " + id + ", Type: " + Task.class.getName()));
    }

    // Método que encontra todas as tarefas associadas a um determinado ID de usuário
    public List<Task> findAllByUserId(Long userId) {
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;
    }

    // Criação de uma nova tarefa. Valida o usuário e salva a tarefa
    @Transactional
    public Task create(Task obj) {
        // Verifica se o usuário existe
        // Define o ID da tarefa como null para garantir a criação de uma nova tarefa
        // Salva a tarefa no banco de dados
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    // Atualiza uma tarefa existente. Busca a tarefa e atualiza os campos permitidos
    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());    // Atualiza apenas a descrição da tarefa
        return this.taskRepository.save(newObj);
    }

    // Deleta uma tarefa pelo ID. Lança exceção se houver entidades relacionadas
    public void delete(Long id) {
        // Verifica se a tarefa existe
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            // Lança exceção personalizada se houver problemas com dados relacionados
            throw new DataBindingViolationException(
                    "It's not possible to delete as there are related entities!");
        }
    }
}
