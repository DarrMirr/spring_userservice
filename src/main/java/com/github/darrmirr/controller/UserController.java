package com.github.darrmirr.controller;

import com.github.darrmirr.exceptions.FieldValidationException;
import com.github.darrmirr.exceptions.UserNotFoundException;
import com.github.darrmirr.model.User;
import com.github.darrmirr.model.UserStatus;
import com.github.darrmirr.repository.UserRepository;
import com.github.darrmirr.response.ChangeStatusResponse;
import com.github.darrmirr.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

import static com.github.darrmirr.model.UserStatusEnum.NONE;

/*
 * @author Darr Mirr
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody User user, BindingResult result) {
        Map<String, Object> responseMap = new HashMap<>();
        if (result.hasErrors()){
            throw new FieldValidationException(result);
        }
        if (user.getId() != null) {
            throw new IllegalArgumentException("id не используется при добавлении пользователя");
        }
        User newUser = userRepository.save(user);
        responseMap.put("id", newUser.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(location).body(responseMap);
    }

    private void validateUser(Long userId) {
        this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        validateUser(id);
        return ResponseEntity.ok(userRepository.findOne(id));
    }

    @PostMapping(value = "/{id}/status")
    public ResponseEntity<?> setUserStatus(@PathVariable("id") Long id, @Valid @RequestBody UserStatus userStatus, BindingResult result) {
        if (result.hasErrors()){
            throw new FieldValidationException(result);
        }
        validateUser(id);
        UserStatus oldUserStatus = userStatusService.findOne(id).orElse(new UserStatus(id, NONE));
        UserStatus newUserStatus = userStatusService.changeStatus(id, userStatus);
        return ResponseEntity.ok().body(new ChangeStatusResponse(id, newUserStatus, oldUserStatus));
    }

    @GetMapping(value = "/{id}/status")
    public ResponseEntity<?> getUserStatus(@PathVariable("id") Long id) {
        validateUser(id);
        return userStatusService.findOne(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
