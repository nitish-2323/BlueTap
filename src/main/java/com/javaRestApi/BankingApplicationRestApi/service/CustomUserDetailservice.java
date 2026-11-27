//
//package com.javaRestApi.BankingApplicationRestApi.service;
//
//import com.javaRestApi.BankingApplicationRestApi.Model.CustomerDTO.CustomerDto;
//import com.javaRestApi.BankingApplicationRestApi.Repository.CustomerRepository;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailservice implements UserDetailsService {
//
//    private final CustomerRepository customerRepository;
//
//    public CustomUserDetailservice(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username)
//            throws UsernameNotFoundException {
//
//        CustomerDto customer = customerRepository.findByFullName(username);
//
//        if (customer == null) {
//            throw new UsernameNotFoundException(
//                    "Customer not found: " + username);
//        }
//
//        return User.withUsername(customer.getFullName())
//                .password(customer.getPassword())
//                .authorities("ROLE_USER")
//                .build();
//    }
//}
