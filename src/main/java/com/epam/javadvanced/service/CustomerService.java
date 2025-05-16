package com.epam.javadvanced.service;

import com.epam.javadvanced.domain.Customer;
import com.epam.javadvanced.dto.CustomerDto;
import com.epam.javadvanced.exceptions.ResourceNotFoundException;
import com.epam.javadvanced.mapper.CustomerMapper;
import com.epam.javadvanced.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        log.info("Retrieving all customers");
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        log.info("Retrieving customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return customerMapper.toDto(customer);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        log.info("Creating new customer");
        Customer customer = customerMapper.toEntity(customerDto);
        customer.setId(null);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Created customer with id: {}", savedCustomer.getId());
        return customerMapper.toDto(savedCustomer);
    }

    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        log.info("Updating customer with id: {}", id);

        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }

        Customer customer = customerMapper.toEntity(customerDto);
        customer.setId(id);
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Updated customer with id: {}", id);
        return customerMapper.toDto(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);

        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
        log.info("Deleted customer with id: {}", id);
    }
}