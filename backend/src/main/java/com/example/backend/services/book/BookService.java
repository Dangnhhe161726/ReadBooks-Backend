package com.example.backend.services.book;

import com.example.backend.exceptions.DataNotFoundException;
import com.example.backend.models.dtos.BookDTO;
import com.example.backend.models.entities.Author;
import com.example.backend.models.entities.Book;
import com.example.backend.models.entities.Category;
import com.example.backend.models.responses.AuthorResponse;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.models.responses.CategoryResponse;
import com.example.backend.repositories.AuthorRepository;
import com.example.backend.repositories.BookRepository;
import com.example.backend.repositories.CategoryRepository;
import com.example.backend.repositories.FeedbackRepository;
import com.example.backend.repositories.NotificationRepository;
import com.example.backend.repositories.UserBookRepository;
import com.example.backend.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
public class BookService implements IBookService {

  private final BookRepository bookRepository;
  private final ModelMapper modelMapper;
  private final AuthorRepository authorRepository;
  private final UserBookRepository userBookRepository;
  private final UserRepository userRepository;
  private final FeedbackRepository feedbackRepository;
  private final NotificationRepository notificationRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public BookResponse getById(Long id) {
    return Optional.ofNullable(id)
        .flatMap(
            e -> bookRepository.findById(e).map(book -> modelMapper.map(book, BookResponse.class)))
        .orElse(null);

  }

  @Override
  public BookDTO save(BookDTO category) {
    return null;
  }

  @Override
  public BookDTO update(@NonNull BookDTO category) {
    return null;
  }

  @Override
  public Page<BookResponse> getByPaging(int pageNo, int pageSize, String sortBy,
      String sortDirection,
      String keyword) {
    Pageable pageable =
        PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
    return bookRepository
        .findAll(keyword, pageable)
        .map(book -> modelMapper.map(book, BookResponse.class));
  }

  @Override
  public BookResponse createBook(@NonNull BookDTO bookDTO) throws DataNotFoundException {
    Optional<Author> existingAuthor = Optional.ofNullable(
        authorRepository.findById(bookDTO.getAuthorId())
            .orElseThrow(() -> new DataNotFoundException(
                    "Can not found author with id" + bookDTO.getAuthorId()
                )
            )
    );
    List<Category> existingListCategory = new ArrayList<>();
    Optional<Category> existingCategory = null;
    for (Long cateId : bookDTO.getCategories()) {
      existingCategory = Optional.ofNullable(
          categoryRepository.findById(cateId)
              .orElseThrow(() -> new DataNotFoundException(
                      "Can not found category with id" + cateId
                  )
              )
      );
      existingListCategory.add(existingCategory.get());
    }
    //
    Book book = modelMapper.map(bookDTO, Book.class);
    book.setAuthor(existingAuthor.get());
    book.setCategories(existingListCategory);
    Book newBook = bookRepository.save(book);
    //
    BookResponse bookResponse = modelMapper.map(newBook, BookResponse.class);
    bookResponse.setAuthor(modelMapper.map(newBook.getAuthor(), AuthorResponse.class));
    List<CategoryResponse> categoryResponses = new ArrayList<>();
    CategoryResponse categoryResponse = null;
    for (Category item : newBook.getCategories()) {
      categoryResponse = modelMapper.map(item, CategoryResponse.class);
      categoryResponses.add(categoryResponse);
    }
    bookResponse.setCategories(categoryResponses);
    return bookResponse;
  }

//    @Override
//    public BookResponse createLinkAndThumnail(Long id, String thumnail, String link) throws Exception {
//        Optional<Book> existingBook = Optional.ofNullable(
//                bookRepository.findById(id)
//                        .orElseThrow(() -> new DataNotFoundException(
//                                        "Can not found book with id" + id
//                                )
//                        )
//        );
//        existingBook.get().setThumbnail(thumnail);
//        existingBook.get().setLink(link);
//        bookRepository.save(existingBook.get());
//        BookResponse bookResponse = modelMapper.map(existingBook.get(), BookResponse.class);
//        bookResponse.setAuthor(modelMapper.map(existingBook.get().getAuthor(), AuthorResponse.class));
//        List<CategoryResponse> categoryResponses = new ArrayList<>();
//        CategoryResponse categoryResponse = null;
//        for (Category item : existingBook.get().getCategories()) {
//            categoryResponse = modelMapper.map(item, CategoryResponse.class);
//            categoryResponses.add(categoryResponse);
//        }
//        bookResponse.setCategories(categoryResponses);
//        return bookResponse;
//    }

//    //
//    Book book = modelMapper.map(bookDTO, Book.class);
//    book.setAuthor(existingAuthor.get());
//    book.setCategories(existingListCategory);
//    Book newBook = bookRepository.save(book);
//    //
//    BookResponse bookResponse = modelMapper.map(newBook, BookResponse.class);
//    bookResponse.setAuthor(modelMapper.map(newBook.getAuthor(),AuthorResponse .class));
//    List<CategoryResponse> categoryResponses = new ArrayList<>();
//    CategoryResponse categoryResponse = null;
//    for(
//    Category item :newBook.getCategories())
//
//    {
//        categoryResponse = modelMapper.map(item, CategoryResponse.class);
//        categoryResponses.add(categoryResponse);
//    }
//    bookResponse.setCategorys(categoryResponses);
//    return bookResponse;
//}

  @Override
  public BookResponse createLinkAndThumnail(Long id, String thumnail, String link)
      throws Exception {
    Optional<Book> existingBook = Optional.ofNullable(
        bookRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException(
                    "Can not found author with id" + id
                )
            )
    );
    existingBook.get().setThumbnail(thumnail);
    existingBook.get().setLink(link);
    bookRepository.save(existingBook.get());
    BookResponse bookResponse = modelMapper.map(existingBook.get(), BookResponse.class);
    bookResponse.setAuthor(modelMapper.map(existingBook.get().getAuthor(), AuthorResponse.class));
    List<CategoryResponse> categoryResponses = new ArrayList<>();
    CategoryResponse categoryResponse = null;
    for (Category item : existingBook.get().getCategories()) {
      categoryResponse = modelMapper.map(item, CategoryResponse.class);
      categoryResponses.add(categoryResponse);
    }
    bookResponse.setCategories(categoryResponses);
    return bookResponse;
  }

  @Override
  public List<BookResponse> getBooksByUserId(@NonNull Long id) {
    return Optional.ofNullable(id)
        .map(userBookRepository::findBooksByUserId)
        .orElse(List.of())
        .stream()
        .map(book -> modelMapper.map(book, BookResponse.class))
        .collect(Collectors.toList());
  }

  @Override
  public Page<BookResponse> getByName(String name, Pageable pageable) {
    Page<Book> books = bookRepository.findByNameContaining(name, pageable);
    return books.map(this::convertToBookResponse);
  }

  private BookResponse convertToBookResponse(Book book) {
    return modelMapper.map(book, BookResponse.class);
  }
}


