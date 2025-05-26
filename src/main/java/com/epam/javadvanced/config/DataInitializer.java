package com.epam.javadvanced.config;

import com.epam.javadvanced.domain.Customer;
import com.epam.javadvanced.domain.Product;
import com.epam.javadvanced.repository.CustomerRepository;
import com.epam.javadvanced.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        initializeProducts();
        initializeCustomers();
    }

    private void initializeProducts() {
        log.info("Checking if products need initialization...");

        if (productRepository.count() > 0) {
            log.info("Database already contains {} products, skipping initialization", productRepository.count());
            return;
        }

        log.info("Initializing database with sample products");

        List<Product> products = Arrays.asList(
                Product.builder()
                        .name("Laptop")
                        .description("High-performance laptop")
                        .price(1299.99)
                        .stockQuantity(10)
                        .build(),
                Product.builder()
                        .name("Smartphone")
                        .description("Latest model smartphone")
                        .price(899.99)
                        .stockQuantity(15)
                        .build(),
                Product.builder()
                        .name("Headphones")
                        .description("Noise-cancelling headphones")
                        .price(249.99)
                        .stockQuantity(20)
                        .build()
        );

        productRepository.saveAll(products);
        log.info("Database initialized with {} products", productRepository.count());
    }

    private void initializeCustomers() {
        log.info("Checking if customers need initialization...");

        if (customerRepository.count() > 0) {
            log.info("Database already contains {} customers, skipping initialization", customerRepository.count());
            return;
        }

        log.info("Initializing database with sample customers");

        List<Customer> customers = Arrays.asList(
                Customer.builder()
                        .firstName("John")
                        .lastName("Deere")
                        .dob(1985)
                        .balance(5000.00)
                        .build(),
                Customer.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .dob(1990)
                        .balance(7500.50)
                        .build(),
                Customer.builder()
                        .firstName("Michael")
                        .lastName("Jackson")
                        .dob(1978)
                        .balance(12000.75)
                        .build(),
                Customer.builder()
                        .firstName("Emily")
                        .lastName("Abramov")
                        .dob(1995)
                        .balance(3200.25)
                        .build(),
                Customer.builder()
                        .firstName("David")
                        .lastName("Copperfield")
                        .dob(1982)
                        .balance(9800.00)
                        .build()
        );

        customerRepository.saveAll(customers);
        log.info("Database initialized with {} customers", customerRepository.count());
    }
}