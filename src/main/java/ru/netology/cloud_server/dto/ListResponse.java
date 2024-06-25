package ru.netology.cloud_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Data transfer object List response
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ListResponse {
    private String filename;
    private Long size;
}
