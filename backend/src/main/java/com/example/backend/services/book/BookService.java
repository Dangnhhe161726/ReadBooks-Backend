package com.example.backend.services.book;

import com.example.backend.models.dtos.BookDTO;
import com.example.backend.models.entities.Book;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public interface BookService {

  Page<BookDTO> getByPaging(
      int pageNo, int pageSize, String sortBy, String sortDirection, String keyword);
  BookDTO getById(Long id);

  BookDTO save(BookDTO category);

  BookDTO update(@NonNull BookDTO category);

}
