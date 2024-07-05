package com.example.backend.services.category;

import com.example.backend.models.entities.Category;
import com.example.backend.models.responses.CategoryResponse;
import java.util.List;

public interface ICategoryService {
  void createCategoies(String name);

  CategoryResponse getById(Long id);

  List<CategoryResponse> getAll();

  CategoryResponse save(CategoryResponse product);

  void delete(Long id);
}
