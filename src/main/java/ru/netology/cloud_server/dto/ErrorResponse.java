package ru.netology.cloud_server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Data transfer object Error obj
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private Integer id;
    private String massage;

}