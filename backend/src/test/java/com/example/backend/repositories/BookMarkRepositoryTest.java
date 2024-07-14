package com.example.backend.repositories;

import com.example.backend.models.entities.Book;
import com.example.backend.models.entities.BookMark;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookMarkRepositoryTest {

    @Autowired
    private BookMarkRepository bookMarkRepository;

    @Test
    public void testFindBookMarksByBookId() {
        // Replace with actual book ID you want to test with
        Long bookId = 2L;
        Long userId = 1L;
        // Call the service or repository method
        List<BookMark> foundBookMarks = bookMarkRepository.findByBookIdAAndUserId(bookId, userId);

        // Assert the results
        assertThat(foundBookMarks).hasSize(1);
        assertThat(foundBookMarks).extracting(BookMark::getName).contains("bookMark 1");
    }
}
