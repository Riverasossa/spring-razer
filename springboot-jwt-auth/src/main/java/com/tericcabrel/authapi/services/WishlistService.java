package com.tericcabrel.authapi.services;

import com.tericcabrel.authapi.dtos.*;
import com.tericcabrel.authapi.entities.*;
import com.tericcabrel.authapi.repositories.ProductRepository;
import com.tericcabrel.authapi.repositories.UserRepository;
import com.tericcabrel.authapi.repositories.WishlistRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public WishlistService(WishlistRepository wishlistRepository,
                               UserRepository userRepository,
                               ProductRepository productRepository,
                               ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public Wishlist getWishlistByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(User::getWishlist).orElse(null);
    }

    @Transactional
    public WishlistDto getWishlistDtoByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Wishlist wishlist = userOptional.get().getWishlist();
            Hibernate.initialize(wishlist.getWishlistProducts());
            return convertToDto(wishlist);
        } else {
            return null;
        }
    }

    @Transactional
    public void createWishlistForUser(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlistRepository.save(wishlist);
        user.setWishlist(wishlist);
    }

    @Transactional
    public WishlistDto addProductToWishlist(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                User user = userOptional.get();
                Product product = productOptional.get();

                Wishlist wishlist = user.getWishlist();
                Optional<WishlistProduct> existingProductOptional = wishlist.getWishlistProducts().stream()
                        .filter(p -> p.getProduct().equals(product))
                        .findFirst();

                if (existingProductOptional.isPresent()) {
                    WishlistProduct existingProduct = existingProductOptional.get();
                    existingProduct.setQuantity(existingProduct.getQuantity() + 1);
                } else {
                    WishlistProduct newProduct = new WishlistProduct();
                    newProduct.setWishlist(wishlist);
                    newProduct.setProduct(product);
                    newProduct.setQuantity(1);

                    wishlist.getWishlistProducts().add(newProduct);
                }

                wishlistRepository.save(wishlist);

                return convertToDto(wishlist);
            } else {
                throw new RuntimeException("Product not found");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public WishlistDto removeProductFromWishlist(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                User user = userOptional.get();
                Product product = productOptional.get();

                Wishlist wishlist = user.getWishlist();
                Optional<WishlistProduct> existingProductOptional = wishlist.getWishlistProducts().stream()
                        .filter(p -> p.getProduct().equals(product))
                        .findFirst();

                if (existingProductOptional.isPresent()) {
                    WishlistProduct existingProduct = existingProductOptional.get();
                    wishlist.getWishlistProducts().remove(existingProduct);

                    wishlistRepository.save(wishlist);

                    return convertToDto(wishlist);
                } else {
                    throw new RuntimeException("Product not found in the wishlist");
                }
            } else {
                throw new RuntimeException("Product not found");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public WishlistDto decreaseProductQuantity(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                User user = userOptional.get();
                Product product = productOptional.get();

                Wishlist wishlist = user.getWishlist();
                Optional<WishlistProduct> existingProductOptional = wishlist.getWishlistProducts().stream()
                        .filter(p -> p.getProduct().equals(product))
                        .findFirst();

                if (existingProductOptional.isPresent()) {
                    WishlistProduct existingProduct = existingProductOptional.get();
                    int newQuantity = existingProduct.getQuantity() - 1;
                    if (newQuantity > 0) {
                        existingProduct.setQuantity(newQuantity);
                    } else {
                        wishlist.getWishlistProducts().remove(existingProduct);
                    }

                    wishlistRepository.save(wishlist);

                    return convertToDto(wishlist);
                } else {
                    throw new RuntimeException("Product not found in the cart");
                }
            } else {
                throw new RuntimeException("Product not found");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public WishlistDto clearWishlist(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Wishlist wishlist = user.getWishlist();

            wishlist.getWishlistProducts().clear();

            wishlistRepository.save(wishlist);

            return convertToDto(wishlist);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private WishlistDto convertToDto(Wishlist wishlist) {
        WishlistDto wishlistDto = new WishlistDto();
        wishlistDto.setWishlistId(wishlist.getWishlistId());
        Set<WishlistProductDto> wishlistProductDtos = wishlist.getWishlistProducts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
        wishlistDto.setProducts(wishlistProductDtos);
        return wishlistDto;
    }

    private WishlistProductDto convertToDto(WishlistProduct wishlistProduct) {
        WishlistProductDto wishlistProductDto = new WishlistProductDto();
        wishlistProductDto.setWishlistProductId(wishlistProduct.getId());

        ProductDto productDto = productService.convertToDto(wishlistProduct.getProduct());
        wishlistProductDto.setProduct(productDto);

        wishlistProductDto.setQuantity(wishlistProduct.getQuantity());

        return wishlistProductDto;
    }
}
