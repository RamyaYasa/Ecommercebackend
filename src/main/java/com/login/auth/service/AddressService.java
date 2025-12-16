package com.login.auth.service;

import com.login.auth.dto.AddressRequest;
import com.login.auth.dto.AddressResponse;
import com.login.auth.entity.Address;
import com.login.auth.entity.User;
import com.login.auth.repository.AddressRepository;
import com.login.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    private User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // -----------------------------
    // ADD ADDRESS
    // -----------------------------
    public AddressResponse addAddress(String userId, AddressRequest req) {

        User user = getUser(userId);

        Address address = Address.builder()
                .title(req.getTitle())
                .house(req.getHouse())
                .street(req.getStreet())
                .landmark(req.getLandmark())
                .city(req.getCity())
                .state(req.getState())
                .pincode(req.getPincode())
                .name(req.getName())
                .phone(req.getPhone())
                .user(user)
                .build();

        Address saved = addressRepository.save(address);

        return AddressResponse.builder()
                .id(saved.getId())
                .build();
    }

    // -----------------------------
    // GET ALL ADDRESSES
    // -----------------------------
    public List<Address> getAddresses(String userId) {
        return addressRepository.findByUser(getUser(userId));
    }

    // -----------------------------
    // DELETE ADDRESS
    // -----------------------------
    public String deleteAddress(String addressId, String userId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized deletion");
        }

        addressRepository.delete(address);
        return "Address deleted successfully";
    }

    // -----------------------------
    // UPDATE ADDRESS
    // -----------------------------
    public String updateAddress(String addressId, String userId, AddressRequest req) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized update");
        }

        address.setTitle(req.getTitle());
        address.setHouse(req.getHouse());
        address.setStreet(req.getStreet());
        address.setLandmark(req.getLandmark());
        address.setCity(req.getCity());
        address.setState(req.getState());
        address.setPincode(req.getPincode());
        address.setName(req.getName());
        address.setPhone(req.getPhone());

        addressRepository.save(address);
        return "Address updated successfully";
    }
}
