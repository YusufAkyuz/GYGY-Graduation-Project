package com.telcox.productcatalogservice.web;

import com.telcox.common.web.PageResponse;
import com.telcox.productcatalogservice.dto.CreateProductRequest;
import com.telcox.productcatalogservice.dto.ProductResponse;
import com.telcox.productcatalogservice.dto.UpdatePriceRequest;
import com.telcox.productcatalogservice.service.ProductCatalogService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductCatalogService productCatalogService;

    public ProductController(ProductCatalogService productCatalogService) {
        this.productCatalogService = productCatalogService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        return ProductResponse.from(productCatalogService.create(request));
    }

    @GetMapping("/{code}")
    public ProductResponse getCurrent(@PathVariable String code) {
        return ProductResponse.from(productCatalogService.getCurrentByCode(code));
    }

    @GetMapping("/versions/{id}")
    public ProductResponse getVersion(@PathVariable UUID id) {
        return ProductResponse.from(productCatalogService.getVersionById(id));
    }

    @GetMapping
    public PageResponse<ProductResponse> list(Pageable pageable) {
        return PageResponse.from(productCatalogService.list(pageable).map(ProductResponse::from));
    }

    @PutMapping("/{code}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse changePrice(@PathVariable String code, @Valid @RequestBody UpdatePriceRequest request) {
        return ProductResponse.from(productCatalogService.changePrice(code, request.monthlyPrice()));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable String code) {
        productCatalogService.deactivate(code);
        return ResponseEntity.noContent().build();
    }
}
