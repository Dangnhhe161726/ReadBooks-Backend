package com.example.backend.repositories;

import com.example.backend.models.entities.Book;
import com.example.backend.models.entities.UserBook;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {

  @Query("SELECT ub.book FROM UserBook ub WHERE ub.userEntity.id = :userId")
  List<Book> findBooksByUserId(@Param("userId") Long userId);


}
