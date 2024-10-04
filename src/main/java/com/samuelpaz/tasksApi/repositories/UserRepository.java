package com.samuelpaz.tasksApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuelpaz.tasksApi.models.User;

// @Repository:
// Indica que a interface é um componente do Spring e será utilizada como repositório de dados (acessa o banco de dados)
// O Spring faz o gerenciamento automático de transações e injeção de dependência para esse repositório.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
// O JpaRepository oferece métodos prontos para realizar operações CRUD (Create, Read, Update, Delete).
// User = entidade, Long = id
}