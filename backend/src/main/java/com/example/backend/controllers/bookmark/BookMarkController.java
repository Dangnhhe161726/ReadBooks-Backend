package com.example.backend.controllers.bookmark;

import com.example.backend.models.dtos.BookMarkDTO;
import com.example.backend.models.responses.BookMarkResponse;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.services.bookmark.IBookMarkService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/bookmark")
@RequiredArgsConstructor
public class BookMarkController {
    private String timeStamp = LocalDateTime.now().toString();

    private final IBookMarkService bookMarkService;

    @GetMapping("/book/{bookId}/{userId}")
    @PreAuthorize("hasAnyAuthority('READER', 'ADMIN')")
    public ResponseEntity<HttpResponse> getByBookId(
            @PathVariable("bookId") @NotNull(message = "error.request.path.variable.id.invalid") Long bookId,
            @PathVariable("userId") @NotNull(message = "error.request.path.variable.id.invalid") Long userId
    ) {
        try {
            List<BookMarkResponse> bookMarkResponses = bookMarkService.findByBookIdAAndUserId(bookId,userId);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("Get bookmark success")
                            .data(Map.of("bookMarks", bookMarkResponses))
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

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('READER', 'ADMIN')")
    public ResponseEntity<HttpResponse> create(
            @RequestBody BookMarkDTO bookMarkDTO
    ) {
        try {
            BookMarkResponse newBookMark = bookMarkService.save(bookMarkDTO);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(timeStamp)
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("Create bookmark success")
                            .data(Map.of("bookMark", newBookMark))
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

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('READER', 'ADMIN')")
    public ResponseEntity<HttpResponse> delete(
            @PathVariable("id") @NotNull(message = "error.request.path.variable.id.invalid") Long id
    ) {
        try {
            boolean checkDelete = bookMarkService.delete(id);
            if (checkDelete) {
                return ResponseEntity.ok().body(
                        HttpResponse.builder()
                                .timeStamp(timeStamp)
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .message("Delete bookmark success")
                                .build()
                );
            } else {
                throw new Exception("Delete failed");
            }
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
