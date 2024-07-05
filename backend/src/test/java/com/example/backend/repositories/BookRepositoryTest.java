package com.example.backend.repositories;

import com.example.backend.models.entities.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testFindByNameContaining() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> result = bookRepository.findByNameContaining("Sample Book", pageable);
        List<Book> books = result.getContent();

        assertThat(books).hasSize(1);
        assertThat(books).extracting(Book::getName).contains("Sample Book");
    }

}
