package com.example.api.repository;

import com.example.api.model.ExampleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ExampleRepository extends JpaRepository<ExampleResponse, Long> {
    @Query("SELECT e FROM ExampleResponse e WHERE e.name LIKE %:keyword%")
    Page<ExampleResponse> findByNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);
}
