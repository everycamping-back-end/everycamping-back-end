package com.zerobase.everycampingbackend.review.controller;

import com.zerobase.everycampingbackend.review.domain.dto.ReviewDto;
import com.zerobase.everycampingbackend.review.domain.form.ReviewForm;
import com.zerobase.everycampingbackend.review.service.ReviewService;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> writeReview(@AuthenticationPrincipal Customer customer,
        @RequestBody Long productId,
        @RequestBody ReviewForm form)
        throws IOException {
        reviewService.writeReview(customer, productId, form);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> editReview(@AuthenticationPrincipal Customer customer,
        @PathVariable Long reviewId, @RequestBody ReviewForm form) throws IOException {
        reviewService.editReview(customer, reviewId, form);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@AuthenticationPrincipal Customer customer, @PathVariable Long reviewId) {
        reviewService.deleteReview(customer, reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewDetail(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewDetail(reviewId));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(reviewService.getReviewsByCustomerId(customerId));
    }

}
