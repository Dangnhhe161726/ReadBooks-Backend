package com.example.backend.repositories;

import com.example.backend.models.entities.Author;
import com.example.backend.models.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByName(String name);

    Page<Category> findByNameContaining(String name, Pageable pageable);
}
