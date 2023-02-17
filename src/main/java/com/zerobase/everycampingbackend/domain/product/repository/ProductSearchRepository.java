package com.zerobase.everycampingbackend.domain.product.repository;

import com.zerobase.everycampingbackend.domain.product.entity.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

}
