package ru.netology.cloud_server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_server.dto.EditFileRequest;
import ru.netology.cloud_server.dto.ListResponse;
import ru.netology.cloud_server.entity.User;
import ru.netology.cloud_server.exception.InputDataException;
import ru.netology.cloud_server.exception.ServerException;
import ru.netology.cloud_server.repository.CloudRepositoryUsers;
import ru.netology.cloud_server.service.CloudServiceFile;
import ru.netology.cloud_server.utils.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static ru.netology.cloud_server.utils.Const.*;


@RequiredArgsConstructor
@RestController
public class CloudController {

    private final CloudServiceFile cloudServiceFile;
    private final CloudRepositoryUsers cloudRepositoryUsers;
    private final Logger log = Logger.getInstance();

    //загрузка файла на облако
    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam(FILENAME_PARAM) String fileName, MultipartFile file) {
        var user = getUserFromSecurityContext();
        cloudServiceFile.uploadFile(fileName, file, user);
        // System.out.println(file.getName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //удаление файла
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam(FILENAME_PARAM) String fileName) {
        var user = getUserFromSecurityContext();
        cloudServiceFile.deleteFile(fileName, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //скачивание файла
    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(@RequestParam(FILENAME_PARAM) String fileName) {
        var user = getUserFromSecurityContext();
        byte[] file = cloudServiceFile.downloadFile(fileName, user);
        try {
            return ResponseEntity.ok().body(new ByteArrayResource(file));
        } catch (InputDataException e) {
            log.writeLog(STR."Error download File: \{fileName}");
            throw e;
        } catch (Exception e) {
            log.writeLog(STR."Error download File: \{fileName}");
            throw new ServerException("Error download File");
        }
    }

    //переменование файла
    @PutMapping("/file")
    public ResponseEntity<?> editFile(@RequestParam(FILENAME_PARAM) String fileName,
                                      @RequestBody EditFileRequest editFileRequest) {
        String newFilename = editFileRequest.getFilename();
        var user = getUserFromSecurityContext();
        cloudServiceFile.editFileName(fileName, newFilename, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //получения списка файлов
    @GetMapping("/list")
    public List<ListResponse> getAllFiles(@RequestParam(LIMIT_PARAM) Integer limit) {
        var user = getUserFromSecurityContext();
        try {
            var file = cloudServiceFile.getFileList(limit, user);

            List<ListResponse> result = file.stream()
                    .map(o -> new ListResponse(o.getFileName(), o.getFileSize()))
                    .collect(Collectors.toList());
            return result;
        } catch (InputDataException e) {
            log.writeLog("Error get File List");
            throw e;
        } catch (Exception e) {
            log.writeLog("Error get File List");
            throw new ServerException("Error get File List");
        }

    }

    public User getUserFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        System.out.println(username + " Name");
        var user = cloudRepositoryUsers.findByUsername(username);
        System.out.println(user.isPresent());
        if (user.isPresent()) {
            return user.get();
        } else {
            log.writeLog(STR."Error authentication user: \{username}");
            throw new InputDataException(STR."Error authentication user: \{username}");
        }
    }
}
