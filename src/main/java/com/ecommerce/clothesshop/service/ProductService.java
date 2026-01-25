package com.ecommerce.clothesshop.service;

import com.ecommerce.clothesshop.dto.ProductRequest;
import com.ecommerce.clothesshop.dto.ProductResponse;
import com.ecommerce.clothesshop.model.Product;
import com.ecommerce.clothesshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    
    public Flux<ProductResponse> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue()
            .map(this::mapToResponse)
            .doOnError(e -> log.error("Error fetching products", e));
    }
    
    public Mono<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id)
            .map(this::mapToResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Product not found with id: " + id)));
    }
    
    public Flux<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword)
            .map(this::mapToResponse);
    }
    
    public Flux<ProductResponse> filterProducts(String category, String brand, 
                                                Double minPrice, Double maxPrice, String gender) {
        return productRepository.findByFilters(category, brand, minPrice, maxPrice, gender)
            .map(this::mapToResponse);
    }
    
    public Flux<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategory(category)
            .map(this::mapToResponse);
    }
    
    public Mono<ProductResponse> createProduct(ProductRequest request) {
        Product product = Product.builder()
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .category(request.getCategory())
            .subCategory(request.getSubCategory())
            .brand(request.getBrand())
            .size(request.getSize())
            .color(request.getColor())
            .gender(request.getGender())
            .stockQuantity(request.getStockQuantity())
            .imageUrl(request.getImageUrl())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
            
        return productRepository.save(product)
            .map(this::mapToResponse)
            .doOnSuccess(p -> log.info("Product created: {}", p.getName()));
    }
    
    public Mono<ProductResponse> updateProduct(Long id, ProductRequest request) {
        return productRepository.findById(id)
            .flatMap(product -> {
                product.setName(request.getName());
                product.setDescription(request.getDescription());
                product.setPrice(request.getPrice());
                product.setCategory(request.getCategory());
                product.setSubCategory(request.getSubCategory());
                product.setBrand(request.getBrand());
                product.setSize(request.getSize());
                product.setColor(request.getColor());
                product.setGender(request.getGender());
                product.setStockQuantity(request.getStockQuantity());
                product.setImageUrl(request.getImageUrl());
                if (request.getIsActive() != null) {
                    product.setIsActive(request.getIsActive());
                }
                product.setUpdatedAt(LocalDateTime.now());
                
                return productRepository.save(product);
            })
            .map(this::mapToResponse)
            .switchIfEmpty(Mono.error(new RuntimeException("Product not found with id: " + id)));
    }
    
    public Mono<Void> deleteProduct(Long id) {
        return productRepository.deleteById(id)
            .doOnSuccess(v -> log.info("Product deleted: {}", id));
    }
    
    public Mono<ProductResponse> updateStock(Long productId, Integer quantity) {
        return productRepository.findById(productId)
            .flatMap(product -> {
                int newStock = product.getStockQuantity() - quantity;
                if (newStock < 0) {
                    return Mono.error(new RuntimeException("Insufficient stock"));
                }
                product.setStockQuantity(newStock);
                product.setUpdatedAt(LocalDateTime.now());
                return productRepository.save(product);
            })
            .map(this::mapToResponse);
    }
    
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .category(product.getCategory())
            .subCategory(product.getSubCategory())
            .brand(product.getBrand())
            .size(product.getSize())
            .color(product.getColor())
            .gender(product.getGender())
            .stockQuantity(product.getStockQuantity())
            .imageUrl(product.getImageUrl())
            .isActive(product.getIsActive())
            .build();
    }
}