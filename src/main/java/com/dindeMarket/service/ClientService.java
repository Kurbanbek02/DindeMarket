package com.dindeMarket.service;

import com.dindeMarket.api.payload.ClientRequest;
import com.dindeMarket.api.payload.ClientResponse;
import com.dindeMarket.config.jwt.JwtTokenUtil;
import com.dindeMarket.db.entity.RegionEntity;
import com.dindeMarket.db.entity.RoleEntity;
import com.dindeMarket.db.entity.UserEntity;
import com.dindeMarket.db.repository.RegionRepository;
import com.dindeMarket.db.repository.RoleRepository;
import com.dindeMarket.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {


    private  final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final RoleRepository roleRepository;

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public ClientResponse createClient(ClientRequest clientRequest) {
        UserEntity client = new UserEntity();
        RegionEntity region = regionRepository.findById(clientRequest.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));
        client.setRegion(region);

        // Если username равен null, генерируем случайный UUID
        String username = clientRequest.getUsername() != null ? clientRequest.getUsername() : UUID.randomUUID().toString();
        client.setUsername(username);
        client.setPassword(passwordEncoder.encode(clientRequest.getPassword()));
        client.setFirstName(clientRequest.getFirstName());
        client.setLastName(clientRequest.getLastName());
        client.setPhoneNumber(clientRequest.getPhoneNumber());
        client.setAddress(clientRequest.getAddress());

        // Создаем роль клиента и назначаем ее пользователю
        client.setRoles(Set.of(roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("Role not found"))));

        // Сохраняем клиента в базе данных
        UserEntity savedClient = userRepository.save(client);

        // Генерируем JWT токен для нового клиента
        String token = jwtTokenUtil.generateToken(savedClient);

        // Создаем и возвращаем объект ClientResponse с токеном
        return convertToResponse(savedClient, token);
    }


    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<ClientResponse> findAll() {
        return userRepository.findAllByRoles_Name("ROLE_CLIENT")
                .stream()
                .map(client -> {
                    return convertToResponse(client, null);
                })
                .collect(Collectors.toList());
    }


    public ClientResponse updateClient(Long id, ClientRequest clientRequest) {
        UserEntity client = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        RegionEntity region = regionRepository.findById(clientRequest.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));
        client.setRegion(region);

        // Если username равен null, не изменяем его
        String username = clientRequest.getUsername() != null ? clientRequest.getUsername() : client.getUsername();
        client.setUsername(username);

        // Обновляем пароль только если он передан
        if (clientRequest.getPassword() != null) {
            client.setPassword(passwordEncoder.encode(clientRequest.getPassword()));
        }

        client.setFirstName(clientRequest.getFirstName());
        client.setLastName(clientRequest.getLastName());
        client.setPhoneNumber(clientRequest.getPhoneNumber());
        client.setAddress(clientRequest.getAddress());

        // Сохраняем обновленного клиента в базе данных
        UserEntity updatedClient = userRepository.save(client);

        // Генерируем новый JWT токен для обновленного клиента
        String token = jwtTokenUtil.generateToken(updatedClient);

        // Создаем и возвращаем объект ClientResponse с токеном
        return convertToResponse(updatedClient, token);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public ClientResponse convertToResponse(UserEntity client,String token) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setUsername(client.getUsername());
        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setPhoneNumber(client.getPhoneNumber());
        response.setAddress(client.getAddress());
        response.setToken(token);
        return response;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
