package com.example.backend.services.bookmark;

import com.example.backend.exceptions.DataNotFoundException;
import com.example.backend.models.dtos.BookMarkDTO;
import com.example.backend.models.entities.Author;
import com.example.backend.models.entities.Book;
import com.example.backend.models.entities.BookMark;
import com.example.backend.models.entities.UserEntity;
import com.example.backend.models.responses.BookMarkResponse;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.repositories.BookMarkRepository;
import com.example.backend.repositories.BookRepository;
import com.example.backend.repositories.UserRepository;
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
    private final UserRepository userRepository;
    @Override
    public List<BookMarkResponse> findByBookIdAAndUserId(Long bookId, Long userId) {
        if (bookId == null || userId == null) {
            return List.of();
        }
        return bookMarkRepository.findByBookIdAAndUserId(bookId, userId)
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
        Optional<UserEntity> existingUser = Optional.ofNullable(
                userRepository.findById(bookMarkDTO.getUserId())
                        .orElseThrow(() -> new DataNotFoundException(
                                        "Can not found user with id" + bookMarkDTO.getUserId()
                                )
                        )
        );
        BookMark bookMark = modelMapper.map(bookMarkDTO, BookMark.class);
        bookMark.setBook(existingBook.get());
        bookMark.setUserEntity(existingUser.get());
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
