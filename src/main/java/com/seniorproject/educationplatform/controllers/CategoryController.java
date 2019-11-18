package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.dto.category.AddCategoryReq;
import com.seniorproject.educationplatform.models.Category;
import com.seniorproject.educationplatform.models.Topic;
import com.seniorproject.educationplatform.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity getCategories() {
        List<Category> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity getCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @PostMapping(value = {"/category/{categoryName}", "/category/{parentId}/{categoryName}"})
    public ResponseEntity addCategory(@PathVariable String categoryName, @PathVariable(required = false) Long parentId) {
        Category category = categoryService.addCategory(categoryName, parentId);
        return ResponseEntity.ok("Category added");
    }

    @PostMapping("/categories/{categoryNames}")
    public ResponseEntity addCategories(@PathVariable List<String> categoryNames) {
        categoryService.addCategories(categoryNames);
        return ResponseEntity.ok("Categories added");
    }

    @PostMapping("/subcategories")
    public ResponseEntity addSubcategories(@RequestBody AddCategoryReq addCategoryReq) {
        categoryService.addSubcategories(addCategoryReq);
        return ResponseEntity.ok("Subcategories added");
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok("Category deleted with id: " + categoryId);
    }


    /*  Topic Controller  */
    @GetMapping("/topics")
    public ResponseEntity getTopics() {
        List<Topic> topics = categoryService.getTopics();
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity getTopicById(@PathVariable Long topicId) {
        Topic topic = categoryService.getTopicById(topicId);
        return ResponseEntity.ok(topic);
    }

    @PostMapping("/category/{categoryId}/topic/{topicName}")
    public ResponseEntity addTopic(@PathVariable Long categoryId, @PathVariable String topicName) {
        categoryService.addTopic(categoryId, topicName);
        return ResponseEntity.ok("Topic added");
    }

    @PostMapping("/category/{categoryId}/topics/{topicNames}")
    public ResponseEntity addTopics(@PathVariable Long categoryId, @PathVariable List<String> topicNames) {
        categoryService.addTopics(categoryId, topicNames);
        return ResponseEntity.ok("Topics added");
    }

    @DeleteMapping("/topic/{topicId}")
    public ResponseEntity deleteTopic(@PathVariable Long topicId) {
        categoryService.deleteTopicById(topicId);
        return ResponseEntity.ok("Topic deleted with id: " + topicId);
    }

}
