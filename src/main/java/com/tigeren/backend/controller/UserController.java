package com.tigeren.backend.controller;

import com.tigeren.backend.dto.CreateUserDTO;
import com.tigeren.backend.dto.DeleteUserDTO;
import com.tigeren.backend.dto.UpdateUserDTO;
import com.tigeren.backend.entity.User;
import com.tigeren.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        User createdUser = userService.createUser(createUserDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<User> getUserById(@RequestParam("id") String userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "insertedAt") String sortField,
            @RequestParam(defaultValue = "ASC") String sortOrder) {
        
        List<User> users = userService.getAllUsers(pageSize, pageNumber, keyword, sortField, sortOrder);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            User updatedUser = userService.updateUser(updateUserDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody DeleteUserDTO deleteUserDTO) {
        userService.deleteUser(deleteUserDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}