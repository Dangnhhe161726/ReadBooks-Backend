package com.example.backend.repositories;

import com.example.backend.models.entities.Book;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(
            "SELECT DISTINCT p FROM Book  p "
                    + "WHERE UPPER(p.name) LIKE "
                    + "CONCAT('%', UPPER(:keyword), '%' )")
    Page<Book> findAll(String keyword, Pageable pageable);

    Page<Book> findByNameContaining(String name, Pageable pageable);

    @Query(
            "SELECT DISTINCT p FROM Book  p "
                    + "WHERE p.author.id = :id")
    List<Book> findBooksByAuthor(Long id);

  @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
  List<Book> findBooksByCategoryId(@Param("categoryId") Long categoryId);

  @Query("SELECT b FROM Book b JOIN b.author c WHERE c.id = :authorId")
  List<Book> findBooksByAuthorId(@Param("authorId") Long authorId);
}
