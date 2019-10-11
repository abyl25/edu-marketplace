package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User student;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
    private List<CartItem> cartItem = new ArrayList<>();

//    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)  //
//    private List<Course> courses = new ArrayList<>();  // FK

}
