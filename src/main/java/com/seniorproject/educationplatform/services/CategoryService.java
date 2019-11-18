package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.category.AddCategoryReq;
import com.seniorproject.educationplatform.models.Category;
import com.seniorproject.educationplatform.models.Topic;
import com.seniorproject.educationplatform.repositories.CategoryRepo;
import com.seniorproject.educationplatform.repositories.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private CategoryRepo categoryRepo;
    private TopicRepo topicRepo;

    @Autowired
    public CategoryService(CategoryRepo categoryRepo, TopicRepo topicRepo) {
        this.categoryRepo = categoryRepo;
        this.topicRepo = topicRepo;
    }

    public Category addCategory(String categoryName, Long parentId) {
        Category category;
        if (parentId != null) {
            Category parentCategory = getCategoryById(parentId);
            category = new Category(categoryName, parentCategory);
        } else {
            category = new Category(categoryName);
        }
        return categoryRepo.save(category);
    }

    public void addCategories(List<String> categoryNames) {
        List<Category> categories = categoryNames.stream().map(Category::new).collect(Collectors.toList());
        System.out.println("LOG: CategoryService, categories: " + categories);
        categoryRepo.saveAll(categories);
    }

    public void addSubcategories(AddCategoryReq addCategoryReq) {
        Long parentId = addCategoryReq.getParentId();
        Category parentCategory = getCategoryById(parentId);
        List<String> subcategoryNames = addCategoryReq.getSubcategories();
        System.out.println("LOG: CategoryService, addSubcategories, subcategoryNames: " + subcategoryNames);
        List<Category> subcategories = subcategoryNames.stream().map(c -> new Category(c, parentCategory)).collect(Collectors.toList());
        System.out.println("LOG: CategoryService, addSubcategories, subcategories: " + subcategories);
        categoryRepo.saveAll(subcategories);
    }

    public void deleteCategoryById(Long categoryId) {
        categoryRepo.deleteById(categoryId);
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
    public void addTopic(Long categoryId, String topicName) {
        Topic topic = new Topic();
        topic.setName(topicName);
        Category subcategory = getCategoryById(categoryId);
        topic.setSubcategory(subcategory);
        topicRepo.save(topic);
    }

    public void addTopics(Long categoryId, List<String> topicNames) {
        Category category = getCategoryById(categoryId);
        List<Topic> topics = topicNames.stream().map(name -> new Topic(name, category)).collect(Collectors.toList());
        System.out.println("LOG: CategoryService, topics: " + topics);
        topicRepo.saveAll(topics);
    }

    public void deleteTopicById(Long topicId) {
        topicRepo.deleteById(topicId);
    }

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
