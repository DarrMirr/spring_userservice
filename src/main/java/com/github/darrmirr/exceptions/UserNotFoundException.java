package com.github.darrmirr.exceptions;

/*
 * @author Darr Mirr
 */

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super("не удалось найти пользователя по id = " + userId);
    }
}
