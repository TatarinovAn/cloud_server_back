package ru.netology.cloud_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Data transfer object login Response
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
    @JsonProperty("auth-token")
    private String authToken;
}
