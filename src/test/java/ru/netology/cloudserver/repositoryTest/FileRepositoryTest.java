package ru.netology.cloudserver.repositoryTest;


import org.junit.Test;
import org.mockito.Mockito;
import ru.netology.cloud_server.repository.CloudRepositoryUsers;

import static org.junit.Assert.*;


public class FileRepositoryTest {


    CloudRepositoryUsers cloudRepositoryUsers = Mockito.mock(CloudRepositoryUsers.class);


    @Test
    public void deleteByUserAndFileName() {
        assertNotNull(cloudRepositoryUsers.findByUsername("username"));
    }
}
