package com.login.auth.repository;

import com.login.auth.entity.Address;
import com.login.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, String> {

    List<Address> findByUser(User user);
}
