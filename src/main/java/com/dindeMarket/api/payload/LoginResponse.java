package com.dindeMarket.api.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LoginResponse {
  private String jwtToken;
  //private String message;
  private Collection<? extends GrantedAuthority> authorities;

  public LoginResponse(String jwtToken, Collection<? extends GrantedAuthority> authorities) {
    this.jwtToken = jwtToken;
    this.authorities =authorities;
  }
}