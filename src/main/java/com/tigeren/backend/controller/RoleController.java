package com.tigeren.backend.controller;

import com.tigeren.backend.dto.CreateRoleDTO;
import com.tigeren.backend.dto.DeleteRecordDTO;
import com.tigeren.backend.dto.RoleDTO;
import com.tigeren.backend.dto.UpdateRoleDTO;
import com.tigeren.backend.entity.Role;
import com.tigeren.backend.service.RoleService;
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
@RequestMapping("/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) {
        Role createdRole = roleService.createRole(createRoleDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<RoleDTO> getRoleById(@RequestParam("id") String roleId) {
        Optional<RoleDTO> roleDTO = roleService.getRoleById(roleId);
        return roleDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleDTO>> getAllRoles(
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "insertedAt") String sortField,
            @RequestParam(defaultValue = "ASC") String sortOrder) {

        List<RoleDTO> rolesDTO = roleService.getAllRoles(pageSize, pageNumber, keyword, sortField, sortOrder);
        return new ResponseEntity<>(rolesDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Role> updateRole(@Valid @RequestBody UpdateRoleDTO updateRoleDTO) {
        try {
            Role updatedRole = roleService.updateRole(updateRoleDTO);
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteRoles(@Valid @RequestBody DeleteRecordDTO deleteRecordDTO) {
        roleService.deleteRoles(deleteRecordDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
