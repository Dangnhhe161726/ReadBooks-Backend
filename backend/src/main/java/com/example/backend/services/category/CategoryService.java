package com.example.backend.services.category;

import com.example.backend.models.entities.Category;
import com.example.backend.models.entities.Role;
import com.example.backend.repositories.CategoryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

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
}
