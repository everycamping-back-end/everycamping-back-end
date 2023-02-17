package com.zerobase.everycampingbackend.domain.product.repository;

import com.zerobase.everycampingbackend.domain.product.entity.Product;
import com.zerobase.everycampingbackend.domain.user.entity.Seller;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Page<Product> findAllBySeller(Seller seller, Pageable pageable);

    @Query("Select p from Product p where p.id in :ids order by field(p.id, :ids)")
    List<Product> findAllByIdIn(@Param("ids") List<Long> ids);
}
