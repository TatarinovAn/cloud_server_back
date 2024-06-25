package ru.netology.cloud_server.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.cloud_server.repository.CloudRepositoryUsers;
import ru.netology.cloud_server.utils.Logger;
import ru.netology.cloud_server.utils.UserDetailsImpl;

@Service
public class DetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CloudRepositoryUsers cloudRepositoryUsers;
    private final Logger log = Logger.getInstance();

    //получить пользователя по имени
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = cloudRepositoryUsers.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(STR."Username don't found: \{username}"));

        return new UserDetailsImpl(user);
    }
}
