package ru.netology.cloudserver.repositoryTest;


import org.junit.Test;
import org.mockito.Mockito;
import ru.netology.cloud_server.repository.CloudRepositoryUsers;
import ru.netology.cloudserver.CloudServerApplicationTests;

import static org.junit.Assert.*;


public class UserRepositoryTest extends CloudServerApplicationTests {

    CloudRepositoryUsers cloudRepositoryUsers = Mockito.mock(CloudRepositoryUsers.class);


    @Test
    public void findByUsername() {
        var user = cloudRepositoryUsers.findByUsername("test");
        assertNotNull(user);

    }
}
