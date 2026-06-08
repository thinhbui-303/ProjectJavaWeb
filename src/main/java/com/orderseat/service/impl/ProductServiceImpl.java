package com.orderseat.service.impl;

import com.orderseat.entity.Product;
import com.orderseat.repository.ProductRepository;
import com.orderseat.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final com.orderseat.repository.ProductRepository productRepository;
    private final com.orderseat.repository.CategoryRepository categoryRepository;
    private final com.orderseat.util.FileStorageUtil fileStorageUtil;

    @Override
    public List<com.orderseat.entity.Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public com.orderseat.entity.Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
    }

    @Override
    public java.util.List<com.orderseat.entity.Product> getProductsByCategory(Long categoryId) {
        com.orderseat.entity.Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        
        java.util.List<Long> categoryIds = new java.util.ArrayList<>();
        categoryIds.add(category.getId());
        if (category.getSubCategories() != null) {
            for (com.orderseat.entity.Category sub : category.getSubCategories()) {
                categoryIds.add(sub.getId());
            }
        }
        return productRepository.findByCategoryIdIn(categoryIds);
    }
    @Override
    public org.springframework.data.domain.Page<com.orderseat.entity.Product> getProductsPaged(String keyword, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public org.springframework.data.domain.Page<com.orderseat.entity.Product> getProductsByCategoryPaged(Long categoryId, String keyword, int page, int size) {
        com.orderseat.entity.Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        
        java.util.List<Long> categoryIds = new java.util.ArrayList<>();
        categoryIds.add(category.getId());
        if (category.getSubCategories() != null) {
            for (com.orderseat.entity.Category sub : category.getSubCategories()) {
                categoryIds.add(sub.getId());
            }
        }
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.trim().isEmpty()) {
            return productRepository.findByCategoryIdInAndNameContainingIgnoreCase(categoryIds, keyword.trim(), pageable);
        }
        
        return productRepository.findByCategoryIdIn(categoryIds, pageable);
    }
    @Override
    @org.springframework.transaction.annotation.Transactional
    public void saveProduct(com.orderseat.dto.ProductRequest request) {
        com.orderseat.entity.Product product;
        
        if (request.getId() != null) {
            product = getProductById(request.getId());
        } else {
            product = new com.orderseat.entity.Product();
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        
        // Mapping Category
        com.orderseat.entity.Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        product.setCategory(category);

        // Handle Image Upload
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            String imagePath = fileStorageUtil.storeFile(request.getImageFile());
            product.setImageUrl(imagePath);
        }

        productRepository.save(product);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            if (product.getCategory() != null) {
                product.getCategory().getProducts().remove(product);
            }
            productRepository.delete(product);
        }
    }
}
