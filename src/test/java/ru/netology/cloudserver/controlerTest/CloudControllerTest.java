package ru.netology.cloudserver.controlerTest;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.netology.cloud_server.controller.CloudController;
import ru.netology.cloud_server.dto.EditFileRequest;
import ru.netology.cloud_server.dto.ListResponse;
import ru.netology.cloud_server.entity.User;
import ru.netology.cloud_server.service.CloudServiceFile;
import ru.netology.cloud_server.utils.UsernamePasswordAuthentication;
import ru.netology.cloudserver.CloudServerApplicationTests;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class CloudControllerTest extends CloudServerApplicationTests {


    @MockBean
    CloudServiceFile cloudServiceFile = Mockito.mock(CloudServiceFile.class);
    @MockBean
    CloudController cloudController = Mockito.mock(CloudController.class);


    private void setAuth() {
        Authentication authentication = new UsernamePasswordAuthentication("USERNAME", null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void getAllFiles() {


        setAuth();

        User user = new User();

        List<ListResponse> list = List.of(ListResponse.builder()
                .filename("Test")
                .size(20L)
                .build());

        when(cloudController.getAllFiles(any())).thenReturn(list);
        assertNotNull(cloudServiceFile.getFileList(1, user));
    }


    @Test
    public void downloadFile() {
        setAuth();
        User user = new User();
        byte[] bytes = "nameFile".getBytes();
        when(cloudServiceFile.downloadFile(any(), any())).thenReturn(bytes);

        assertNotNull(cloudServiceFile.downloadFile("nameFile", user));
    }

    @Test
    public void deleteFile() {
        setAuth();
        Mockito.doNothing().when(cloudServiceFile).deleteFile(any(), any());
    }

    @Test
    public void editFile() {
        setAuth();
        User user = new User();
        cloudServiceFile.editFileName("fileName", "newFileName", user);

        assertNotNull(cloudServiceFile);
    }

}





