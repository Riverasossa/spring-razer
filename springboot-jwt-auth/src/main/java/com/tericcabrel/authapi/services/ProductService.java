package com.tericcabrel.authapi.services;

import com.tericcabrel.authapi.dtos.ProductDto;
import com.tericcabrel.authapi.entities.Product;
import com.tericcabrel.authapi.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /*public Page<Product> filterProducts(String name, List<String> categories, double minPrice, double maxPrice, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            return productRepository.findByNameContaining(name, pageable);
        } else if (categories != null && !categories.isEmpty()) {
            return productRepository.findByCategoryIn(categories, pageable);
        } else if (minPrice >= 0 && maxPrice >= minPrice) {
            return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }*/

    public List<Product> allProducts() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(ProductDto productDTO) {
        Optional<Product> existingProduct = productRepository.findByName(productDTO.getName());

        if (existingProduct.isPresent()) {
            throw new RuntimeException("A product with this name already exists.");
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        return productRepository.save(product);
    }

    private String saveImageLocally(MultipartFile imageFile) throws IOException {
        String fileName = imageFile.getOriginalFilename();
        String filePath = "C:/products/" + fileName; // Ruta donde se guardarán las imágenes

        File directory = new File("C:/products");
        if (!directory.exists()) {
            directory.mkdirs(); // Crea el directorio si no existe
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageFile.getBytes());
        }

        return filePath;
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