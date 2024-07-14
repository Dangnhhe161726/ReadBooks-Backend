package com.example.backend.repositories;

import com.example.backend.models.entities.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    @Query("SELECT DISTINCT bm FROM  BookMark bm WHERE  bm.book.id = :bookId AND bm.userEntity.id = :userId")
    List<BookMark> findByBookIdAAndUserId(Long bookId, Long userId);


}
