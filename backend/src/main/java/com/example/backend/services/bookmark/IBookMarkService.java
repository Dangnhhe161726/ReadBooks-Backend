package com.example.backend.services.bookmark;

import com.amazonaws.services.mq.model.NotFoundException;
import com.example.backend.exceptions.DataNotFoundException;
import com.example.backend.models.dtos.BookMarkDTO;
import com.example.backend.models.responses.BookMarkResponse;

import java.util.List;

public interface IBookMarkService {

    List<BookMarkResponse> findByBookId(Long bookId);
    BookMarkResponse save(BookMarkDTO bookMarkDTO) throws DataNotFoundException;

    boolean delete(Long id) throws DataNotFoundException;
}
