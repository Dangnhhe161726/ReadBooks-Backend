package com.example.backend.services.author;

import com.example.backend.models.responses.AuthorResponse;
import com.example.backend.models.responses.CategoryResponse;
import com.example.backend.models.responses.UserTokenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAuthorService {

    void createAuthors(String name);

    AuthorResponse getById(Long id);

    List<AuthorResponse> getAll();

    AuthorResponse save(AuthorResponse product);

    void delete(Long id);

   Page<AuthorResponse>  getByName(String name, Pageable pageable);


}
