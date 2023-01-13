package com.zerobase.everycampingbackend.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.zerobase.everycampingbackend.common.exception.CustomException;
import com.zerobase.everycampingbackend.common.exception.ErrorCode;
import com.zerobase.everycampingbackend.common.staticimage.dto.S3Path;
import com.zerobase.everycampingbackend.common.staticimage.service.StaticImageService;
import com.zerobase.everycampingbackend.product.domain.entity.Product;
import com.zerobase.everycampingbackend.product.service.ProductService;
import com.zerobase.everycampingbackend.review.domain.dto.ReviewDto;
import com.zerobase.everycampingbackend.review.domain.entity.Review;
import com.zerobase.everycampingbackend.review.domain.form.ReviewForm;
import com.zerobase.everycampingbackend.review.domain.repository.ReviewRepository;
import com.zerobase.everycampingbackend.user.domain.entity.Customer;
import com.zerobase.everycampingbackend.user.service.CustomerService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private ProductService productService;
    @Mock
    private StaticImageService staticImageService;
    @InjectMocks
    private ReviewService reviewService;

    private final ReviewForm form = new ReviewForm(1, "리뷰", null);
    private String userEmail = "aaa";
    private final Customer customer = Customer.builder().id(1L).email("aaa").build();
    private final Product product = Product.builder().id(1L).build();
    private final Review review = Review.builder()
        .id(1L)
        .customer(customer)
        .product(product)
        .score(5)
        .text("리뷰아님")
        .build();
    private final S3Path s3Path = new S3Path("uri", "path");

    @Test
    @DisplayName("리뷰 작성 성공")
    void success_writeReview() throws IOException {
        // given
        given(customerService.getCustomerById(anyLong()))
            .willReturn(customer);
        given(productService.getProductById(anyLong()))
            .willReturn(product);
        given(staticImageService.saveImage(any()))
            .willReturn(s3Path);
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        // when
        reviewService.writeReview(userEmail, 1L, 1L, form);

        // then
        verify(reviewRepository).save(captor.capture());
        assertEquals(product.getId(), captor.getValue().getProduct().getId());
        assertEquals(customer.getId(), captor.getValue().getCustomer().getId());
        assertEquals(s3Path.getImageUri(), captor.getValue().getImageUri());
        assertEquals(s3Path.getImagePath(), captor.getValue().getImagePath());
        assertEquals(form.getScore(), captor.getValue().getScore());
        assertEquals(form.getText(), captor.getValue().getText());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 로그인 유저와 폼 유저 정보 불일치")
    void fail_writeReview_customerNotMatched(){
        // given
        userEmail = "eeeee";
        given(customerService.getCustomerById(anyLong()))
            .willReturn(customer);

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
            reviewService.writeReview(userEmail, 1L, 1L, form));

        // then
        assertEquals(ErrorCode.REVIEW_WRITER_NOT_QUALIFIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void success_editReview() throws IOException {
        // given
        review.setImageUri("fjweifjwofowjfowef");
        review.setImagePath("vmvermvpermvpermpver");
        given(reviewRepository.findById(anyLong()))
            .willReturn(Optional.of(review));
        given(staticImageService.editImage(anyString(), any()))
            .willReturn(s3Path);
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        // when
        reviewService.editReview(userEmail, 1L, form);

        // then
        verify(reviewRepository).save(captor.capture());
        assertEquals(s3Path.getImageUri(), captor.getValue().getImageUri());
        assertEquals(s3Path.getImagePath(), captor.getValue().getImagePath());
        assertEquals(form.getScore(), captor.getValue().getScore());
        assertEquals(form.getText(), captor.getValue().getText());
    }

    @Test
    @DisplayName("리뷰 수정 실패 - 해당 리뷰 없음")
    void fail_editReview_reviewNotFound(){
        // given
        userEmail = "not-matched-user";
        given(reviewRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
            reviewService.editReview(userEmail, 1L, form));

        // then
        assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 수정 실패 - 해당 리뷰 수정 권한 없음")
    void fail_editReview_userNotEditor(){
        // given
        userEmail = "not-matched-user";
        given(reviewRepository.findById(anyLong()))
            .willReturn(Optional.of(review));

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
            reviewService.editReview(userEmail, 1L, form));

        // then
        assertEquals(ErrorCode.REVIEW_EDITOR_NOT_MATCHED, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void success_deleteReview(){
        // given
        given(reviewRepository.findById(anyLong()))
            .willReturn(Optional.of(review));
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        // when
        reviewService.deleteReview(userEmail, 1L);

        // then
        verify(reviewRepository).delete(captor.capture());
    }

    @Test
    @DisplayName("리뷰 삭제 실패 - 해당 리뷰 없음")
    void fail_deleteReview_reviewNotFound(){
        // given
        given(reviewRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
            reviewService.deleteReview(userEmail, 1L));

        // then
        assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 삭제 실패 - 리뷰 삭제 권한 없음")
    void fail_deleteReview_userNotEditor(){
        // given
        userEmail = "wfqwfqf";
        given(reviewRepository.findById(anyLong()))
            .willReturn(Optional.of(review));

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
            reviewService.deleteReview(userEmail, 1L));

        // then
        assertEquals(ErrorCode.REVIEW_EDITOR_NOT_MATCHED, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 조회 성공")
    void success_getReviewDetail(){
        // given
        given(reviewRepository.findById(anyLong()))
            .willReturn(Optional.of(review));

        // when
        ReviewDto result = reviewService.getReviewDetail(1L);

        // then
        assertEquals(review.getScore(), result.getScore());
        assertEquals(review.getText(), result.getText());
        assertEquals(review.getImageUri(), result.getImageUri());
        assertEquals(review.getCustomer().getNickName(), result.getCustomerName());
    }

    @Test
    @DisplayName("리뷰 조회 실패 - 해당 리뷰 없음")
    void fail_getReviewDetail_reviewNotFound(){
        // given
        given(reviewRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
            reviewService.getReviewDetail(1L));

        // then
        assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저별 리뷰 조회 성공")
    void success_getReviewsByCustomerId(){
        // given
        List<Review> list = List.of(review);
        given(customerService.getCustomerById(anyLong()))
            .willReturn(customer);
        given(reviewRepository.findAllByCustomer(any()))
            .willReturn(list);

        // when
        List<ReviewDto> result = reviewService.getReviewsByCustomerId(1L);

        // then
        assertEquals(review.getScore(), result.get(0).getScore());
        assertEquals(review.getText(), result.get(0).getText());
        assertEquals(review.getImageUri(), result.get(0).getImageUri());
        assertEquals(review.getCustomer().getNickName(), result.get(0).getCustomerName());
    }

    @Test
    @DisplayName("상품별 리뷰 조회 성공")
    void success_getReviewsByProductId(){
        // given
        List<Review> list = List.of(review);
        given(productService.getProductById(anyLong()))
            .willReturn(product);
        given(reviewRepository.findAllByProduct(any()))
            .willReturn(list);

        // when
        List<ReviewDto> result = reviewService.getReviewsByProductId(1L);

        // then
        assertEquals(review.getScore(), result.get(0).getScore());
        assertEquals(review.getText(), result.get(0).getText());
        assertEquals(review.getImageUri(), result.get(0).getImageUri());
        assertEquals(review.getCustomer().getNickName(), result.get(0).getCustomerName());
    }
}