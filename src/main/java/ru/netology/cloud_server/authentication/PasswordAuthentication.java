package ru.netology.cloud_server.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.netology.cloud_server.service.DetailsServiceImpl;
import ru.netology.cloud_server.utils.Logger;
import ru.netology.cloud_server.utils.UserDetailsImpl;
import ru.netology.cloud_server.utils.UsernamePasswordAuthentication;

@RequiredArgsConstructor
@Component
public class PasswordAuthentication {
    private final DetailsServiceImpl detailsService;
    private final PasswordEncoder passwordEncoder;
    private final Logger log = Logger.getInstance();

    public Authentication auth(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        UserDetailsImpl userDetails = (UserDetailsImpl) detailsService.loadUserByUsername(username);
        System.out.println(userDetails.getPassword());
        System.out.println(password);
        if (passwordEncoder.matches(password, passwordEncoder.encode(userDetails.getPassword()))) {
            return new UsernamePasswordAuthentication(username, password, null);
        } else {
            log.writeLog(STR."Bad credentials username: \{username}");
            throw new BadCredentialsException("Bad credentials");

        }

    }
}
