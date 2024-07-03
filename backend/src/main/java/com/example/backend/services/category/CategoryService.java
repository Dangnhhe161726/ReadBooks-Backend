package com.example.backend.services.category;

import com.example.backend.models.entities.Category;
import com.example.backend.models.responses.CategoryResponse;
import com.example.backend.repositories.CategoryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
  private final CategoryRepository categoryRepository;
  private final ModelMapper modelMapper;

  @PostConstruct
  @Transactional
  public void initData() {
    createCategoies("Chiến tranh");
    createCategoies("Dân gian");
    createCategoies("Hiện thực");
    createCategoies("Khoa học");
    createCategoies("Lãng mạn");
    createCategoies("Phiêu lưu");
    createCategoies("Trinh thám");
    createCategoies("Truyện ngắn - Tản văn");
    createCategoies("Chính Luận");
    createCategoies("Giả Tưởng");
    createCategoies("Hồi Ký - Tuỳ Bút");
    createCategoies("Kinh Dị");
    createCategoies("Lịch Sử");
    createCategoies("Thơ");
    createCategoies("Truyện cười");
    createCategoies("Tâm lý");
  }

  @Override
  public void createCategoies(String name) {
    if (!categoryRepository.existsByName(name)) {
      Category category = new Category();
      category.setName(name);
      categoryRepository.save(category);
    }
  }

  @Override
  public CategoryResponse getById(Long id) {
    return Optional.ofNullable(id)
        .flatMap(
            e ->
                categoryRepository.findById(e).map(c -> modelMapper.map(c, CategoryResponse.class)))
        .orElse(null);
  }

  @Override
  public List<CategoryResponse> getAll() {
    List<Category> entities = categoryRepository.findAll();
    return entities.stream()
        .map(e -> e != null ? modelMapper.map(e, CategoryResponse.class) : null)
        .toList();
  }

  @Override
  public CategoryResponse save(CategoryResponse product) {
    return null;
  }

  @Override
  public void delete(Long id) {}
}
