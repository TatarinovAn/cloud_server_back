package ru.netology.cloudserver.serviceTest;


import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import ru.netology.cloud_server.entity.User;
import ru.netology.cloud_server.repository.CloudRepositoryFiles;
import ru.netology.cloud_server.repository.CloudRepositoryUsers;
import ru.netology.cloud_server.service.CloudServiceFile;
import ru.netology.cloudserver.CloudServerApplicationTests;


import java.util.LinkedList;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;


@AutoConfigureMockMvc
@SpringBootTest
public class FileServiceTest extends CloudServerApplicationTests {


    @MockBean
    CloudServiceFile cloudServiceFile = Mockito.mock(CloudServiceFile.class);

    @MockBean
    CloudRepositoryFiles cloudRepositoryFiles = Mockito.mock(CloudRepositoryFiles.class);

    @MockBean
    CloudRepositoryUsers cloudRepositoryUsers = Mockito.mock(CloudRepositoryUsers.class);

    MockMultipartFile mockMultipartFile = new MockMultipartFile("FILENAME", new byte[32]);


    @Test
    public void uploadFile() {

        cloudServiceFile.uploadFile("FILENAME", mockMultipartFile, User.builder().build());

        var storage = cloudRepositoryFiles.findByUserAndFileName(new User(), "Username");
        assertNotNull(storage);
    }

}
