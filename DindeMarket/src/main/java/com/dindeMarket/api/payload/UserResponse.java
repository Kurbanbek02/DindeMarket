package com.dindeMarket.api.payload;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private boolean enabled;
    private boolean deleted;
    private LocalDateTime created;
    private boolean isActive;
    private String phoneNumber;

}