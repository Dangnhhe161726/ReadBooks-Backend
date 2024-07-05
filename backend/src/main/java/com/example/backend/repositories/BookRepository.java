package com.example.backend.repositories;

import com.example.backend.models.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
  @Query(
      "SELECT DISTINCT p FROM Book  p "
          + "WHERE UPPER(p.name) LIKE "
          + "CONCAT('%', UPPER(:keyword), '%' )" )
  Page<Book> findAll( String keyword,Pageable pageable);
  Page<Book> findByNameContaining(String name, Pageable pageable);
}
