package com.example.backend.services.book;

import com.example.backend.models.dtos.BookDTO;
import com.example.backend.models.entities.UserEntity;
import com.example.backend.repositories.BookRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements  BookService{
private final BookRepository bookRepository;
  private final ModelMapper modelMapper;

  @Override
  public Page<BookDTO> getByPaging(int pageNo, int pageSize, String sortBy, String sortDirection,
      String keyword) {
    Pageable pageable =
        PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
    return bookRepository
        .findAll( keyword, pageable)
        .map(book -> modelMapper.map(book, BookDTO.class));
  }

  @Override
  public BookDTO getById(Long id) {
    return null;
  }

  @Override
  public BookDTO save(BookDTO category) {
    return null;
  }

  @Override
  public BookDTO update(@NonNull BookDTO category) {
    return null;
  }
}
