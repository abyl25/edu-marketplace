package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.ESModels.ESCourse;
import com.seniorproject.educationplatform.ESModels.ESUser;
import com.seniorproject.educationplatform.ESRepos.CourseSearchRepo;
import com.seniorproject.educationplatform.dto.course.*;
import com.seniorproject.educationplatform.models.*;
import com.seniorproject.educationplatform.repositories.CourseGoalRepo;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import com.seniorproject.educationplatform.repositories.CourseRequirementRepo;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Slf4j
@Service
public class CourseService {
    private CategoryService categoryService;
    private CourseRepo courseRepo;
    private CourseRequirementRepo courseRequirementRepo;
    private CourseGoalRepo courseGoalRepo;
    private CourseSearchRepo courseSearchRepo;
    private UserService userService;
    private UserRepo userRepo;
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public CourseService(CategoryService categoryService, CourseRepo courseRepo, CourseRequirementRepo courseRequirementRepo,
             CourseGoalRepo courseGoalRepo, CourseSearchRepo courseSearchRepo, UserService userService, UserRepo userRepo,
             ElasticsearchOperations elasticsearchOperations) {
        this.categoryService = categoryService;
        this.courseRepo = courseRepo;
        this.courseRequirementRepo = courseRequirementRepo;
        this.courseGoalRepo = courseGoalRepo;
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

    public Course createCourse(CreateCourseReq createCourseReq) {
        Course course = createCourseDtoToEntity(createCourseReq);
        Date date = new Date(System.currentTimeMillis());
        course.setAddedDate(date);
        course.setLastUpdate(date);

        String permaLink = createPermaLink(course.getTitle());
        course.setPermaLink(permaLink);

        return courseRepo.save(course);
    }

    public Course addCourse(AddCourseInfoDto addCourseInfoDto) {
        Course course = courseDtoToEntity(addCourseInfoDto);
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
//        courseSearchRepo.save(esCourse);

        return course;
    }

    public Course addCourseInfo(AddCourseInfoDto addCourseInfoDto) {
        Course course = courseRepo.findById(addCourseInfoDto.getCourseId()).orElse(null);
        course.setTitle(addCourseInfoDto.getTitle());
        course.setSubtitle(addCourseInfoDto.getSubtitle());
        course.setDescription(addCourseInfoDto.getDescription());
        course.setLanguage(addCourseInfoDto.getLanguage());
        Level level = Level.valueOf(addCourseInfoDto.getLevel().trim());
        course.setLevel(level);
        Category category = categoryService.getCategoryByName(addCourseInfoDto.getCategory().trim());
        course.setCategory(category);
        Topic topic = categoryService.getTopicByName(addCourseInfoDto.getTopic().trim());
        course.setTopic(topic);
        course.setPrice(addCourseInfoDto.getPrice());
        return course;
    }

    public void addCourseReqs(AddCourseReq addCourseReq) {
        List<CourseRequirement> courseRequirements = new ArrayList<>();
        for (String req: addCourseReq.getReqs()) {
            CourseRequirement courseRequirement = courseReqDtoToEntity(addCourseReq);
            courseRequirement.setName(req);
            courseRequirements.add(courseRequirement);
        }
        courseRequirementRepo.saveAll(courseRequirements);
        System.out.println("Course reqs added");
    }

    public void addCourseGoals(AddCourseGoal addCourseGoal) {
        List<CourseGoal> courseGoals = new ArrayList<>();
        for (String goal: addCourseGoal.getGoals()) {
            CourseGoal courseGoal = courseGoalDtoToEntity(addCourseGoal);
            courseGoal.setName(goal);
            courseGoals.add(courseGoal);
        }
        courseGoalRepo.saveAll(courseGoals);
        System.out.println("Course goals added");
    }

    public ResponseEntity addCourseTarget(AddCourseTarget addCourseTarget) {
        AddCourseReq addCourseReq = new AddCourseReq(addCourseTarget.getCourseId(), addCourseTarget.getReqs());
        addCourseReqs(addCourseReq);
        AddCourseGoal addCourseGoal = new AddCourseGoal(addCourseTarget.getCourseId(), addCourseTarget.getGoals());
        addCourseGoals(addCourseGoal);
        return ResponseEntity.ok("course targets added");
    }

    public void removeCourse(Long id) {
        courseRepo.deleteById(id);
//        courseSearchRepo.deleteById(id);
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
            return new ResponseEntity<>("Category or subcategory " + "'" + category + "/" + subCategory + "'" + " does not exist!", HttpStatus.NOT_FOUND);
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


    // DTO TO ENTITY and VICE VERSA MAPPERS
    private Course createCourseDtoToEntity(CreateCourseReq createCourseReq) {
        Course course = new Course();
        course.setTitle(createCourseReq.getTitle().trim());
        Category category = categoryService.getCategoryById(createCourseReq.getCategoryId());
        course.setCategory(category);
        User instructor = userRepo.findById(createCourseReq.getInstructorId()).orElse(null);
        course.setInstructor(instructor);
        return course;
    }

    private Course courseDtoToEntity(AddCourseInfoDto addCourseInfoDto) {
        log.info("LOG: CourseDto: " + addCourseInfoDto);
        Course course = new Course();
        course.setTitle(addCourseInfoDto.getTitle().trim());
        course.setSubtitle(addCourseInfoDto.getSubtitle().trim());
        User instructor = userRepo.findById(addCourseInfoDto.getInstructorId()).orElse(null);
        course.setInstructor(instructor);
        course.setDescription(addCourseInfoDto.getDescription().trim());
        Level level = Level.valueOf(addCourseInfoDto.getLevel().trim());
        course.setLevel(level);
        course.setLanguage(addCourseInfoDto.getLanguage().trim());
        course.setCaption(addCourseInfoDto.getCaption());
        course.setPrice(addCourseInfoDto.getPrice());
        Category category = categoryService.getCategoryByName(addCourseInfoDto.getCategory().trim());
        course.setCategory(category);
        Topic topic = categoryService.getTopicByName(addCourseInfoDto.getTopic().trim());
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

    private CourseRequirement courseReqDtoToEntity(AddCourseReq courseReq) {
        CourseRequirement courseRequirement = new CourseRequirement();
        Course course = courseRepo.findById(courseReq.getCourseId()).orElse(null);
        courseRequirement.setCourse(course);
        return courseRequirement;
    }

    private CourseGoal courseGoalDtoToEntity(AddCourseGoal addCourseGoal) {
        CourseGoal courseGoal = new CourseGoal();
        Course course = courseRepo.findById(addCourseGoal.getCourseId()).orElse(null);
        courseGoal.setCourse(course);
        return courseGoal;
    }

}
