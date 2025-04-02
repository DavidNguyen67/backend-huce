package com.tigeren.backend.controller;

import com.tigeren.backend.dto.CreateUserDTO;
import com.tigeren.backend.dto.DeleteRecordDTO;
import com.tigeren.backend.dto.UpdateUserDTO;
import com.tigeren.backend.dto.UserDTO;
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
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        UserDTO createdUser = userService.createUser(createUserDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserById(@RequestParam("id") String userId) {
        Optional<UserDTO> userDTO = userService.getUserById(userId);
        return userDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "insertedAt") String sortField,
            @RequestParam(defaultValue = "ASC") String sortOrder) {

        List<UserDTO> usersDTO = userService.getAllUsers(pageSize, pageNumber, keyword, sortField, sortOrder);
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(updateUserDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody DeleteRecordDTO deleteRecordDTO) {
        userService.deleteUsers(deleteRecordDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}