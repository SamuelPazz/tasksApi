package com.samuelpaz.tasksApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuelpaz.tasksApi.models.Task;

import java.util.List;

// @Repository:
// Indica que a interface é um componente do Spring e será utilizada como repositório de dados (acessa o banco de dados)
// O Spring faz o gerenciamento automático de transações e injeção de dependência para esse repositório.
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

// O JpaRepository oferece métodos prontos para realizar operações CRUD (Create, Read, Update, Delete).
// Task = entidade, Long = id

    // Método que busca todas as tarefas associadas a um usuário específico, com base no ID do usuário
    List<Task> findByUser_Id(Long id);

    /*
    Outras formas:
     // Define uma consulta JPQL para buscar tarefas com base no ID do usuário
     @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
     List<Task> findByUser_Id(@Param("id") Long id);

     // Define uma consulta SQL nativa para buscar tarefas pelo ID do usuário
     @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
     List<Task> findByUser_Id(@Param("id") Long id);
    */
}

