package com.example.backend.controllers.book;

import com.example.backend.models.dtos.BookDTO;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.services.amazons3.IAmazonS3Service;
import com.example.backend.services.book.IBookService;
import com.example.backend.services.cloudinary.ICloudinaryService;
import lombok.RequiredArgsConstructor;
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
    private final ICloudinaryService cloudinaryService;
    private final IAmazonS3Service amazonS3Service;
    private String timeStamp = LocalDateTime.now().toString();

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
