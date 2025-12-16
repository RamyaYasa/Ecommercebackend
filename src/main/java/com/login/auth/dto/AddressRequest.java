package com.login.auth.dto;

import lombok.Data;

@Data
public class AddressRequest {

    private String title;
    private String house;
    private String street;

    private String landmark; // optional
    private String city;
    private String state;
    private String pincode;

    private String name;   // optional
    private String phone;  // optional
}
