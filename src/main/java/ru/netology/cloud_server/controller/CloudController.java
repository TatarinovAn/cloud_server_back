package ru.netology.cloud_server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_server.dto.EditFileRequest;
import ru.netology.cloud_server.dto.ListResponse;
import ru.netology.cloud_server.service.CloudServiceFile;

import java.util.List;

import static ru.netology.cloud_server.utils.Const.*;


@RequiredArgsConstructor
@RestController
public class CloudController {

    private final CloudServiceFile cloudServiceFile;

    //загрузка файла на облако
    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam(FILENAME_PARAM) String fileName, MultipartFile file) {
        cloudServiceFile.uploadFile(fileName, file);
        // System.out.println(file.getName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //удаление файла
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam(FILENAME_PARAM) String fileName) {
        cloudServiceFile.deleteFile(fileName);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //скачивание файла
    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(@RequestParam(FILENAME_PARAM) String fileName) {
        byte[] file = cloudServiceFile.downloadFile(fileName);
        return ResponseEntity.ok().body(new ByteArrayResource(file));
    }

    //переменование файла
    @PutMapping("/file")
    public ResponseEntity<?> editFile(@RequestParam(FILENAME_PARAM) String fileName,
                                      @RequestBody EditFileRequest editFileRequest) {
        cloudServiceFile.editFileName(fileName, editFileRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //получения списка файлов
    @GetMapping("/list")
    public List<ListResponse> getAllFiles(@RequestParam(LIMIT_PARAM) Integer limit) {
        return cloudServiceFile.getFileList(limit);
    }
}
