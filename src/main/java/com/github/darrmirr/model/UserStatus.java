package com.github.darrmirr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/*
 * @author Darr Mirr
 */

@RedisHash("userStatus")
public class UserStatus{

    @Id
    private Long id;

    @NotNull
    private UserStatusEnum userStatus;

    @JsonIgnore
    private LocalDateTime changeTime;

    protected UserStatus() {
    }

    public UserStatus(Long id, UserStatusEnum userStatus) {
        this.id = id;
        this.userStatus = userStatus;
        this.changeTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserStatusEnum getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatusEnum userStatus) {
        this.userStatus = userStatus;
        this.changeTime = LocalDateTime.now();
    }

    public LocalDateTime getChangeTime() {
        return changeTime;
    }

    public void setChangeTime() {
        this.changeTime = LocalDateTime.now();
    }
}
