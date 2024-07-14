package com.example.backend.controllers.book;

import com.example.backend.models.dtos.BookDTO;
import com.example.backend.models.responses.AuthorResponse;
import com.example.backend.models.responses.BookDetailResponse;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.services.book.IBookService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/book")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;
    private String timeStamp = LocalDateTime.now().toString();

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('READER', 'ADMIN')")
    public ResponseEntity<Page<BookResponse>> searchByName(
            @RequestParam String name,
            @RequestParam int page,
            @RequestParam int size) {
        Page<BookResponse> bookResponsePage = bookService.getByName(name, PageRequest.of(page, size));
        return new ResponseEntity<>(bookResponsePage, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> createBook(
            @RequestBody BookDTO bookDTO) {
        try {
            BookResponse newBook = bookService.createBook(bookDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("Create book success")
                            .data(Map.of("Book", newBook))
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .timeStamp(timeStamp)
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<HttpResponse> getBooksByUserId(
            @PathVariable("id") @NotNull(message = "error.request.path.variable.id.invalid") Long id) {
        List<BookResponse> books = bookService.getBooksByUserId(id);

    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(timeStamp)
            .status(HttpStatus.OK)
            .statusCode(HttpStatus.OK.value())
            .message("get user by id success")
            .data(Map.of("Books", books))
            .build()
    );
  }
  @GetMapping("/{id}")
  public ResponseEntity<HttpResponse> getBookById(
      @PathVariable("id") @NotNull(message = "error.request.path.variable.id.invalid") Long id) {
   BookDetailResponse book = bookService.getById(id);
    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(timeStamp)
            .status(HttpStatus.OK)
            .statusCode(HttpStatus.OK.value())
            .message("Login successfull")
            .data(Map.of("Book", book))
            .build()
    );
  }
  @GetMapping("/trending")
  protected ResponseEntity<HttpResponse> getTrendingBooks(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "16") int size,
      @RequestParam(required = false, defaultValue = "favorites") String sortBy,
      @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
      @RequestParam(required = false, defaultValue = "") String keyword
  ) {
    Page<BookResponse> productPage =
        bookService.getByPaging(page, size, sortBy, sortDirection, keyword);
    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(timeStamp)
            .status(HttpStatus.OK)
            .statusCode(HttpStatus.OK.value())
            .message("Login successfull")
            .data(Map.of("productPage", productPage))
            .build()
    );
  }
  @GetMapping("/new")
  protected ResponseEntity<HttpResponse> getNewBooks(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "16") int size,
      @RequestParam(required = false, defaultValue = "createTime") String sortBy,
      @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
      @RequestParam(required = false, defaultValue = "") String keyword
  ) {
    Page<BookResponse> productPage =
        bookService.getByPaging(page, size, sortBy, sortDirection, keyword);
    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(timeStamp)
            .status(HttpStatus.OK)
            .statusCode(HttpStatus.OK.value())
            .message("Login successfull")
            .data(Map.of("productPage", productPage))
            .build()
    );
  }
}
