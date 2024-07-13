package com.example.backend.controllers.author;

import com.example.backend.models.responses.AuthorResponse;
import com.example.backend.models.responses.CategoryResponse;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.services.author.AuthorService;
import com.example.backend.services.category.CategoryService;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/ ")
@RequiredArgsConstructor

public class AuthorController {
    private final AuthorService authorService;
    private String timeStamp = LocalDateTime.now().toString();


    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getById(
            @PathVariable("id") @NotNull(message = "error.request.path.variable.id.invalid") Long id) {

        AuthorResponse authorResponse = authorService.getById(id);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successfull")
                        .data(Map.of("Author", authorResponse))
                        .build()
        );
    }

    @GetMapping()
    public ResponseEntity<HttpResponse> getAll() {
        List<AuthorResponse> categorys = authorService.getAll();

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successfull")
                        .data(Map.of("Authors", categorys))
                        .build()
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('READER', 'ADMIN')")
    public ResponseEntity<Page<AuthorResponse>> searchByName(
            @RequestParam String name,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<AuthorResponse> authorResponses = authorService.getByName(name, PageRequest.of(page, size));
        return new ResponseEntity<>(authorResponses, HttpStatus.OK);
    }
}
