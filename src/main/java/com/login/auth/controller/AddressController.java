package com.login.auth.controller;

import com.login.auth.dto.AddressRequest;
import com.login.auth.dto.AddressResponse;
import com.login.auth.entity.Address;
import com.login.auth.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // ------------------------------
    // ADD ADDRESS
    // ------------------------------
    @PostMapping("/add")
    public AddressResponse addAddress(
            @RequestBody AddressRequest req,
            Principal principal
    ) {
        return addressService.addAddress(principal.getName(), req);
    }

    // ------------------------------
    // GET ALL ADDRESSES
    // ------------------------------
    @GetMapping("/all")
    public List<Address> getAddresses(Principal principal) {
        return addressService.getAddresses(principal.getName());
    }

    // ------------------------------
    // DELETE ADDRESS
    // ------------------------------
    @DeleteMapping("/delete/{id}")
    public String deleteAddress(
            @PathVariable String id,
            Principal principal
    ) {
        return addressService.deleteAddress(id, principal.getName());
    }

    // ------------------------------
    // UPDATE ADDRESS
    // ------------------------------
    @PutMapping("/update/{id}")
    public String updateAddress(
            @PathVariable String id,
            @RequestBody AddressRequest req,
            Principal principal
    ) {
        return addressService.updateAddress(id, principal.getName(), req);
    }
}
