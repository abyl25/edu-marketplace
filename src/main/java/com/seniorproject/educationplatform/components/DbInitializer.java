package com.seniorproject.educationplatform.components;

import com.seniorproject.educationplatform.models.Category;
import com.seniorproject.educationplatform.models.Role;
import com.seniorproject.educationplatform.models.Topic;
import com.seniorproject.educationplatform.repositories.CategoryRepo;
import com.seniorproject.educationplatform.repositories.RoleRepo;
import com.seniorproject.educationplatform.repositories.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DbInitializer implements CommandLineRunner {
    private RoleRepo roleRepo;
    private CategoryRepo categoryRepo;
    private TopicRepo topicRepo;

    @Autowired
    public DbInitializer(RoleRepo roleRepo, CategoryRepo categoryRepo, TopicRepo topicRepo) {
        this.roleRepo = roleRepo;
        this.categoryRepo = categoryRepo;
        this.topicRepo = topicRepo;
    }

    @Override
    public void run(String... args) throws Exception {
//        populateRoles();
//        populateCategories();
    }

    private void populateRoles() {
        Role r1 = new Role("Admin");
        Role r2 = new Role("Instructor");
        Role r3 = new Role("Student");
        List<Role> roles = Arrays.asList(r1, r2, r3);
        roleRepo.saveAll(roles);
    }

    private void populateCategories() {
        Category c1 = new Category("Development");
        Category c2 = new Category("IT & Software");
        Category c3 = new Category("Finance & Accounting");
        Category c4 = new Category("Business");
        Category c5 = new Category("Design");
        Category c6 = new Category("Marketing");
        Category c7 = new Category("Lifestyle");
        Category c8 = new Category("Office productivity");
        Category c9 = new Category("Personal development");
        Category c10 = new Category("Photography");
        Category c11 = new Category("Music");
        Category c12 = new Category("Health & Fitness");
        Category c13 = new Category("Teaching & Academics");
        Category c14 = new Category("Web Development", c1);
        Category c15 = new Category("Mobile Development", c1);
        Category c16 = new Category("Game Development", c1);
        Category c17 = new Category("Databases", c1);
        Category c18 = new Category("Programming Languages", c1);
        Category c19 = new Category("Software Testing", c1);
        Category c20 = new Category("Software Engineering", c1);
        Category c21 = new Category("Development Tools", c1);
        Category c22 = new Category("E-commerce", c1);
        List<Category> categories = Arrays.asList(c1, c2, c3,c4, c5, c6, c7, c8, c9, c10, c11, c12,
                c13, c14, c15, c16, c17, c18, c19, c20, c21, c22);
        categoryRepo.saveAll(categories);

        Topic t1 = new Topic("HTML", c14);
        Topic t2 = new Topic("JavaScript", c14);
        Topic t3 = new Topic("Angular", c14);
        Topic t4 = new Topic("React", c14);
        Topic t5 = new Topic("Vue.JS", c14);
        Topic t6 = new Topic("Node.JS", c14);
        Topic t7 = new Topic("Bootstrap", c14);
        Topic t8 = new Topic("Django", c14);
        Topic t9 = new Topic("Flask", c14);
        Topic t10 = new Topic("Spring", c14);
        Topic t11 = new Topic("Express", c14);
        Topic t12 = new Topic("Ruby on Rails", c14);
        Topic t13 = new Topic("Java", c18);
        Topic t14 = new Topic("Python", c18);
        Topic t15 = new Topic("C#", c18);
        Topic t16 = new Topic("C++", c18);
        Topic t17 = new Topic("Go", c18);
        Topic t18 = new Topic("Ruby", c18);
        Topic t19 = new Topic("Android", c15);
        Topic t20 = new Topic("Swift", c15);
        Topic t21 = new Topic("React Native", c15);
        Topic t22 = new Topic("Flutter", c15);
        Topic t23 = new Topic("Xamarin", c15);
        Topic t24 = new Topic("Objective C", c15);
        Topic t25 = new Topic("Ionic", c15);
        List<Topic> topics = Arrays.asList(t1, t2, t3,t4, t5, t6, t7, t8, t9, t10, t11, t12,
                t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25);
        topicRepo.saveAll(topics);
    }

}
