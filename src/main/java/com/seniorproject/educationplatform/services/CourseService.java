package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.ESModels.ESCourse;
import com.seniorproject.educationplatform.ESModels.ESUser;
import com.seniorproject.educationplatform.ESRepos.CourseSearchRepo;
import com.seniorproject.educationplatform.dto.AddCourseDto;
import com.seniorproject.educationplatform.models.*;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import com.seniorproject.educationplatform.repositories.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
@Slf4j
public class CourseService {
    private CategoryService categoryService;
    private CourseRepo courseRepo;
    private CourseSearchRepo courseSearchRepo;
    private UserService userService;
    private UserRepo userRepo;
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public CourseService(CategoryService categoryService, CourseRepo courseRepo, CourseSearchRepo courseSearchRepo,
                UserService userService, UserRepo userRepo, ElasticsearchOperations elasticsearchOperations) {
        this.categoryService = categoryService;
        this.courseRepo = courseRepo;
        this.courseSearchRepo = courseSearchRepo;
        this.userService = userService;
        this.userRepo = userRepo;
        this.elasticsearchOperations = elasticsearchOperations;
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

        course = courseRepo.save(course);

        ESCourse esCourse = courseEntityToESCourseDocument(course);
        esCourse.setId(course.getId());
        esCourse.setAddedDate(date);
        esCourse.setLastUpdate(date);
        esCourse.setPermaLink(permaLink);
        ESUser user = userService.userEntityToESUserDocument(course.getInstructor());
        esCourse.setInstructor(user);
        courseSearchRepo.save(esCourse);

        return course;
    }

    public void removeCourse(Long id) {
        courseRepo.deleteById(id);
        courseSearchRepo.deleteById(id);
    }

    public List<ESCourse> searchCourses(String search) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(multiMatchQuery(search)
                .field("title")
                .field("subtitle")
                .fuzziness(Fuzziness.ONE)
                .prefixLength(3)
                .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
            .build();
        return elasticsearchOperations.queryForList(searchQuery, ESCourse.class);
    }

    public void courseRequirements(List<CourseRequirement> courseRequirements) {

    }

    public boolean courseTitleExists(String title) {
        return courseRepo.existsByTitleIgnoreCase(title.trim());
    }

    public Course getCourseByTitle(String courseTitle) {
        return courseRepo.findByTitleIgnoreCase(courseTitle);
    }

    public Course getCourseByPermalink (String permaLink) {
        return courseRepo.findByPermaLink(permaLink);
    }

    public List<Course> getCoursesByCategory(String categoryName) throws Exception {
        return courseRepo.findByCategoryNameIgnoreCase(categoryName.trim());
    }

    public ResponseEntity getCoursesByCategory(String categoryName, String subCategoryName) throws Exception {
        String category = categoryName.replace("-", " ");
        String subCategory = subCategoryName.replace("-", " ");
        boolean categoryExists = categoryService.categoryExists(category);
        boolean subcategoryExists = categoryService.categoryExists(subCategory);
        if (categoryExists && subcategoryExists) {
            List<Course> courses = courseRepo.findByCategoryNameIgnoreCase(subCategory.trim());
            return ResponseEntity.ok(courses);
        } else {
            return new ResponseEntity<>("Category or subcategory " + category + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity getAllCoursesByRootCategory(String categoryName) throws Exception {
        if (!categoryService.categoryExists(categoryName)) {
            return new ResponseEntity<>("Category or subcategory " + categoryName + " does not exist!", HttpStatus.NOT_FOUND);
        }
        List<Course> courses = new ArrayList<>();
        List<Category> subCategories = categoryService.getSubCategoriesByParentName(categoryName);
        for (Category subcategory: subCategories) {
            List<Course> cc = getCoursesByCategory(subcategory.getName());
            courses.addAll(cc);
        }
        return ResponseEntity.ok(courses);
    }

    public ResponseEntity getCoursesByTopic(String topicName) {
        if (!categoryService.topicExists(topicName)) {
            return new ResponseEntity<>("Topic " + topicName + " does not exist!", HttpStatus.NOT_FOUND);
        }
        List<Course> courses = courseRepo.findByTopicNameIgnoreCase(topicName.trim());
        return ResponseEntity.ok(courses);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepo.findByInstructorId(instructorId);
    }

    private String createPermaLink(String name) {
        return name.toLowerCase()
                .replaceAll("[_!@#$%^&*()-=+:.,|]", " ")
                .replaceAll(" +", " ").trim()
                .replace(" ", "-");
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

    private ESCourse courseEntityToESCourseDocument(Course course) {
        ESCourse esCourse = new ESCourse();
        esCourse.setTitle(course.getTitle());
        esCourse.setSubtitle(course.getSubtitle());
        esCourse.setDescription(course.getDescription());
        esCourse.setLevel(course.getLevel().toString());
        esCourse.setLanguage(course.getLanguage());
        esCourse.setCaption(course.getCaption());
        esCourse.setPrice(course.getPrice());
        esCourse.setImage("");
        esCourse.setCategory(course.getCategory().getName());
        esCourse.setTopic(course.getTopic().getName());
        return esCourse;
    }

}
