package com.example.backend.services.book;

import com.example.backend.models.dtos.BookDTO;
import com.example.backend.models.responses.BookResponse;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public interface IBookService {

  Page<BookDTO> getByPaging(
      int pageNo, int pageSize, String sortBy, String sortDirection, String keyword);

  BookDTO getById(Long id);

  BookDTO save(BookDTO category);

  BookDTO update(@NonNull BookDTO category);

  BookResponse createBook(@NonNull BookDTO bookDTO) throws Exception;

  BookResponse createLinkAndThumnail(Long id, String thumnail, String Link) throws Exception;

  List<BookResponse> getBooksByUserId(@NonNull Long id);
}
