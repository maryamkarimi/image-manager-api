package com.maryam.image.manager.api.repository;

import com.maryam.image.manager.api.domain.authentication.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UserRepository extends CassandraRepository<User, String> {

    User findByUsername(@NotNull final String username);

}
