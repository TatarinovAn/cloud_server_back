package ru.netology.cloud_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//Data transfer object login Request
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
    private String login;
    private String password;
}
