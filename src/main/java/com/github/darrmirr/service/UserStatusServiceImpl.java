package com.github.darrmirr.service;

import com.github.darrmirr.model.UserStatus;
import com.github.darrmirr.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.github.darrmirr.model.UserStatusEnum.AWAY;
import static com.github.darrmirr.model.UserStatusEnum.ONLINE;

/*
 * @author Darr Mirr
 */

@Service
public class UserStatusServiceImpl implements UserStatusService {

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public UserStatus changeStatus(Long id, UserStatus newUserStatus) {
        newUserStatus.setId(id);
        changeDelay(newUserStatus);
        return userStatusRepository.save(newUserStatus);
    }

    private Runnable changeOnline(UserStatus userStatus) {
        return () -> {
            UserStatus currentStatus = userStatusRepository.findOne(userStatus.getId());
            if (currentStatus != null && currentStatus.getChangeTime().isEqual(userStatus.getChangeTime())) {
                userStatus.setUserStatus(AWAY);
                userStatusRepository.save(userStatus);
            }
        };
    }

    private void changeDelay(UserStatus userStatus) {
        if (userStatus.getUserStatus() == ONLINE) {
            Date initDelayDate = new Date(System.currentTimeMillis() + userStatus.getUserStatus().getDurationSeconds() * 1000);
            threadPoolTaskScheduler.schedule(changeOnline(userStatus), initDelayDate);
        }
    }

    @Override
    public Optional<UserStatus> findOne(Long id) {
        return Optional.ofNullable(userStatusRepository.findOne(id));
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        return userStatusRepository.save(userStatus);
    }
}
