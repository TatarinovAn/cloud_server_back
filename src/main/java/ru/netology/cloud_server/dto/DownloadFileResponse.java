package ru.netology.cloud_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
// Data transfer object
public class DownloadFileResponse {
    private String hash;
    private String file;
}
