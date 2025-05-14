package com.dindeMarket.api.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientRequest {
    private String firstName ="имя";
    private String lastName = "фамилия";
    private String phoneNumber = null;
    private String address = null;
    private String username = null;
    private String password = "0000";
    private Long regionId;

}
