package com.example.backend.services.bookmark;

import com.example.backend.exceptions.DataNotFoundException;
import com.example.backend.models.dtos.BookMarkDTO;
import com.example.backend.models.entities.Author;
import com.example.backend.models.entities.Book;
import com.example.backend.models.entities.BookMark;
import com.example.backend.models.responses.BookMarkResponse;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.repositories.BookMarkRepository;
import com.example.backend.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookMarkService implements IBookMarkService {

    private final ModelMapper modelMapper;
    private final BookMarkRepository bookMarkRepository;
    private final BookRepository bookRepository;

    @Override
    public List<BookMarkResponse> findByBookId(Long bookId) {
        return Optional.ofNullable(bookId)
                .map(bookMarkRepository::findByBookId)
                .orElse(List.of())
                .stream()
                .map(bookMark -> modelMapper.map(bookMark, BookMarkResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookMarkResponse save(BookMarkDTO bookMarkDTO) throws DataNotFoundException {
        Optional<Book> existingBook = Optional.ofNullable(
                bookRepository.findById(bookMarkDTO.getBookId())
                        .orElseThrow(() -> new DataNotFoundException(
                                        "Can not found book with id" + bookMarkDTO.getBookId()
                                )
                        )
        );
        BookMark bookMark = modelMapper.map(bookMarkDTO, BookMark.class);
        bookMark.setBook(existingBook.get());
        BookMark newBookMark = bookMarkRepository.save(bookMark);
        BookMarkResponse bookMarkResponse = modelMapper.map(newBookMark, BookMarkResponse.class);
        return bookMarkResponse;
    }

    @Override
    public boolean delete(Long id) throws DataNotFoundException {
        Optional<BookMark> existingBook = Optional.ofNullable(
                bookMarkRepository.findById(id)
                        .orElseThrow(() -> new DataNotFoundException(
                                        "Can not found bookmark with id" + id
                                )
                        )
        );
        if (existingBook.isPresent()) {
            bookMarkRepository.delete(existingBook.get());
            return true;
        } else {
            return false;
        }
    }
}
