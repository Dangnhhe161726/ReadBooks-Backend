package com.example.backend.controllers.category;

import com.example.backend.models.responses.CategoryResponse;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.services.category.CategoryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
