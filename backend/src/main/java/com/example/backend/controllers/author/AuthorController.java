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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/author")
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
    List<AuthorResponse> authors = authorService.getAll();

    return ResponseEntity.ok().body(
        HttpResponse.builder()
            .timeStamp(timeStamp)
            .status(HttpStatus.OK)
            .statusCode(HttpStatus.OK.value())
            .message("Login successfull")
            .data(Map.of("Authors", authors))
            .build()
    );
  }
}
