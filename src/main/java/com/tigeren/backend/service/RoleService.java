package com.tigeren.backend.service;

import com.tigeren.backend.dto.CreateRoleDTO;
import com.tigeren.backend.dto.DeleteRecordDTO;
import com.tigeren.backend.dto.RoleDTO;
import com.tigeren.backend.dto.UpdateRoleDTO;
import com.tigeren.backend.entity.Role;
import com.tigeren.backend.helper.ObjectMapperHelper;
import com.tigeren.backend.repository.RoleRepository;
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
public class RoleService {
    public final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDTO> getAllRoles(Integer pageSize, Integer pageNumber, String keyword, String sortField, String sortOrder) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));

        List<Role> roles;

        if (keyword.isEmpty()) {
            roles = roleRepository.findAll(pageable).getContent();

        } else {
            roles = roleRepository.findByKeyword(keyword, pageable);
        }

        return ObjectMapperHelper.mapAll(roles, RoleDTO.class);
    }

    public Optional<RoleDTO> getRoleById(String id) {
        Optional<Role> role = roleRepository.findById(id);
        Optional<RoleDTO> roleDTO = Optional.ofNullable(ObjectMapperHelper.map(role, RoleDTO.class));

        if (role.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id);
            log.error("Role not found with id {}", id);
        }
        return roleDTO;
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Role createRole(CreateRoleDTO createRoleDTO) {
        Role role = new Role();

        role.setId(UUID.randomUUID().toString());
        role.setName(createRoleDTO.getName());


        return roleRepository.save(role);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Role updateRole(UpdateRoleDTO updateRoleDTO) {
        String roleId = updateRoleDTO.getId();

        return roleRepository.findById(roleId).map(role -> {
            role.setName(updateRoleDTO.getName());
            return roleRepository.save(role);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id " + roleId));
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteRoles(DeleteRecordDTO deleteRecordDTO) {
        List<String> ids = deleteRecordDTO.getIds();
        roleRepository.deleteAllById(ids);
    }
}
