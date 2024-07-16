package com.example.backend.controllers.category;

import com.example.backend.models.responses.AuthorResponse;
import com.example.backend.models.responses.CategoryResponse;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.services.category.CategoryService;
import jakarta.validation.constraints.NotBlank;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/category")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;
    private String timeStamp = LocalDateTime.now().toString();


    @GetMapping("/{id}")
    public ResponseEntity<HttpResponse> getById(
            @PathVariable("id") @NotNull(message = "error.request.path.variable.id.invalid") Long id) {

        CategoryResponse category = categoryService.getById(id);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successfull")
                        .data(Map.of("Category", category))
                        .build()
        );
    }

    @GetMapping()
    public ResponseEntity<HttpResponse> getAll() {
        List<CategoryResponse> categorys = categoryService.getAll();

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(timeStamp)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successfull")
                        .data(Map.of("Category", categorys))
                        .build()
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('READER', 'ADMIN')")
    public ResponseEntity<Page<CategoryResponse>> searchByName(
            @RequestParam String name,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<CategoryResponse> categoryResponses = categoryService.getByName(name, PageRequest.of(page, size));
        return new ResponseEntity<>(categoryResponses, HttpStatus.OK);
    }
}
