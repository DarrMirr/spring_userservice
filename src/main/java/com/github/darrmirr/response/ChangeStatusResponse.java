package com.github.darrmirr.response;

import com.github.darrmirr.model.UserStatus;
import com.github.darrmirr.model.UserStatusEnum;

/*
 * @author Darr Mirr
 */

public class ChangeStatusResponse {

    private Long id;
    private UserStatusEnum newStatus;
    private UserStatusEnum oldStatus;

    protected ChangeStatusResponse() {
    }

    public ChangeStatusResponse(Long id, UserStatus newStatus, UserStatus oldStatus) {
        this.id = id;
        this.newStatus = newStatus.getUserStatus();
        this.oldStatus = oldStatus.getUserStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserStatusEnum getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(UserStatusEnum newStatus) {
        this.newStatus = newStatus;
    }

    public UserStatusEnum getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(UserStatusEnum oldStatus) {
        this.oldStatus = oldStatus;
    }
}
