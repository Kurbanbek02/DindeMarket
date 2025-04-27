package com.dindeMarket;

import com.dindeMarket.db.entity.RoleEntity;
import com.dindeMarket.db.entity.UserEntity;
import com.dindeMarket.db.repository.RoleRepository;
import com.dindeMarket.db.repository.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
@OpenAPIDefinition
@EnableScheduling
@RequiredArgsConstructor
public class DindeMarketApplication {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(DindeMarketApplication.class, args);
    }


    @PostConstruct
    private void saveRole() {
        RoleEntity role = new RoleEntity();
        role.setRole_id(1L);
        role.setName("ROLE_ADMIN");
        roleRepository.save(role);

        RoleEntity role2 = new RoleEntity();
        role2.setRole_id(2L);
        role2.setName("ROLE_MANAGER");
        roleRepository.save(role2);

        RoleEntity role3 =new RoleEntity();
        role3.setRole_id(3L);
        role3.setName("ROLE_CLIENT");
        roleRepository.save(role3);

        UserEntity user = new UserEntity();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setEnabled(true);
        user.setRegion(null);
//        user.setRoles(Set.of(roleRepository.findByName("ROLE_MANAGER")
//                .orElseThrow(() -> new RuntimeException("Role not found"))));
        userRepository.save(user);
    }
}
