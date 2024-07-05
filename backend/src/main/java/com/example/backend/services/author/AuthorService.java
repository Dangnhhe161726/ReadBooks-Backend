package com.example.backend.services.author;

import com.example.backend.models.entities.Author;
import com.example.backend.models.entities.Category;
import com.example.backend.models.responses.AuthorResponse;
import com.example.backend.models.responses.CategoryResponse;
import com.example.backend.repositories.AuthorRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService implements IAuthorService {
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createAuthors(String name) {
        if (!authorRepository.existsByName(name)) {
            Author author = new Author();
            author.setName(name);
            authorRepository.save(author);
        }
    }

  @Override
  public AuthorResponse getById(Long id) {
    return Optional.ofNullable(id)
        .flatMap(
            e ->
                authorRepository.findById(e).map(c -> modelMapper.map(c, AuthorResponse.class)))
        .orElse(null);  }

  @Override
  public List<AuthorResponse> getAll() {
    List<Author> entities = authorRepository.findAll();
    return entities.stream()
        .map(e -> e != null ? modelMapper.map(e, AuthorResponse.class) : null)
        .toList();
  }

  @Override
  public AuthorResponse save(AuthorResponse product) {
    return null;
  }

  @Override
  public void delete(Long id) {

  }

  @PostConstruct
    @Transactional
    public void initData() {
        createAuthors("Nguyễn Du");
        createAuthors("Nguyễn Nhật Ánh");
        createAuthors("Nguyễn Bỉnh Khiêm");
    }
}
