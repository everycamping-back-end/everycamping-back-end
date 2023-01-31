package com.zerobase.everycampingbackend.domain.review.service;

import com.zerobase.everycampingbackend.exception.CustomException;
import com.zerobase.everycampingbackend.exception.ErrorCode;
import com.zerobase.everycampingbackend.domain.staticimage.dto.S3Path;
import com.zerobase.everycampingbackend.domain.staticimage.service.StaticImageService;
import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.product.service.ProductService;
import com.zerobase.everycampingbackend.domain.review.dto.ReviewDto;
import com.zerobase.everycampingbackend.domain.review.entity.Review;
import com.zerobase.everycampingbackend.domain.review.form.ReviewForm;
import com.zerobase.everycampingbackend.domain.review.repository.ReviewRepository;
import com.zerobase.everycampingbackend.domain.user.service.CustomerService;
import com.zerobase.everycampingbackend.domain.user.entity.Customer;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

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
    public void writeReview(Customer customer, Long productId, ReviewForm form, MultipartFile image)
        throws IOException, TaskRejectedException {
        log.info(customer.getEmail() + " -> 리뷰 작성 시도");

        Customer validCustomer = customerService.getCustomerById(customer.getId());
        if (!validCustomer.getEmail().equals(customer.getEmail())) {
            throw new CustomException(ErrorCode.REVIEW_WRITER_NOT_QUALIFIED);
        }

        Product product = productService.getProductById(productId);
        // 구매한 상품이 맞는 지에 대한 검증 필요

        S3Path s3Path = staticImageService.saveImage(image);

        reviewRepository.save(Review.of(form, customer, product, s3Path));
        productService.addReview(product, form.getScore());

        log.info(customer.getEmail() + " -> 리뷰 작성 완료");
    }

    @Transactional
    public void editReview(Customer customer, Long reviewId, ReviewForm form, MultipartFile image)
        throws IOException, TaskRejectedException {
        log.info(customer.getEmail() + " -> 리뷰 수정 시도");

        Review review = getReviewById(reviewId);
        if (!review.getCustomer().getEmail().equals(customer.getEmail())) {
            throw new CustomException(ErrorCode.REVIEW_EDITOR_NOT_MATCHED);
        }

        S3Path s3Path = staticImageService.editImage(review.getImagePath(), image);
        if (ObjectUtils.isEmpty(image)) {
            s3Path.setImageUri(review.getImageUri());
            s3Path.setImagePath(review.getImagePath());
        }

        Integer oldScore = review.getScore();
        review.setOf(form, s3Path);
        reviewRepository.save(review);

        productService.updateReview(review.getProduct(), review.getScore() - oldScore);

        log.info(customer.getEmail() + " -> 리뷰 수정 완료");
    }

    @Transactional
    public void deleteReview(Customer customer, Long reviewId) throws TaskRejectedException {
        log.info(customer.getEmail() + " -> 리뷰 삭제 시도");

        Review review = getReviewById(reviewId);
        if (!review.getCustomer().getEmail().equals(customer.getEmail())) {
            throw new CustomException(ErrorCode.REVIEW_EDITOR_NOT_MATCHED);
        }

        staticImageService.deleteImage(review.getImagePath());
        productService.deleteReview(review.getProduct(), review.getScore());
        reviewRepository.delete(review);

        log.info(customer.getEmail() + " -> 리뷰 삭제 완료");
    }

    public ReviewDto getReviewDetail(Long reviewId) {
        return ReviewDto.from(getReviewById(reviewId));
    }

    public List<ReviewDto> getReviewsByCustomerId(Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return reviewRepository.findAllByCustomer(customer)
            .stream().map(ReviewDto::from).collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByProductId(Long productId) {
        Product product = productService.getProductById(productId);
        return reviewRepository.findAllByProduct(product)
            .stream().map(ReviewDto::from).collect(Collectors.toList());
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
