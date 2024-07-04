package com.example.backend.services.author;

import com.example.backend.models.responses.AuthorResponse;
import com.example.backend.models.responses.CategoryResponse;
import java.util.List;

public interface IAuthorService {

    void createAuthors(String name);

  AuthorResponse getById(Long id);

  List<AuthorResponse> getAll();

  AuthorResponse save(AuthorResponse product);

  void delete(Long id);
}
