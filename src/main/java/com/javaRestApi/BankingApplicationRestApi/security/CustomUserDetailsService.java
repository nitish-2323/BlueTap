
package com.javaRestApi.BankingApplicationRestApi.security;

import com.javaRestApi.BankingApplicationRestApi.Model.CustomerDTO.CustomerDto;
import com.javaRestApi.BankingApplicationRestApi.Repository.CustomerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        CustomerDto customer = customerRepository.findByFullName(username);

        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found: " + username);
        }

        return User.builder()
                .username(customer.getFullName())
                .password(customer.getPassword())
                .roles("USER")
                .build();
    }
}
