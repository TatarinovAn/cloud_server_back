package ru.netology.cloud_server.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.cloud_server.entity.File;
import ru.netology.cloud_server.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloudRepositoryFiles extends JpaRepository<File, Long> {

    //Получить список всех файлов пользователя из бд


    //@Query(value = "SELECT * FROM users u left join files f on u.id = f.user_id where f.user_id = :id limit :limit", nativeQuery = true)
    List<File> findAllByUser(User user, Limit limit);

    // Получить файл пользователя по имени
    Optional<File> findByUserAndFileName(User user, String fileName);

    // удалить файл из бд
    void deleteByUserAndFileName(User user, String fileName);
}