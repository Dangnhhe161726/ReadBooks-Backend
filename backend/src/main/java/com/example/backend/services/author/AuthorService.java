package com.example.backend.services.author;

import com.example.backend.models.entities.Author;
import com.example.backend.repositories.AuthorRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService implements IAuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public void createAuthors(String name) {
        if (!authorRepository.existsByName(name)) {
            Author author = new Author();
            author.setName(name);
            authorRepository.save(author);
        }
    }

    @PostConstruct
    @Transactional
    public void initData() {
        createAuthors("Nguyễn Du");
        createAuthors("Nguyễn Nhật Ánh");
        createAuthors("Nguyễn Bỉnh Khiêm");
    }
}
