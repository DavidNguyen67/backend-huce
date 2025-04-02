package com.tigeren.backend.service;

import com.tigeren.backend.dto.CreateUserDTO;
import com.tigeren.backend.dto.DeleteUserDTO;
import com.tigeren.backend.dto.UpdateUserDTO;
import com.tigeren.backend.entity.User;
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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id);
            log.error("User not found with id {}", id);
        }
        return user;
    }

    public List<User> getAllUsers(int pageSize, int pageNumber, String keyword, String sortField, String sortOrder) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));

        if (keyword.isEmpty()) {
            return userRepository.findAll(pageable).getContent();
        } else {
            return userRepository.findByKeyword(keyword, pageable);
        }
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public User createUser(CreateUserDTO createUserDTO) {
        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setEmail(createUserDTO.getEmail());
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setActive(createUserDTO.getActive());

        return userRepository.save(user);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public User updateUser(UpdateUserDTO updateUserDTO) {
        String userId = updateUserDTO.getId();

        return userRepository.findById(userId).map(user -> {
            user.setEmail(updateUserDTO.getEmail());
            user.setFirstName(updateUserDTO.getFirstName());
            user.setLastName(updateUserDTO.getLastName());
            user.setActive(updateUserDTO.getActive());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + userId));
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteUser(DeleteUserDTO deleteUserDTO) {
        List<String> ids = deleteUserDTO.getIds();
        userRepository.deleteAllById(ids);
    }
}