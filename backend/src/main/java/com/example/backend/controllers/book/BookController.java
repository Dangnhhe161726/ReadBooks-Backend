package com.example.backend.controllers.book;

import com.example.backend.models.dtos.BookDTO;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.services.book.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/book")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;
    private String timeStamp = LocalDateTime.now().toString();
    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchByName(
            @RequestParam String name,
            @RequestParam int page,
            @RequestParam int size
    ){
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
                            .message("Login successfull")
                            .data(Map.of("Book", newBook))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .timeStamp(timeStamp)
                            .message(e.getMessage())
                            .build()
            );
        }
    }


}
