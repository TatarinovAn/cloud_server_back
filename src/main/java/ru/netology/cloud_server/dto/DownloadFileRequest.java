package ru.netology.cloud_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
// Data transfer object для скачивания файла
public class DownloadFileRequest {
    private String filename;
}
