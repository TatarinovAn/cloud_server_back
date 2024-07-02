package ru.netology.cloud_server.service;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_server.entity.User;
import ru.netology.cloud_server.dto.EditFileRequest;
import ru.netology.cloud_server.dto.ListResponse;
import ru.netology.cloud_server.entity.File;
import ru.netology.cloud_server.exception.InputDataException;
import ru.netology.cloud_server.exception.ServerException;
import ru.netology.cloud_server.repository.CloudRepositoryFiles;
import ru.netology.cloud_server.repository.CloudRepositoryUsers;
import ru.netology.cloud_server.utils.Logger;


import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Data
@Service
public class CloudServiceFile {

    private final CloudRepositoryFiles cloudRepositoryFiles;

    private final CloudRepositoryUsers cloudRepositoryUsers;
    private final Logger log = Logger.getInstance();

    //Загрузить файл в бд

    public void uploadFile(String filename, MultipartFile file, User user) {
        try {

            cloudRepositoryFiles.save(new File(filename, file.getSize(), file.getBytes(), user));
        } catch (InputDataException e) {
            log.writeLog(STR."Error upload File: \{filename}");
            throw e;
        } catch (Exception e) {
            log.writeLog(STR."Error upload File: \{filename}");
            throw new ServerException("Error upload File");
        }
    }

    // Получить файл из бд
    public List<File> getFileList(Integer limit, User user) {
        try {
            System.out.println(user.getUsername());
            var file = cloudRepositoryFiles.findAllByUser(user, Limit.of(limit));
//            for (File s : file) {
//                System.out.println(s.getFileName());
//                System.out.println(s.getFileSize());
//            }
            return file;
        } catch (InputDataException e) {
            log.writeLog("Error get File List");
            throw e;
        } catch (Exception e) {
            log.writeLog("Error get File List");
            throw new ServerException("Error get File List");
        }

    }

    // удалить файл из бд
    @Transactional
    public void deleteFile(String filename, User user) {
        try {
            cloudRepositoryFiles.deleteByUserAndFileName(user, filename);
        } catch (InputDataException e) {
            log.writeLog("Error delete File");
            throw e;
        } catch (Exception e) {
            log.writeLog("Error delete File");
            throw new ServerException("Error delete File");
        }
    }

// загрузить файл из бд

    public byte[] downloadFile(String filename, User user) {
        //var user;
        try {
            var file = getFileByFilename(filename, user);
            return file.getFile();
        } catch (InputDataException e) {
            log.writeLog(STR."Error download File: \{filename}");
            throw e;
        } catch (Exception e) {
            log.writeLog(STR."Error download File: \{filename}");
            throw new ServerException("Error download File");
        }
    }

    // переименовать имя файла
    @Transactional
    public void editFileName(String filename, String newFilename, User user) {
        try {
            var file = getFileByFilename(filename, user);
            file.setFileName(newFilename);
            cloudRepositoryFiles.save(file);
        } catch (InputDataException e) {
            log.writeLog(STR."Error edit File: \{filename}");
            throw e;
        } catch (Exception e) {
            log.writeLog(STR."Error edit File: \{filename}");
            throw new ServerException("Error edit File Name");
        }

    }


    // получение файла
    private File getFileByFilename(String filename, User user) {

        var file = cloudRepositoryFiles.findByUserAndFileName(user, filename);
        if (file.isPresent()) {
            return file.get();
        } else {
            log.writeLog(STR."Error get file: \{filename}");
            throw new InputDataException(STR."Error get file: \{filename}");
        }
    }
}
