package com.example.backend.services.category;

import com.example.backend.models.entities.Category;
import com.example.backend.models.responses.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
  void createCategoies(String name);

  CategoryResponse getById(Long id);

  List<CategoryResponse> getAll();

  CategoryResponse save(CategoryResponse product);

  void delete(Long id);

  Page<CategoryResponse> getByName(String name, Pageable pageable);
}
