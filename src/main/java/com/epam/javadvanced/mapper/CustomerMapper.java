package com.epam.javadvanced.mapper;

import com.epam.javadvanced.domain.Customer;
import com.epam.javadvanced.dto.CustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dob(customer.getDob())
                .balance(customer.getBalance())
                .build();
    }

    public Customer toEntity(CustomerDto customerDto) {
        return Customer.builder()
                .id(customerDto.getId())
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .dob(customerDto.getDob())
                .balance(customerDto.getBalance())
                .build();
    }
}
