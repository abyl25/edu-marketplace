package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.dto.AddCourseDto;
import com.seniorproject.educationplatform.models.*;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import com.seniorproject.educationplatform.repositories.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CourseService {
    private CategoryService categoryService;
    private CourseRepo courseRepo;
    private UserRepo userRepo;

    @Autowired
    public CourseService(CategoryService categoryService, CourseRepo courseRepo, UserRepo userRepo) {
        this.categoryService = categoryService;
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
    }

    public List<Course> getCourses() {
        return courseRepo.findAll();
    }

    public Optional<Course> getCourseById(Long courseId) {
        return courseRepo.findById(courseId);
    }

    public Course addCourse(AddCourseDto addCourseDto) {
        Course course = courseDtoToEntity(addCourseDto);
        Date date = new Date(System.currentTimeMillis());
        course.setAddedDate(date);
        course.setLastUpdate(date);

        String permaLink = createPermaLink(course.getTitle());
        course.setPermaLink(permaLink);

        courseRepo.save(course);
        return course;
    }

    public Course getCourseByTitle(String courseTitle) {
        return courseRepo.findByTitleIgnoreCase(courseTitle);
    }

    public Course getCourseByPermalink (String permaLink) {
        return courseRepo.findByPermaLink(permaLink);
    }

    public List<Course> getCoursesByCategory(String categoryName) throws Exception {
        boolean categoryExists = categoryService.categoryExists(categoryName);
        if (categoryExists) {
            return courseRepo.findByCategoryName(categoryName);
        } else {
            throw new Exception("Category or subcategory does not exist!");
        }
    }

    public List<Course> getCoursesByCategory(String categoryName, String subCategoryName) throws Exception {
        boolean categoryExists = categoryService.categoryExists(categoryName);
        boolean subcategoryExists = categoryService.categoryExists(subCategoryName);
        if (categoryExists && subcategoryExists) {
            return courseRepo.findByCategoryName(categoryName);
        } else {
            throw new Exception("Category or subcategory does not exist!");
        }
    }

    public List<Course> getAllCoursesByRootCategory(String categoryName) throws Exception {
        List<Course> courses = new ArrayList<>();
        List<Category> subCategories = categoryService.getSubCategoriesByParentName(categoryName);
        for (Category subcategory: subCategories) {
            List<Course> cc = getCoursesByCategory(subcategory.getName());
            courses.addAll(cc);
        }
        return courses;
    }

    public List<Course> getCoursesByTopic(String topicName) {
        return courseRepo.findByTopicNameIgnoreCase(topicName);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepo.findByInstructorId(instructorId);
    }

    private String createPermaLink(String name) {
        return name.toLowerCase().replace("-", " ").replaceAll(" +", " ").replace(" ", "-");
    }

    public List<Course> getPopularCourses() {
        return null;
    }

    public List<Course> getTrendingCourses() {
        return null;
    }

    public List<Course> getBeginnerFavoriteCourses() {
        return null;
    }

    public List<Topic> getPopularTopics() {
        return null;
    }

    public List<User> getPopularInstructors() {
        return null;
    }

    private Course courseDtoToEntity(AddCourseDto addCourseDto) {
        log.info("LOG: CourseDto: " + addCourseDto);
        Course course = new Course();
        course.setTitle(addCourseDto.getTitle().trim());
        course.setSubtitle(addCourseDto.getSubtitle().trim());
        User instructor = userRepo.findById(addCourseDto.getInstructorId()).orElse(null);
        course.setInstructor(instructor);
        course.setDescription(addCourseDto.getDescription().trim());
        Level level = Level.valueOf(addCourseDto.getLevel().trim());
        course.setLevel(level);
        course.setLanguage(addCourseDto.getLanguage().trim());
        course.setCaption(addCourseDto.getCaption());
        course.setPrice(addCourseDto.getPrice());
        Category category = categoryService.getCategoryByName(addCourseDto.getCategory().trim());
        course.setCategory(category);
        Topic topic = categoryService.getTopicByName(addCourseDto.getTopic().trim());
        course.setTopic(topic);
        course.setImage("");
        return course;
    }

}
