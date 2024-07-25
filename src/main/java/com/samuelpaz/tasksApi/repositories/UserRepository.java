package com.samuelpaz.tasksApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuelpaz.tasksApi.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


}
