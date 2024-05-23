package com.tericcabrel.authapi.repositories;

import com.tericcabrel.authapi.entities.Wishlist;
import com.tericcabrel.authapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
}
