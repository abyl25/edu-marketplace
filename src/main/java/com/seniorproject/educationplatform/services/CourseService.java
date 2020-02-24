package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.ESModels.ESCourse;
import com.seniorproject.educationplatform.ESModels.ESUser;
import com.seniorproject.educationplatform.ESRepos.CourseSearchRepo;
import com.seniorproject.educationplatform.dto.course.*;
import com.seniorproject.educationplatform.dto.course.resp.AddLectureRespDto;
import com.seniorproject.educationplatform.dto.course.resp.AddSectionRespDto;
import com.seniorproject.educationplatform.dto.course.resp.CoursesRespDto;
import com.seniorproject.educationplatform.exception.CustomException;
import com.seniorproject.educationplatform.models.*;
import com.seniorproject.educationplatform.repositories.*;
import com.seniorproject.educationplatform.security.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Slf4j
@Service
@Transactional
public class CourseService {
    private CourseOrderService courseOrderService;
    private CategoryService categoryService;
    private CourseRepo courseRepo;
    private CourseRequirementRepo courseRequirementRepo;
    private CourseGoalRepo courseGoalRepo;
    private CourseSectionRepo courseSectionRepo;
    private CourseLectureRepo courseLectureRepo;
    private CourseSearchRepo courseSearchRepo;
    private AuthService authService;
    private UserService userService;
    private UserRepo userRepo;
    private ElasticsearchOperations elasticsearchOperations;
    private ModelMapper modelMapper;

    @Autowired
    public CourseService(CourseOrderService courseOrderService, CategoryService categoryService, CourseRepo courseRepo, CourseRequirementRepo courseRequirementRepo,
             CourseGoalRepo courseGoalRepo, CourseSectionRepo courseSectionRepo, CourseLectureRepo courseLectureRepo, CourseSearchRepo courseSearchRepo, AuthService authService, UserService userService, UserRepo userRepo,
             ElasticsearchOperations elasticsearchOperations, ModelMapper modelMapper) {
        this.courseOrderService = courseOrderService;
        this.categoryService = categoryService;
        this.courseRepo = courseRepo;
        this.courseRequirementRepo = courseRequirementRepo;
        this.courseGoalRepo = courseGoalRepo;
        this.courseSectionRepo = courseSectionRepo;
        this.courseLectureRepo = courseLectureRepo;
        this.courseSearchRepo = courseSearchRepo;
        this.authService = authService;
        this.userService = userService;
        this.userRepo = userRepo;
        this.elasticsearchOperations = elasticsearchOperations;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<Object> getCourses(String status) {
        List<Course> courses;
        if (status == null) {
            courses = courseRepo.findAll();
        } else {
            CourseStatus courseStatus;
            try {
                courseStatus = CourseStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return new ResponseEntity<>("Course status: " + status + " does not exist", HttpStatus.NOT_FOUND);
            }
            courses = getCourseByStatus(courseStatus);
        }

        List<CoursesRespDto> coursesDto = courses.stream().map(c -> modelMapper.map(c, CoursesRespDto.class)).collect(Collectors.toList());
        List<Integer> studentsCountList = courses.stream().map(c -> userRepo.getInstructorStudentsCount(c.getInstructor().getId())).collect(Collectors.toList());
        List<Integer> coursesCountList = courses.stream().map(c -> this.getCoursesCountByInstructor(c.getInstructor().getId())).collect(Collectors.toList());

        for (int i = 0; i < coursesDto.size(); i++) {
            coursesDto.get(i).getInstructor().setStudentsCount(studentsCountList.get(i));
            coursesDto.get(i).getInstructor().setCoursesCount(coursesCountList.get(i));
        }
        return ResponseEntity.ok(coursesDto);
    }

    public ResponseEntity<Object> getCoursesByPage(Pageable pageable) {
        Page<Course> coursePages = courseRepo.findAll(pageable);
        return ResponseEntity.ok(coursePages.getContent());
    }

    public Optional<Course> getCourseById(Long courseId) {
        return courseRepo.findById(courseId);
    }

    public Course createCourse(CreateCourseReq createCourseReq) {
        Course course = courseDtoToEntity(createCourseReq);
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

    public ResponseEntity<Map<String, Object>> getCourseTarget(Long courseId) {
        List<CourseRequirement> reqs = courseRequirementRepo.findByCourseId(courseId);
        List<CourseGoal> goals = courseGoalRepo.findByCourseId(courseId);
        Map<String, Object> resp = new HashMap<>();
        Map<String, Object> target = new HashMap<>();
        target.put("reqs", reqs);
        target.put("goals", goals);
        resp.put("target", target);
        return ResponseEntity.ok(resp);
    }

    public ResponseEntity<Object> updateCourseMainInfo(Long courseId, CreateCourseReq updateCourse) {
        Course course = courseRepo.findById(courseId).get();
        if (!course.getInstructor().getId().equals(updateCourse.getInstructorId())) {
            return ResponseEntity.badRequest().body("You are not owner of this course!");
        }
        course = setCourseFields(course, updateCourse);
        course = courseRepo.save(course);
        return ResponseEntity.ok(course);
    }

    public List<CourseRequirement> addCourseReqs(AddCourseRequirement addCourseReq) {
        List<CourseRequirement> courseRequirements = new ArrayList<>();
        for (String req: addCourseReq.getReqs()) {
            if (!courseRequirementRepo.existsByCourseIdAndName(addCourseReq.getCourseId(), req)) {
                CourseRequirement courseRequirement = courseReqDtoToEntity(addCourseReq);
                courseRequirement.setName(req);
                courseRequirements.add(courseRequirement);
            }
        }
        return courseRequirementRepo.saveAll(courseRequirements);
    }

    public List<CourseGoal> addCourseGoals(AddCourseGoal addCourseGoal) {
        List<CourseGoal> courseGoals = new ArrayList<>();
        for (String goal: addCourseGoal.getGoals()) {
            if (!courseGoalRepo.existsByCourseIdAndName(addCourseGoal.getCourseId(), goal)) {
                CourseGoal courseGoal = courseGoalDtoToEntity(addCourseGoal);
                courseGoal.setName(goal);
                courseGoals.add(courseGoal);
            }
        }
        return courseGoalRepo.saveAll(courseGoals);
    }

    public ResponseEntity<Object> addCourseTarget(AddCourseTarget addCourseTarget) {
        AddCourseRequirement addCourseReq = new AddCourseRequirement(addCourseTarget.getCourseId(), addCourseTarget.getReqs());
        List<CourseRequirement> reqs = addCourseReqs(addCourseReq);
        AddCourseGoal addCourseGoal = new AddCourseGoal(addCourseTarget.getCourseId(), addCourseTarget.getGoals());
        List<CourseGoal> goals = addCourseGoals(addCourseGoal);
        return ResponseEntity.ok("Course target added");
    }

    /* Course Section */
    public AddSectionRespDto addCourseSection(Long courseId, String sectionName) {
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new CustomException("Section not found", HttpStatus.NOT_FOUND));
        CourseSection section = new CourseSection(sectionName, course);
        section = courseSectionRepo.save(section);
        AddSectionRespDto addSectionRespDto = modelMapper.map(section, AddSectionRespDto.class);
        addSectionRespDto.setCourseId(course.getId());
        return addSectionRespDto;
    }

    public AddSectionRespDto updateCourseSection(Long courseId, Long sectionId, String sectionName) {
        CourseSection section = courseSectionRepo.findById(sectionId).orElseThrow(() -> new CustomException("Section not found", HttpStatus.NOT_FOUND));
        section.setName(sectionName);
        courseSectionRepo.save(section);
        return modelMapper.map(section, AddSectionRespDto.class);
    }

    public void deleteCourseSection(Long courseId, Long sectionId) {
        CourseSection section = courseSectionRepo.findById(sectionId).orElseThrow(() -> new CustomException("Section not found", HttpStatus.NOT_FOUND));
        courseSectionRepo.delete(section);
    }

    /* Course Lecture */
    public AddLectureRespDto addCourseLecture(Long courseId, Long sectionId, String lectureName) {
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
        CourseSection section = courseSectionRepo.findById(sectionId).orElseThrow(() -> new CustomException("Lecture not found", HttpStatus.NOT_FOUND));
        CourseLecture lecture = new CourseLecture(lectureName, section);
        lecture = courseLectureRepo.save(lecture);
        AddLectureRespDto addLectureRespDto = modelMapper.map(lecture, AddLectureRespDto.class);
        addLectureRespDto.setCourseId(course.getId());
        return addLectureRespDto;
    }

    public AddLectureRespDto updateCourseLecture(Long courseId, Long sectionId, Long lectureId, String lectureName) {
        CourseLecture lecture = courseLectureRepo.findById(lectureId).orElseThrow(() -> new CustomException("Lecture not found", HttpStatus.NOT_FOUND));
        lecture.setName(lectureName);
        courseLectureRepo.save(lecture);
        return modelMapper.map(lecture, AddLectureRespDto.class);
    }

    public void deleteCourseLecture(Long courseId, Long sectionId, Long lectureId) {
        CourseLecture lecture = courseLectureRepo.findById(lectureId).orElseThrow(() -> new CustomException("Lecture not found", HttpStatus.NOT_FOUND));
        courseLectureRepo.delete(lecture);
    }

    public void addCourseLectureContent(Long courseId, Long sectionId, Long lectureId) {
    }



    public void removeCourseReq(Long courseId, String name) {
        courseRequirementRepo.removeByCourseIdAndName(courseId, name);
    }

    public void removeCourseGoal(Long courseId, String name) {
        courseGoalRepo.removeByCourseIdAndName(courseId, name);
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

    public List<Course> getCourseByStatus(CourseStatus courseStatus) {
        return courseRepo.findByStatus(courseStatus);
    }

    public ResponseEntity<Object> updateCourseStatus(Long courseId, String status) {
        Course course = getCourseById(courseId).get();
        CourseStatus courseStatus;
        try {
            courseStatus = CourseStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Course status: " + status + " does not exist", HttpStatus.NOT_FOUND);
        }
        course.setStatus(courseStatus);
        courseRepo.save(course);
        return ResponseEntity.ok("Course status updated");
    }

    public boolean courseTitleExists(String title) {
        return courseRepo.existsByTitleIgnoreCase(title.trim());
    }

    public Course getCourseByTitle(String courseTitle) {
        return courseRepo.findByTitleIgnoreCase(courseTitle);
    }

    public boolean courseExistsByPermaLink(String permaLink) {
        return courseRepo.existsByPermaLink(permaLink);
    }

    public ResponseEntity<Object> getCourseByPermalink (String permaLink) {
        if(!courseExistsByPermaLink(permaLink)) {
            return new ResponseEntity<>("Course with permalink " + permaLink + "not found", HttpStatus.NOT_FOUND);
        }
        Course course = courseRepo.findByPermaLink(permaLink);
        return ResponseEntity.ok(course);
    }

    public List<Course> getCoursesByCategory(String categoryName) throws Exception {
        return courseRepo.findByCategoryNameIgnoreCase(categoryName.trim());
    }

    public ResponseEntity<Object> getCoursesByCategory(String categoryName, String subCategoryName) throws Exception {
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

    public ResponseEntity<Object> getAllCoursesByRootCategory(String categoryName) throws Exception {
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

    public ResponseEntity<Object> getCoursesByTopic(String topicName) {
        if (!categoryService.topicExists(topicName)) {
            return new ResponseEntity<>("Topic " + topicName + " does not exist!", HttpStatus.NOT_FOUND);
        }
        List<Course> courses = courseRepo.findByTopicNameIgnoreCase(topicName.trim());
        return ResponseEntity.ok(courses);
    }

    public List<Course> getCoursesByInstructor(Long instructorId, Integer page) {
        if (page != null) {
            Pageable pageable = PageRequest.of(page, 10);
            return courseRepo.findByInstructorId(instructorId, pageable);
        }
        return courseRepo.findByInstructorId(instructorId, null);
    }

    public int getCoursesCountByInstructor(Long instructorId) {
        return courseRepo.countByInstructorId(instructorId);
    }

    public Course getCourseByInstructor(Long instructorId, Long courseId) {
        boolean isInstructorCourse = isInstructorsCourse(instructorId, courseId);
        if (isInstructorCourse) {
            return courseRepo.findByIdAndInstructorId(courseId, instructorId);
        } else {
            throw new CustomException("Not Instructors Course", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public boolean isInstructorsCourse(Long instructorId, Long courseId) {
        JwtUser instructor = authService.getLoggedInUser();
        System.out.println("LOG: isInstructorsCourse, jwt instructor: " + instructor);
        List<Course> instructorCourses = getCoursesByInstructor(instructor.getId(), null);
        return instructorCourses.stream().anyMatch(c -> c.getId().equals(courseId));
    }

    public List<User> getEnrolledStudents(Long courseId) {
        List<CourseOrder> courseOrders = courseOrderService.getOrdersByCourseId(courseId);
        return courseOrders.stream().map(CourseOrder::getStudent).collect(Collectors.toList());
    }

    private String createPermaLink(String name) {
        return name.toLowerCase()
                .replaceAll("[_!@#$%^&*()-=+:.,|]", " ")
                .replaceAll(" +", " ").trim()
                .replace(" ", "-");
    }


    /*  */
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
    private Course courseDtoToEntity(CreateCourseReq createCourseReq) {
        Course course = new Course();
        return setCourseFields(course, createCourseReq);
    }

    private Course setCourseFields(Course course, CreateCourseReq courseReq) {
        course.setTitle(courseReq.getTitle().trim());
        course.setSubtitle(courseReq.getSubtitle().trim());
        User instructor = userRepo.findById(courseReq.getInstructorId()).orElse(null);
        course.setInstructor(instructor);
        course.setDescription(courseReq.getDescription().trim());
        Level level = Level.valueOf(courseReq.getLevel().trim());
        course.setLevel(level);
        course.setLanguage(courseReq.getLanguage().trim());
        course.setPrice(courseReq.getPrice());
        Category category = categoryService.getCategoryByName(courseReq.getCategory().trim());
        course.setCategory(category);
        Topic topic = categoryService.getTopicByName(courseReq.getTopic().trim());
        course.setTopic(topic);
        course.setImage_name("");
        course.setStatus(CourseStatus.DRAFT);
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

    private CourseRequirement courseReqDtoToEntity(AddCourseRequirement courseReq) {
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
