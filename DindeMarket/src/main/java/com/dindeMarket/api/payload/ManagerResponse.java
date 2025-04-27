package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ManagerResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private RegionResponse region;

}

