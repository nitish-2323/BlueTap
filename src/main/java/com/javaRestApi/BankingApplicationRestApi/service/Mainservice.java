
package com.javaRestApi.BankingApplicationRestApi.service;

import com.javaRestApi.BankingApplicationRestApi.Execption.UserExecption;
import com.javaRestApi.BankingApplicationRestApi.Execption.UserNotFound;
import com.javaRestApi.BankingApplicationRestApi.Model.CustomerDTO.CustomerDto;
import com.javaRestApi.BankingApplicationRestApi.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Mainservice {

    private final CustomerRepository mainRepositry;
    private final PasswordEncoder passwordEncoder;

    public Mainservice(CustomerRepository mainRepositry,
                       PasswordEncoder passwordEncoder) {
        this.mainRepositry = mainRepositry;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDto save(CustomerDto dto, long customerId) {

        CustomerDto existing = mainRepositry.findBycustomerId(customerId);

        if (existing != null) {
            throw new UserExecption("Id must be unique, please check Customer id");
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        return mainRepositry.save(dto);
    }

    public CustomerDto getInfo(CustomerDto dto, long customerId) {

        CustomerDto obj = mainRepositry.findBycustomerId(customerId);

        if (obj == null) {
            throw new UserNotFound("Invalid customer");
        }

        return obj;
    }

    public CustomerDto updateObj(CustomerDto dto, long customerId) {

        CustomerDto obj = mainRepositry.findBycustomerId(customerId);

        if (obj == null) {
            throw new UserNotFound("Customer not found");
        }

        obj.setFullName(dto.getFullName());
        obj.setEmail(dto.getEmail());
        obj.setPhoneNumber(dto.getPhoneNumber());
        obj.setAddress(dto.getAddress());
        obj.setAadhaarNumber(dto.getAadhaarNumber());
        obj.setPanNumber(dto.getPanNumber());

        return mainRepositry.save(obj);
    }

    @Transactional
    public CustomerDto delete(long customerId) {

        CustomerDto obj = mainRepositry.findBycustomerId(customerId);

        if (obj == null) {
            throw new UserNotFound("Customer not found for deletion");
        }

        mainRepositry.delete(obj);

        return obj;
    }
}
