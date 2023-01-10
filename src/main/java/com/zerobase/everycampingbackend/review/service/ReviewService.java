package com.zerobase.everycampingbackend.review.service;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.common.staticimage.dto.S3Path;
import com.zerobase.everycampingbackend.common.staticimage.service.StaticImageService;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.service.ProductService;
import com.zerobase.everycampingbackend.review.domain.entity.Review;
import com.zerobase.everycampingbackend.review.domain.form.ReviewForm;
import com.zerobase.everycampingbackend.review.domain.repository.ReviewRepository;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import com.zerobase.everycampingbackend.user.service.CustomerService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final StaticImageService staticImageService;

    @Transactional
    public void writeReview(String userEmail, Long customerId, Long productId, ReviewForm form)
        throws IOException {
        log.info(userEmail + " -> 리뷰 작성 시도");

        Customer customer = customerService.getCustomerById(customerId);
        if (!customer.getEmail().equals(userEmail)) {
            throw new CustomException(ErrorCode.REVIEW_WRITER_NOT_QUALIFIED);
        }

        Product product = productService.getProductById(productId);
        // 구매한 상품이 맞는 지에 대한 검증 필요

        S3Path s3Path = staticImageService.saveImage(form.getImage());

        reviewRepository.save(Review.of(form, customer, product, s3Path));
        productService.addReview(product, form.getScore());

        log.info(userEmail + " -> 리뷰 작성 완료");
    }

    @Transactional
    public void editReview(String userEmail, Long reviewId, ReviewForm form) throws IOException{
        log.info(userEmail + " -> 리뷰 수정 시도");

        Review review = getReviewById(reviewId);
        if (!review.getCustomer().getEmail().equals(userEmail)) {
            throw new CustomException(ErrorCode.REVIEW_EDITOR_NOT_MATCHED);
        }

        S3Path s3Path = staticImageService.editImage(review.getImagePath(), form.getImage());

        Integer oldScore = review.getScore();
        review.setOf(form, s3Path);
        reviewRepository.save(review);

        productService.updateReview(review.getProduct(), review.getScore() - oldScore);

        log.info(userEmail + " -> 리뷰 수정 완료");
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    public List<Review> getReviewsByCustomer(Customer customer) {
        return reviewRepository.findAllByCustomer(customer);
    }

    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findAllByProduct(product);
    }

}
