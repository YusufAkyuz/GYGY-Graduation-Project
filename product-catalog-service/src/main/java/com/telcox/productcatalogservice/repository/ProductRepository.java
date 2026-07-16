package com.telcox.productcatalogservice.repository;

import com.telcox.productcatalogservice.domain.Product;
import com.telcox.productcatalogservice.domain.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByCodeAndEffectiveToIsNull(String code);

    boolean existsByCodeAndEffectiveToIsNull(String code);

    Page<Product> findAllByEffectiveToIsNull(Pageable pageable);

    Page<Product> findAllByEffectiveToIsNullAndProductType(ProductType productType, Pageable pageable);
}
