package com.github.darrmirr.repository;

import com.github.darrmirr.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/*
 * @author Darr Mirr
 */

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(Long id);
}
