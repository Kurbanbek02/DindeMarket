package com.dindeMarket.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "roles")
public class RoleEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_gen")
    @SequenceGenerator(name = "roles_gen", sequenceName = "roles_seq", allocationSize = 1)
    private Long role_id;
    private String name;
    @JsonIgnore
    @ManyToMany(targetEntity = UserEntity.class, mappedBy = "roles", cascade = CascadeType.ALL)
    private List<UserEntity> users;

    public static final String ADMIN = "ADMIN";
    public static final String MANAGER = "MANAGER";

    @Override
    public String getAuthority() {
        return name;
    }
}

