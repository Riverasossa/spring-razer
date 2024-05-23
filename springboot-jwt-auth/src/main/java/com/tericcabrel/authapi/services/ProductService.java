package com.tericcabrel.authapi.services;

import com.tericcabrel.authapi.dtos.ProductDto;
import com.tericcabrel.authapi.entities.Product;
import com.tericcabrel.authapi.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> allProducts() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(ProductDto productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDto productDTO) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return null;
        }

        Product existingProduct = optionalProduct.get();
        existingProduct.setName(productDTO.getName());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setImage(productDTO.getImage());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
    }

    public ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setName(product.getName());
        productDto.setCategory(product.getCategory());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setImage(product.getImage());
        return productDto;
    }
}
