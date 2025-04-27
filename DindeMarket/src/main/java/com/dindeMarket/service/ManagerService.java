package com.dindeMarket.service;

import com.dindeMarket.api.payload.ManagerRequest;
import com.dindeMarket.api.payload.ManagerResponse;
import com.dindeMarket.api.payload.RegionResponse;
import com.dindeMarket.db.entity.RegionEntity;
import com.dindeMarket.db.entity.UserEntity;
import com.dindeMarket.db.repository.RegionRepository;
import com.dindeMarket.db.repository.RoleRepository;
import com.dindeMarket.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class ManagerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ManagerResponse createManager(ManagerRequest managerRequest) {
        RegionEntity region = regionRepository.findById(managerRequest.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));

        UserEntity manager = new UserEntity();
        manager.setPhoneNumber(managerRequest.getPhoneNumber());
        manager.setUsername(managerRequest.getUsername());
        manager.setPassword(passwordEncoder.encode(managerRequest.getPassword()));
        manager.setFirstName(managerRequest.getFirstName());
        manager.setLastName(managerRequest.getLastName());
        manager.setRegion(region);
        manager.setRoles(Set.of(roleRepository.findByName("ROLE_MANAGER")
                .orElseThrow(() -> new RuntimeException("Role not found"))));

        userRepository.save(manager);

        return mapToManagerResponse(manager);
    }

    public ManagerResponse updateManager(Long id, ManagerRequest managerRequest) {
        UserEntity manager = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        RegionEntity region = regionRepository.findById(managerRequest.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));

        manager.setUsername(managerRequest.getUsername());
        manager.setPassword(passwordEncoder.encode(managerRequest.getPassword()));
        manager.setFirstName(managerRequest.getFirstName());
        manager.setLastName(managerRequest.getLastName());
        manager.setPhoneNumber(managerRequest.getPhoneNumber());
        manager.setRegion(region);

        userRepository.save(manager);

        return mapToManagerResponse(manager);
    }

    public Optional<ManagerResponse> findById(Long id) {
        return userRepository.findById(id).map(this::mapToManagerResponse);
    }

    public Optional<ManagerResponse> findByUsername(String username) {
        return userRepository.findByUsername(username).map(this::mapToManagerResponse);
    }

    public List<ManagerResponse> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_MANAGER")))
                .map(this::mapToManagerResponse)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private ManagerResponse mapToManagerResponse(UserEntity user) {
        RegionResponse regionResponse = new RegionResponse();
        regionResponse.setId(user.getRegion().getId());
        regionResponse.setName(user.getRegion().getName());

        ManagerResponse managerResponse = new ManagerResponse();
        managerResponse.setId(user.getId());
        managerResponse.setPhoneNumber(user.getPhoneNumber());
        managerResponse.setUsername(user.getUsername());
        managerResponse.setFirstName(user.getFirstName());
        managerResponse.setLastName(user.getLastName());
        managerResponse.setRegion(regionResponse);

        return managerResponse;
    }

    @Transactional
    public void deleteManagersByIds(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }
}
