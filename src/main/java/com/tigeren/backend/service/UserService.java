package com.tigeren.backend.service;

import com.tigeren.backend.dto.CreateUserDTO;
import com.tigeren.backend.dto.DeleteRecordDTO;
import com.tigeren.backend.dto.UpdateUserDTO;
import com.tigeren.backend.dto.UserDTO;
import com.tigeren.backend.entity.Role;
import com.tigeren.backend.entity.User;
import com.tigeren.backend.helper.ObjectMapperHelper;
import com.tigeren.backend.repository.RoleRepository;
import com.tigeren.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<UserDTO> getUserById(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            log.error("User not found with id {}", id);
        }
        return Optional.ofNullable(ObjectMapperHelper.map(user, UserDTO.class));
    }

    public List<UserDTO> getAllUsers(Integer pageSize, Integer pageNumber, String keyword, String sortField, String sortOrder) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));

        List<User> users;

        if (keyword.isEmpty()) {
            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.findByKeyword(keyword, pageable);
        }

        return ObjectMapperHelper.mapAll(users, UserDTO.class);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setEmail(createUserDTO.getEmail());
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setActive(createUserDTO.getActive());

        Role role = roleRepository.findById(createUserDTO.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role ID"));

        user.setRole(role);
        User createdUser = userRepository.save(user);
        return ObjectMapperHelper.map(createdUser, UserDTO.class);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public UserDTO updateUser(UpdateUserDTO updateUserDTO) {
        String userId = updateUserDTO.getId();

        return userRepository.findById(userId).map(user -> {
            user.setEmail(updateUserDTO.getEmail());
            user.setFirstName(updateUserDTO.getFirstName());
            user.setLastName(updateUserDTO.getLastName());
            user.setActive(updateUserDTO.getActive());

            Role role = roleRepository.findById(updateUserDTO.getRoleId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role ID"));

            user.setRole(role);

            User updatedUser = userRepository.save(user);
            return ObjectMapperHelper.map(updatedUser, UserDTO.class);

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + userId));
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteUsers(DeleteRecordDTO deleteRecordDTO) {
        List<String> ids = deleteRecordDTO.getIds();
        userRepository.deleteAllById(ids);
    }
}