package com.zerobase.everycampingbackend.review.domain.repository;

import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.review.domain.entity.Review;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProduct(Product product);
    List<Review> findAllByCustomer(Customer customer);
}
