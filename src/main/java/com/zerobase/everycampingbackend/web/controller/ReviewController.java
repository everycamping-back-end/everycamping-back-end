package com.zerobase.everycampingbackend.web.controller;

import com.zerobase.everycampingbackend.domain.review.dto.ReviewDto;
import com.zerobase.everycampingbackend.domain.review.form.ReviewForm;
import com.zerobase.everycampingbackend.domain.review.service.ReviewService;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/{productId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> writeReview(@AuthenticationPrincipal Customer customer,
        @PathVariable Long productId,
        @RequestPart ReviewForm form, MultipartFile image)
        throws IOException {
        reviewService.writeReview(customer, productId, form, image);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{reviewId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> editReview(@AuthenticationPrincipal Customer customer,
        @PathVariable Long reviewId, @RequestPart ReviewForm form, MultipartFile image) throws IOException {
        reviewService.editReview(customer, reviewId, form, image);
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
