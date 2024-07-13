package com.example.backend.repositories;

import com.example.backend.models.entities.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Boolean existsByName(String name);

    Page<Author> findByNameContaining(String name, Pageable pageable);
}
