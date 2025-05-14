package com.dindeMarket.api.controller;

import com.dindeMarket.api.payload.ClientRequest;
import com.dindeMarket.api.payload.ClientResponse;
import com.dindeMarket.config.jwt.JwtTokenUtil;
import com.dindeMarket.db.entity.UserEntity;
import com.dindeMarket.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin
public class ClientController {

    private final ClientService clientService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public ResponseEntity<?> registerClient(@RequestBody ClientRequest clientRequest) {
//        if (clientService.findByUsername(clientRequest.getUsername()).isPresent()) {
//            return ResponseEntity.badRequest().body("Username already exists");
//        }


        return ResponseEntity.ok(clientService.createClient(clientRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        return clientService.findById(id)
                .map(client -> ResponseEntity.ok(clientService.convertToResponse(client,null)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @RequestBody ClientRequest clientRequest) {


        return ResponseEntity.ok(clientService.updateClient(
                id,
                clientRequest
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        clientService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
