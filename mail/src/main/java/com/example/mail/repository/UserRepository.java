package com.example.mail.repository;

import com.example.mail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository< User , String> {

    boolean existsByUsernameOrEmail(String username,String email);
}
