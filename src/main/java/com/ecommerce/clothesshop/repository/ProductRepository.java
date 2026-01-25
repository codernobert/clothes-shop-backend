package com.ecommerce.clothesshop.repository;

import com.ecommerce.clothesshop.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface ProductRepository extends R2dbcRepository<Product, Long> {
    Flux<Product> findByCategory(String category);
    Flux<Product> findByBrand(String brand);
    Flux<Product> findByIsActiveTrue();
    
    @Query("SELECT * FROM products WHERE is_active = true AND " +
           "(:category IS NULL OR category = :category) AND " +
           "(:brand IS NULL OR brand = :brand) AND " +
           "(:minPrice IS NULL OR price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR price <= :maxPrice) AND " +
           "(:gender IS NULL OR gender = :gender)")
    Flux<Product> findByFilters(
        @Param("category") String category,
        @Param("brand") String brand,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("gender") String gender
    );
    
    @Query("SELECT * FROM products WHERE is_active = true AND " +
           "(LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Flux<Product> searchProducts(@Param("keyword") String keyword);
}
