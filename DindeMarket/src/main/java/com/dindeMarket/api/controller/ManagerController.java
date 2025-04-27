package com.dindeMarket.api.controller;

import com.dindeMarket.api.payload.ManagerRequest;
import com.dindeMarket.api.payload.ManagerResponse;
import com.dindeMarket.db.entity.UserEntity;
import com.dindeMarket.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
@RequestMapping("/api/managers")
@CrossOrigin
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping
    public ResponseEntity<?> registerManager(@RequestBody ManagerRequest managerRequest) {
        if (managerService.findByUsername(managerRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        ManagerResponse manager = managerService.createManager(managerRequest);

        return ResponseEntity.ok(manager);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponse> getManagerById(@PathVariable Long id) {
        return managerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<ManagerResponse>> getAllManagers() {
        return ResponseEntity.ok(managerService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponse> updateManager(
            @PathVariable Long id,
            @RequestBody ManagerRequest managerRequest) {

        ManagerResponse updatedManager = managerService.updateManager(
                id,
                managerRequest
        );
        return ResponseEntity.ok(updatedManager);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable Long id) {
        managerService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteManagers(@RequestBody List<Long> ids) {
        managerService.deleteManagersByIds(ids);
        return ResponseEntity.noContent().build();
    }
}

