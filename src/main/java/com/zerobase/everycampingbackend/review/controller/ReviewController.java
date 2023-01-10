package com.zerobase.everycampingbackend.review.controller;

import com.zerobase.everycampingbackend.review.domain.dto.ReviewDto;
import com.zerobase.everycampingbackend.review.domain.form.ReviewForm;
import com.zerobase.everycampingbackend.review.service.ReviewService;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Boolean> writeReview(Principal principal,
        @RequestBody Long customerId,
        @RequestBody Long productId,
        @RequestBody ReviewForm form)
        throws IOException {
        reviewService.writeReview(principal.getName(), customerId, productId, form);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Boolean> editReview(Principal principal,
        @PathVariable Long reviewId, @RequestBody ReviewForm form) throws IOException {
        reviewService.editReview(principal.getName(), reviewId, form);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Boolean> deleteReview(Principal principal, @PathVariable Long reviewId) {
        reviewService.deleteReview(principal.getName(), reviewId);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewDetail(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewDetail(reviewId));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsById(@RequestParam Long customerId,
        @RequestParam Long productId) {
        List<ReviewDto> reviews = null;
        if(!ObjectUtils.isEmpty(customerId)){
            reviews = reviewService.getReviewsByCustomerId(customerId);
        } else if(!ObjectUtils.isEmpty(productId)){
            reviews = reviewService.getReviewsByProductId(productId);
        }
        return ResponseEntity.ok(reviews);
    }

}
