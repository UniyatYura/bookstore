package mate.academy.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.CategoryRequestDto;
import mate.academy.bookstore.dto.CategoryResponseDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.CategoryRepository;
import mate.academy.bookstore.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        Category categoryFromDb = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id = " + id)
        );
        return categoryMapper.toDto(categoryFromDb);
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category saveCategory = categoryRepository.save(category);
        return categoryMapper.toDto(saveCategory);
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryDto) {
        Category categoryFromDb = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id " + id)
        );
        categoryFromDb.setName(categoryDto.getName());
        categoryFromDb.setDescription(categoryDto.getDescription());
        Category saveCategory = categoryRepository.save(categoryFromDb);
        return categoryMapper.toDto(saveCategory);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoriesId(Long id) {
        return categoryRepository.getBooksByCategoriesId(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
