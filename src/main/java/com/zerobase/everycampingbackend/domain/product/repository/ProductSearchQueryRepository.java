package com.zerobase.everycampingbackend.domain.product.repository;

import com.zerobase.everycampingbackend.domain.product.entity.ProductDocument;
import com.zerobase.everycampingbackend.domain.product.form.ProductSearchForm;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class ProductSearchQueryRepository {
    private final ElasticsearchOperations operations;

    public List<Long> findByCondition(ProductSearchForm form, Pageable pageable){
        CriteriaQuery query = generateQuery(form)
            .setPageable(pageable);

        SearchHits<ProductDocument> search = operations.search(query, ProductDocument.class);
        return search.getSearchHits().stream()
            .map(e -> e.getContent().getId())
            .collect(Collectors.toList());
    }

    private CriteriaQuery generateQuery(ProductSearchForm form) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());

        if (!ObjectUtils.isEmpty(form.getCategory())) {
            query.addCriteria(Criteria.where("category").is(form.getCategory()));
        }

        if(!ObjectUtils.isEmpty(form.getTags())) {
            query.addCriteria(Criteria.where("tags").in(form.getTags()));
        }

        if(StringUtils.hasText(form.getName())) {
            query.addCriteria(Criteria.where("name").contains(form.getName()));
        }

        return query;
    }
}
