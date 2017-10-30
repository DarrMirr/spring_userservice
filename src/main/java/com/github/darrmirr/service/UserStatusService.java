package com.github.darrmirr.service;

import com.github.darrmirr.model.UserStatus;

import java.util.Optional;

/*
 * @author Darr Mirr
 */

public interface UserStatusService {

    UserStatus changeStatus(Long id, UserStatus newUserStatus);

    Optional<UserStatus> findOne(Long id);

    UserStatus save(UserStatus userStatus);
}
