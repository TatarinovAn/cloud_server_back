package ru.netology.cloud_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloud_server.entity.User;

import java.util.Optional;


@Repository
public interface CloudRepositoryUsers extends JpaRepository<User, Long> {

    //получить пользователя по имени
    Optional<User> findByUsername(String username);


}
