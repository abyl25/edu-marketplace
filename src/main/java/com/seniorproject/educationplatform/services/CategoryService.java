package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.models.Category;
import com.seniorproject.educationplatform.models.Topic;
import com.seniorproject.educationplatform.repositories.CategoryRepo;
import com.seniorproject.educationplatform.repositories.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepo categoryRepo;
    private TopicRepo topicRepo;

    @Autowired
    public CategoryService(CategoryRepo categoryRepo, TopicRepo topicRepo) {
        this.categoryRepo = categoryRepo;
        this.topicRepo = topicRepo;
    }

    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepo.findByNameIgnoreCase(categoryName.trim());
    }

    public boolean categoryExists(String categoryName) {
        return categoryRepo.existsByNameIgnoreCase(categoryName.trim());
    }

    public List<Category> getCategories() {
        return categoryRepo.findByParentId(null);
    }

    public List<Category> getSubCategories(Long id) {
        return categoryRepo.findByParentId(id);
    }

    public List<Category> getSubCategoriesByParentName(String parentName) {
        Category category = categoryRepo.findByNameIgnoreCase(parentName.trim());
        return categoryRepo.findByParentId(category.getId());
    }


    /*  Topics Service  */
    public Topic getTopicById(Long id) {
        return topicRepo.findById(id).orElse(null);
    }

    public boolean topicExists(String topicName) {
        return topicRepo.existsByNameIgnoreCase(topicName.trim());
    }

    public Topic getTopicByName(String topicName) {
        return topicRepo.findByNameIgnoreCase(topicName);
    }

    public List<Topic> getTopics() {
        return topicRepo.findAll();
    }

    public List<Topic> getTopicsBySubcategoryId(Long categoryId) {
        return topicRepo.findBySubcategoryId(categoryId);
    }

    public List<Topic> getTopicsBySubcategoryName(String categoryName) {
        return topicRepo.findBySubcategoryNameIgnoreCase(categoryName.trim());
    }

}
