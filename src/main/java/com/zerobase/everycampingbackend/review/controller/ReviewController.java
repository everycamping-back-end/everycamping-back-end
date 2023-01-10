package com.zerobase.everycampingbackend.review.controller;

import com.zerobase.everycampingbackend.review.domain.form.ReviewForm;
import com.zerobase.everycampingbackend.review.service.ReviewService;
import java.io.IOException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Boolean> writeReview(Principal principal, @RequestBody ReviewForm form)
        throws IOException {
        reviewService.writeReview(principal.getName(), form);
        return ResponseEntity.ok(true);
    }

}
