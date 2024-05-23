package com.tericcabrel.authapi.services;

import com.tericcabrel.authapi.dtos.ProductDto;
import com.tericcabrel.authapi.dtos.ShoppingCartDto;
import com.tericcabrel.authapi.dtos.ShoppingCartProductDto;
import com.tericcabrel.authapi.entities.Product;
import com.tericcabrel.authapi.entities.ShoppingCart;
import com.tericcabrel.authapi.entities.ShoppingCartProduct;
import com.tericcabrel.authapi.entities.User;
import com.tericcabrel.authapi.repositories.ProductRepository;
import com.tericcabrel.authapi.repositories.ShoppingCartRepository;
import com.tericcabrel.authapi.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               UserRepository userRepository,
                               ProductRepository productRepository,
                               ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public ShoppingCart getShoppingCartByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(User::getShoppingCart).orElse(null);
    }

    @Transactional
    public ShoppingCartDto getShoppingCartDtoByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            ShoppingCart shoppingCart = userOptional.get().getShoppingCart();
            Hibernate.initialize(shoppingCart.getShoppingCartProducts());
            return convertToDto(shoppingCart);
        } else {
            return null;
        }
    }


    @Transactional
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        user.setShoppingCart(shoppingCart);
    }

    @Transactional
    public ShoppingCartDto addProductToCart(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                User user = userOptional.get();
                Product product = productOptional.get();

                ShoppingCart shoppingCart = user.getShoppingCart();
                Optional<ShoppingCartProduct> existingProductOptional = shoppingCart.getShoppingCartProducts().stream()
                        .filter(p -> p.getProduct().equals(product))
                        .findFirst();

                if (existingProductOptional.isPresent()) {
                    ShoppingCartProduct existingProduct = existingProductOptional.get();
                    existingProduct.setQuantity(existingProduct.getQuantity() + 1);
                } else {
                    ShoppingCartProduct newProduct = new ShoppingCartProduct();
                    newProduct.setShoppingCart(shoppingCart);
                    newProduct.setProduct(product);
                    newProduct.setQuantity(1);

                    shoppingCart.getShoppingCartProducts().add(newProduct);
                }

                shoppingCart.setTotal(calculateTotal(shoppingCart));

                shoppingCartRepository.save(shoppingCart);

                return convertToDto(shoppingCart);
            } else {
                throw new RuntimeException("Product not found");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public ShoppingCartDto removeProductFromCart(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                User user = userOptional.get();
                Product product = productOptional.get();

                ShoppingCart shoppingCart = user.getShoppingCart();
                Optional<ShoppingCartProduct> existingProductOptional = shoppingCart.getShoppingCartProducts().stream()
                        .filter(p -> p.getProduct().equals(product))
                        .findFirst();

                if (existingProductOptional.isPresent()) {
                    ShoppingCartProduct existingProduct = existingProductOptional.get();
                    shoppingCart.getShoppingCartProducts().remove(existingProduct);

                    shoppingCart.setTotal(calculateTotal(shoppingCart));

                    shoppingCartRepository.save(shoppingCart);

                    return convertToDto(shoppingCart);
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
    public ShoppingCartDto decreaseProductQuantity(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                User user = userOptional.get();
                Product product = productOptional.get();

                ShoppingCart shoppingCart = user.getShoppingCart();
                Optional<ShoppingCartProduct> existingProductOptional = shoppingCart.getShoppingCartProducts().stream()
                        .filter(p -> p.getProduct().equals(product))
                        .findFirst();

                if (existingProductOptional.isPresent()) {
                    ShoppingCartProduct existingProduct = existingProductOptional.get();
                    int newQuantity = existingProduct.getQuantity() - 1;
                    if (newQuantity > 0) {
                        existingProduct.setQuantity(newQuantity);
                    } else {
                        shoppingCart.getShoppingCartProducts().remove(existingProduct);
                    }

                    shoppingCart.setTotal(calculateTotal(shoppingCart));

                    shoppingCartRepository.save(shoppingCart);

                    return convertToDto(shoppingCart);
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
    public ShoppingCartDto clearCart(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ShoppingCart shoppingCart = user.getShoppingCart();

            shoppingCart.getShoppingCartProducts().clear();

            shoppingCart.setTotal(0.0);

            shoppingCartRepository.save(shoppingCart);

            return convertToDto(shoppingCart);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private ShoppingCartDto convertToDto(ShoppingCart shoppingCart) {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setShoppingCartId(shoppingCart.getShoppingCartId());
        shoppingCartDto.setTotal(shoppingCart.getTotal());
        Set<ShoppingCartProductDto> shoppingCartProductDtos = shoppingCart.getShoppingCartProducts().stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
        shoppingCartDto.setProducts(shoppingCartProductDtos);
        return shoppingCartDto;
    }

    private ShoppingCartProductDto convertToDto(ShoppingCartProduct shoppingCartProduct) {
        ShoppingCartProductDto shoppingCartProductDto = new ShoppingCartProductDto();
        shoppingCartProductDto.setShopingCartProductId(shoppingCartProduct.getId());

        ProductDto productDto = productService.convertToDto(shoppingCartProduct.getProduct());
        shoppingCartProductDto.setProduct(productDto);

        shoppingCartProductDto.setQuantity(shoppingCartProduct.getQuantity());

        return shoppingCartProductDto;
    }

    private double calculateTotal(ShoppingCart shoppingCart) {
        double total = 0.0;
        for (ShoppingCartProduct product : shoppingCart.getShoppingCartProducts()) {
            double price = product.getProduct().getPrice();
            total += price * product.getQuantity();
        }
        return total;
    }
}
