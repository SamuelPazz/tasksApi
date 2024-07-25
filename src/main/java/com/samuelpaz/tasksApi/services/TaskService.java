package com.samuelpaz.tasksApi.services;

import com.samuelpaz.tasksApi.models.User;
import com.samuelpaz.tasksApi.models.Task;
import com.samuelpaz.tasksApi.repositories.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException(
                "Task not found! Id: " + id + ", Type: " + Task.class.getName()));
    }

    public List<Task> findAllByUserId(Long UserId) {
        List<Task> tasks = this.taskRepository.findByUser_Id(UserId);
        return tasks;
    }

    @Transactional
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
             throw new RuntimeException(
                     "It's not possible to delete as there are related entities!");
        }
    }
}