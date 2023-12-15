package com.noteapp.auth.repository;
import com.noteapp.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// DAO
public interface UserRepository extends JpaRepository<User, String> {
}
