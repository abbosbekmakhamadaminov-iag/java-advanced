package com.epam.javadvanced.service;

import com.epam.javadvanced.avro.EventType;
import com.epam.javadvanced.domain.Product;
import com.epam.javadvanced.dto.ProductDto;
import com.epam.javadvanced.exceptions.ResourceNotFoundException;
import com.epam.javadvanced.mapper.ProductMapper;
import com.epam.javadvanced.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaProducerService kafkaProducerService;

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        product.setId(null);
        Product savedProduct = productRepository.save(product);

        kafkaProducerService.sendProductEvent(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                EventType.CREATED
        );
        return productMapper.toDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        Product product = productMapper.toEntity(productDto);
        product.setId(id);
        Product updatedProduct = productRepository.save(product);

        kafkaProducerService.sendProductEvent(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                EventType.UPDATED
        );
        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.deleteById(id);

        kafkaProducerService.sendProductEvent(
                product.getId(),
                product.getName(),
                product.getPrice(),
                EventType.DELETED
        );
    }
}