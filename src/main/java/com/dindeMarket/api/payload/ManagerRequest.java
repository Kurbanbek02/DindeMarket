package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerRequest {
    private String phoneNumber;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Long regionId;
}
