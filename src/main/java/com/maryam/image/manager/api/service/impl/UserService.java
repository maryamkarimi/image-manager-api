package com.maryam.image.manager.api.service.impl;

import com.maryam.image.manager.api.domain.authentication.User;
import com.maryam.image.manager.api.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class UserService implements UserDetailsService {

    @NotNull final UserRepository userRepository;

    public UserService(@NotNull final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("User with username {0} cannot be found.", username));
        }

        return user;
    }

}
