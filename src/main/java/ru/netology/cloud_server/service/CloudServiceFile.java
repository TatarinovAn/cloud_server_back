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

    public void uploadFile(String filename, MultipartFile file) {
        try {
            var user = getUserFromSecurityContext();
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
    public List<ListResponse> getFileList(Integer limit) {
        try {
            var user = getUserFromSecurityContext();
            System.out.println(user.getUsername());
            var file = cloudRepositoryFiles.findAllByUser(user, Limit.of(limit));
//            for (File s : file) {
//                System.out.println(s.getFileName());
//                System.out.println(s.getFileSize());
//            }

            List<ListResponse> result = file.stream()
                    .map(o -> new ListResponse(o.getFileName(), o.getFileSize()))
                    .collect(Collectors.toList());
//            for (ListResponse s : result) {
//                System.out.println(s);
//            }
            return result;

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
    public void deleteFile(String filename) {
        try {
            var user = getUserFromSecurityContext();
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

    public byte[] downloadFile(String filename) {
        //var user;
        try {
            var file = getFileByFilename(filename);
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
    public void editFileName(String filename, EditFileRequest editFileRequest) {
        try {
            var file = getFileByFilename(filename);
            file.setFileName(editFileRequest.getFilename());
            cloudRepositoryFiles.save(file);
        } catch (InputDataException e) {
            log.writeLog(STR."Error edit File: \{filename}");
            throw e;
        } catch (Exception e) {
            log.writeLog(STR."Error edit File: \{filename}");
            throw new ServerException("Error edit File Name");
        }

    }

    //аунтификация пользователя
    private User getUserFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        var user = cloudRepositoryUsers.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            log.writeLog(STR."Error authentication user: \{username}");
            throw new InputDataException(STR."Error authentication user: \{username}");
        }
    }

    // получение файла
    private File getFileByFilename(String filename) {
        var user = getUserFromSecurityContext();
        var file = cloudRepositoryFiles.findByUserAndFileName(user, filename);
        if (file.isPresent()) {
            return file.get();
        } else {
            log.writeLog(STR."Error get file: \{filename}");
            throw new InputDataException(STR."Error get file: \{filename}");
        }
    }
}
